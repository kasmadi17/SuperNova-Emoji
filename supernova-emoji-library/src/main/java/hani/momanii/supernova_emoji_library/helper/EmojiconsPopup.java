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

package hani.momanii.supernova_emoji_library.helper;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import hani.momanii.supernova_emoji_library.R;
import hani.momanii.supernova_emoji_library.emojiview.EmojiView;
import hani.momanii.supernova_emoji_library.gif.GifView;
import hani.momanii.supernova_emoji_library.sticker.StickerData;
import hani.momanii.supernova_emoji_library.sticker.StickerView;

/**
 * @author Hani Al Momani (hani.momanii@gmail.com)
 * @author Damai Subimawanto (damai.subimawanto@gmail.com)
 */

public class EmojiconsPopup extends PopupWindow {
    private int mEmojiTabLastSelectedIndex = -1;
    //    private View[] mEmojiTabs;
//    private PagerAdapter mEmojisAdapter;
//    private EmojiconRecentsManager mRecentsManager;
    public ArrayList<String> gift= new ArrayList<>();
    private int keyBoardHeight = 0;
    private Boolean pendingOpen = false;
    private Boolean isOpened = false;
    public EmojiconGridView.OnEmojiconClickedListener onEmojiconClickedListener;
    public StickerView.OnStickerClick onStickerClick;
    public OnEmojiBackspaceClickedListener onEmojiBackspaceClickedListener;
    public OnSearchClicked onSearchClicked;
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
    EmojiView emojiFragment;
    View[] fragment = new View[3];
    StickerView stickerView;

    private ViewPager emojisPager;
    private ArrayList<StickerData> stickerData;

    /**
     * Constructor
     *
     * @param rootView         The top most layout in your view hierarchy. The difference of this view and the screen height will be used to calculate the keyboard height.
     * @param mContext         The context of current activity.
     * @param useSystemDefault .
     */
    public EmojiconsPopup(View rootView, Context mContext,
                          ArrayList<StickerData> stickerData,
                          boolean useSystemDefault,
                          OnSearchClicked onSearchClicked) {
        super(mContext);
        this.stickerData = stickerData;
        this.mUseSystemDefault = useSystemDefault;
        this.mContext = mContext;
        this.rootView = rootView;
        this.onSearchClicked = onSearchClicked;
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
     * Set the listener for the event when any of the sticker is clicked
     */
    public void setOnStickerClickedListener(StickerView.OnStickerClick listener) {
        this.onStickerClick = listener;
    }

    /**
     * Set the listener for the event when backspace on emojicon popup is clicked
     */
    public void setOnEmojiconBackspaceClickedListener(OnEmojiBackspaceClickedListener listener) {
        this.onEmojiBackspaceClickedListener = listener;

    }

    /**
     * Use this function to show the emoji popup.
     * NOTE: Since, the soft keyboard sizes are variable on different android devices, the
     * library needs you to open the soft keyboard atleast once before calling this function.
     * If that is not possible see showAtBottomPending() function.
     */
    public void showAtBottom() {
        Log.i("EmojIconActions", "showAtBottom: ");
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
            Log.i("EmojIconActions", "showAtBottomPending:isKeyBoardOpen");
        } else {
            Log.i("EmojIconActions", "showAtBottomPending:keyboardNotOpen");
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
                .addOnGlobalLayoutListener(() -> {
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
                            showAtBottom();
                            if (pendingOpen) {
                                pendingOpen = false;
                            }
                        } else {
                            isOpened = false;
                            if (onSoftKeyboardOpenCloseListener != null) {
                                onSoftKeyboardOpenCloseListener.onKeyboardClose();
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
//            mEmojisAdapter = null;
            positionPager = emojisPager.getCurrentItem();
            dismiss();
            this.mUseSystemDefault = mUseSystemDefault;
            setContentView(createCustomView());
            //mEmojisAdapter.notifyDataSetChanged();
//            mEmojiTabs[positionPager].setSelected(true);
//            emojisPager.setCurrentItem(positionPager);
//            onPageSelected(positionPager);
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
        EmojiView emojiFragment = new EmojiView(mContext, this);
        stickerView = new StickerView(mContext, stickerData, this);
        fragment[0] = emojiFragment.getRootView();
        fragment[1] = stickerView.getRootView();
        fragment[2] = new GifView(mContext, this).getRootView();
        MainPagerAdapter adapter = new MainPagerAdapter(fragment);
        ViewPager viewPager = view.findViewById(R.id.viewPager);
        TabLayout tabs = view.findViewById(R.id.tabLayout);
        ImageView imgSearch = view.findViewById(R.id.imgSearch);
        imgSearch.setVisibility(View.GONE);
        viewPager.setAdapter(adapter);

        tabs.setupWithViewPager(viewPager);
        tabs.setSelectedTabIndicator(null);
        tabs.getTabAt(0).setIcon(R.drawable.ic_emoji);
        tabs.getTabAt(1).setIcon(R.drawable.ic_sticker);
        tabs.getTabAt(2).setIcon(R.drawable.ic_gif);
        tabs.setTabIconTint(ColorStateList.valueOf(Color.parseColor("#8F8F8F")));
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_IN);
                if (tab.getPosition() == 2){
                    imgSearch.setVisibility(View.VISIBLE);
                }else{
                    imgSearch.setVisibility(View.GONE);
                }
                Log.i("TAG", "onTabSelected: "+tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.parseColor("#8F8F8F"), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_IN);
            }
        });
        tabs.getTabAt(0).select();

        imgSearch.setOnClickListener(view -> {
            onSearchClicked.onSearchClicked();
        });


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

    public void setData(ArrayList<StickerData> stickerData){
        this.stickerData = stickerData;
        stickerView.setData(stickerData);
    }


    public interface OnEmojiBackspaceClickedListener {
        void onEmojiBackspaceClicked(View v);
    }

    public interface OnSearchClicked{
        void onSearchClicked();
    }

    public interface OnSoftKeyboardOpenCloseListener {
        void onKeyboardOpen(int keyBoardHeight);
        void onKeyboardClose();
    }

}