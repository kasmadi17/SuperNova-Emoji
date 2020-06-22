/*
 * Copyright 2016 Hani Al Momani
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package hani.momanii.supernova_emoji_library.Helper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.Arrays;
import java.util.List;

import hani.momanii.supernova_emoji_library.R;
import hani.momanii.supernova_emoji_library.emoji.Cars;
import hani.momanii.supernova_emoji_library.emoji.Electr;
import hani.momanii.supernova_emoji_library.emoji.Emojicon;
import hani.momanii.supernova_emoji_library.emoji.Food;
import hani.momanii.supernova_emoji_library.emoji.Nature;
import hani.momanii.supernova_emoji_library.emoji.People;
import hani.momanii.supernova_emoji_library.emoji.Sport;
import hani.momanii.supernova_emoji_library.emoji.Symbols;

/**
 * @author Hani Al Momani (hani.momanii@gmail.com)
 * @author Damai Subimawanto (damai.subimawanto@gmail.com)
 */

public class EmojiconsPopup extends PopupWindow implements ViewPager.OnPageChangeListener, EmojiconRecents {
    private int mEmojiTabLastSelectedIndex = -1;
    private View[] mEmojiTabs;
    private PagerAdapter mEmojisAdapter;
    private EmojiconRecentsManager mRecentsManager;
    private int keyBoardHeight = 0;
    private Boolean pendingOpen = false;
    private Boolean isOpened = false;
    public EmojiconGridView.OnEmojiconClickedListener onEmojiconClickedListener;
    OnEmojiconBackspaceClickedListener onEmojiconBackspaceClickedListener;
    OnSoftKeyboardOpenCloseListener onSoftKeyboardOpenCloseListener;
    View rootView;
    Context mContext;
    boolean mUseSystemDefault;
    View view;
    int positionPager = 0;
    int iconPressedColor = Color.parseColor("#495C66");
    int tabsColor = Color.parseColor("#DCE1E2");
    int backgroundColor = Color.parseColor("#E6EBEF");
    Integer systemNavigationBarHeight = null;

    private ViewPager emojisPager;

    /**
     * Constructor
     *
     * @param rootView         The top most layout in your view hierarchy. The difference of this view and the screen height will be used to calculate the keyboard height.
     * @param mContext         The context of current activity.
     * @param useSystemDefault .
     */
    public EmojiconsPopup(View rootView, Context mContext, boolean useSystemDefault) {
        super(mContext);
        this.mUseSystemDefault = useSystemDefault;
        this.mContext = mContext;
        this.rootView = rootView;
        View customView = createCustomView();
        setContentView(customView);
        setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        setSize(LayoutParams.MATCH_PARENT, 255);
        setBackgroundDrawable(null);
    }

    /**
     * Set the listener for the event of keyboard opening or closing.
     */
    public void setOnSoftKeyboardOpenCloseListener(OnSoftKeyboardOpenCloseListener listener) {
        this.onSoftKeyboardOpenCloseListener = listener;
    }

    /**
     * Set the listener for the event when any of the emojicon is clicked
     */
    public void setOnEmojiconClickedListener(EmojiconGridView.OnEmojiconClickedListener listener) {
        this.onEmojiconClickedListener = listener;
    }

    /**
     * Set the listener for the event when backspace on emojicon popup is clicked
     */
    public void setOnEmojiconBackspaceClickedListener(OnEmojiconBackspaceClickedListener listener) {
        this.onEmojiconBackspaceClickedListener = listener;
    }

    /**
     * Use this function to show the emoji popup.
     * NOTE: Since, the soft keyboard sizes are variable on different android devices, the
     * library needs you to open the soft keyboard atleast once before calling this function.
     * If that is not possible see showAtBottomPending() function.
     */
    public void showAtBottom() {
        showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
    }

    /**
     * Use this function when the soft keyboard has not been opened yet. This
     * will show the emoji popup after the keyboard is up next time.
     * Generally, you will be calling InputMethodManager.showSoftInput function after
     * calling this function.
     */
    public void showAtBottomPending() {
        if (isKeyBoardOpen()) {
            showAtBottom();
        } else {
            pendingOpen = true;
        }
    }

    /**
     * @return Returns true if the soft keyboard is open, false otherwise.
     */
    public Boolean isKeyBoardOpen() {
        return isOpened;
    }

    /**
     * Dismiss the popup
     */
    @Override
    public void dismiss() {
        super.dismiss();
        EmojiconRecentsManager.getInstance(mContext)
                .saveRecents();
    }

    /**
     * Call this function to resize the emoji popup according to your soft keyboard size
     */
    public void setSizeForSoftKeyboard() {
        rootView.getViewTreeObserver()
                .addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (mContext instanceof Activity) {
                            int heightDifference = getHeightDifference(mContext);
                            if (heightDifference > 0) {
                                if (systemNavigationBarHeight == null) {
                                    /* Get layout height when the layout was created at first time */
                                    systemNavigationBarHeight = heightDifference;
                                }
                            } else {
                                systemNavigationBarHeight = 0;
                            }

                            if (heightDifference > getDefaultNavigationBarHeight(mContext)) {
                                keyBoardHeight = heightDifference - systemNavigationBarHeight;
                                setSize(LayoutParams.MATCH_PARENT, keyBoardHeight);
                                if (!isOpened) {
                                    if (onSoftKeyboardOpenCloseListener != null) {
                                        onSoftKeyboardOpenCloseListener.onKeyboardOpen(keyBoardHeight);
                                    }
                                }
                                isOpened = true;
                                if (pendingOpen) {
                                    showAtBottom();
                                    pendingOpen = false;
                                }
                            } else {
                                isOpened = false;
                                if (onSoftKeyboardOpenCloseListener != null) {
                                    onSoftKeyboardOpenCloseListener.onKeyboardClose();
                                }
                            }
                        }
                    }
                });
    }

    /**
     * Manually set the popup window size
     *
     * @param width  Width of the popup
     * @param height Height of the popup
     */
    public void setSize(int width, int height) {
        setWidth(width);
        setHeight(height);
    }

    public void updateUseSystemDefault(boolean mUseSystemDefault) {
        if (view != null) {
            mEmojisAdapter = null;
            positionPager = emojisPager.getCurrentItem();
            dismiss();

            this.mUseSystemDefault = mUseSystemDefault;
            setContentView(createCustomView());
            //mEmojisAdapter.notifyDataSetChanged();
            mEmojiTabs[positionPager].setSelected(true);
            emojisPager.setCurrentItem(positionPager);
            onPageSelected(positionPager);
            if (!isShowing()) {
                if (isKeyBoardOpen()) {
                    // If keyboard is visible, simply show the emoji popup
                    showAtBottom();
                    // changeEmojiKeyboardIcon(emojiButton, R.drawable.ic_action_keyboard);
                } else {
                    // else, open the text keyboard first and immediately after that show the emoji popup
                    showAtBottomPending();
                    // changeEmojiKeyboardIcon(emojiButton, R.drawable.ic_action_keyboard);
                }
            }
        }
    }

    private View createCustomView() {
        view = LayoutInflater.from(mContext).inflate(R.layout.emojicons, null, false);
        emojisPager = view.findViewById(R.id.emojis_pager);
        LinearLayout tabs = view.findViewById(R.id.emojis_tab);

        emojisPager.clearOnPageChangeListeners();
        emojisPager.addOnPageChangeListener(this);
        EmojiconRecents recents = this;
        mEmojisAdapter = new EmojisPagerAdapter(Arrays.asList(
                new EmojiconRecentsGridView(mContext, null, null, this, mUseSystemDefault),
                new EmojiconGridView(mContext, People.DATA, recents, this, mUseSystemDefault),
                new EmojiconGridView(mContext, Nature.DATA, recents, this, mUseSystemDefault),
                new EmojiconGridView(mContext, Food.DATA, recents, this, mUseSystemDefault),
                new EmojiconGridView(mContext, Sport.DATA, recents, this, mUseSystemDefault),
                new EmojiconGridView(mContext, Cars.DATA, recents, this, mUseSystemDefault),
                new EmojiconGridView(mContext, Electr.DATA, recents, this, mUseSystemDefault),
                new EmojiconGridView(mContext, Symbols.DATA, recents, this, mUseSystemDefault)
        ));
        emojisPager.setAdapter(mEmojisAdapter);
        mEmojiTabs = new View[8];

        mEmojiTabs[0] = view.findViewById(R.id.emojis_tab_0_recents);
        mEmojiTabs[1] = view.findViewById(R.id.emojis_tab_1_people);
        mEmojiTabs[2] = view.findViewById(R.id.emojis_tab_2_nature);
        mEmojiTabs[3] = view.findViewById(R.id.emojis_tab_3_food);
        mEmojiTabs[4] = view.findViewById(R.id.emojis_tab_4_sport);
        mEmojiTabs[5] = view.findViewById(R.id.emojis_tab_5_cars);
        mEmojiTabs[6] = view.findViewById(R.id.emojis_tab_6_elec);
        mEmojiTabs[7] = view.findViewById(R.id.emojis_tab_7_sym);
        for (int i = 0; i < mEmojiTabs.length; i++) {
            final int position = i;
            mEmojiTabs[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    emojisPager.setCurrentItem(position);
                }
            });
        }

        emojisPager.setBackgroundColor(backgroundColor);
        tabs.setBackgroundColor(tabsColor);
        for (View mEmojiTab : mEmojiTabs) {
            ImageButton btn = (ImageButton) mEmojiTab;
            btn.setColorFilter(iconPressedColor);
        }

        ImageButton imgBtn = view.findViewById(R.id.emojis_backspace);
        imgBtn.setColorFilter(iconPressedColor);
        imgBtn.setBackgroundColor(backgroundColor);

        view.findViewById(R.id.emojis_backspace)
                .setOnTouchListener(new RepeatListener(500, 50, new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (onEmojiconBackspaceClickedListener != null) {
                            onEmojiconBackspaceClickedListener.onEmojiconBackspaceClicked(v);
                        }
                    }
                }));

        // get last selected page
        mRecentsManager = EmojiconRecentsManager.getInstance(view.getContext());
        int page = mRecentsManager.getRecentPage();
        // last page was recents, check if there are recents to use
        // if none was found, go to page 1
        if (page == 0 && mRecentsManager.size() == 0) {
            page = 1;
        }

        if (page == 0) {
            onPageSelected(page);
        } else {
            emojisPager.setCurrentItem(page, false);
        }
        return view;
    }

    private int getHeightDifference(Context context) {
        Point screenSize = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            ((Activity) context).getWindowManager().getDefaultDisplay().getRealSize(screenSize);
        } else {
            ((Activity) context).getWindowManager().getDefaultDisplay().getSize(screenSize);
        }

        Rect rect = new Rect();
        rootView.getWindowVisibleDisplayFrame(rect);
        return screenSize.y - rect.bottom;
    }

    private int getDefaultNavigationBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 100;
    }

    @Override
    public void addRecentEmoji(Context context, Emojicon emojicon) {
        PagerAdapter pagerAdapter = emojisPager.getAdapter();
        if (pagerAdapter instanceof EmojisPagerAdapter) {
            EmojiconRecentsGridView fragment = ((EmojisPagerAdapter) emojisPager.getAdapter()).getRecentFragment();
            fragment.addRecentEmoji(context, emojicon);
        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {}

    @Override
    public void onPageSelected(int i) {
        if (mEmojiTabLastSelectedIndex == i) {
            return;
        }
        switch (i) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:

                if (mEmojiTabLastSelectedIndex >= 0 && mEmojiTabLastSelectedIndex < mEmojiTabs.length) {
                    mEmojiTabs[mEmojiTabLastSelectedIndex].setSelected(false);
                }
                mEmojiTabs[i].setSelected(true);
                mEmojiTabLastSelectedIndex = i;
                mRecentsManager.setRecentPage(i);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {}

    /**
     * @param iconPressedColor The color of icons on tab
     * @param tabsColor        The color of tabs background
     * @param backgroundColor  The color of emoji background
     */
    public void setColors(final int iconPressedColor, final int tabsColor, final int backgroundColor) {
        this.backgroundColor = backgroundColor;
        this.iconPressedColor = iconPressedColor;
        this.tabsColor = tabsColor;
    }

    private static class EmojisPagerAdapter extends PagerAdapter {
        private List<EmojiconGridView> views;

        public EmojiconRecentsGridView getRecentFragment() {
            for (EmojiconGridView it : views) {
                if (it instanceof EmojiconRecentsGridView) {
                    return (EmojiconRecentsGridView) it;
                }
            }
            return null;
        }

        public EmojisPagerAdapter(List<EmojiconGridView> views) {
            super();
            this.views = views;
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View v = views.get(position).rootView;
            container.addView(v, 0);
            return v;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object view) {
            container.removeView((View) view);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object key) {
            return key == view;
        }
    }

    /**
     * A class, that can be used as a TouchListener on any view (e.g. a Button).
     * It cyclically runs a clickListener, emulating keyboard-like behaviour. First
     * click is fired immediately, next before initialInterval, and subsequent before
     * normalInterval.
     * <p/>
     * <p>Interval is scheduled before the onClick completes, so it has to run fast.
     * If it runs slow, it does not generate skipped onClicks.
     */
    public static class RepeatListener implements View.OnTouchListener {

        private Handler handler = new Handler();

        private int initialInterval;
        private final int normalInterval;
        private final OnClickListener clickListener;

        private Runnable handlerRunnable = new Runnable() {
            @Override
            public void run() {
                if (downView == null) {
                    return;
                }
                handler.removeCallbacksAndMessages(downView);
                handler.postAtTime(this, downView, SystemClock.uptimeMillis() + normalInterval);
                clickListener.onClick(downView);
            }
        };

        private View downView;

        /**
         * @param initialInterval The interval before first click event
         * @param normalInterval  The interval before second and subsequent click
         *                        events
         * @param clickListener   The OnClickListener, that will be called
         *                        periodically
         */
        public RepeatListener(int initialInterval, int normalInterval, OnClickListener clickListener) {
            if (clickListener == null) {
                throw new IllegalArgumentException("null runnable");
            }
            if (initialInterval < 0 || normalInterval < 0) {
                throw new IllegalArgumentException("negative interval");
            }

            this.initialInterval = initialInterval;
            this.normalInterval = normalInterval;
            this.clickListener = clickListener;
        }

        @SuppressLint("ClickableViewAccessibility")
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downView = view;
                    handler.removeCallbacks(handlerRunnable);
                    handler.postAtTime(handlerRunnable, downView, SystemClock.uptimeMillis() + initialInterval);
                    clickListener.onClick(view);
                    return true;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_OUTSIDE:
                    handler.removeCallbacksAndMessages(downView);
                    downView = null;
                    return true;
            }
            return false;
        }
    }

    public interface OnEmojiconBackspaceClickedListener {
        void onEmojiconBackspaceClicked(View v);
    }

    public interface OnSoftKeyboardOpenCloseListener {
        void onKeyboardOpen(int keyBoardHeight);
        void onKeyboardClose();
    }
}