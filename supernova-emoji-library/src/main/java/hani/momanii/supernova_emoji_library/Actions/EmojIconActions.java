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
package hani.momanii.supernova_emoji_library.Actions;

import android.content.Context;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import androidx.fragment.app.FragmentManager;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hani.momanii.supernova_emoji_library.R;
import hani.momanii.supernova_emoji_library.gif.SearchGifFragment;
import hani.momanii.supernova_emoji_library.helper.EmojiconEditText;
import hani.momanii.supernova_emoji_library.helper.EmojiconsPopup;
import hani.momanii.supernova_emoji_library.sticker.StickerData;
import hani.momanii.supernova_emoji_library.sticker.StickerModel;
import hani.momanii.supernova_emoji_library.sticker.StickerView;

/**
 * @author Hani Al Momani (hani.momanii@gmail.com)
 */
public class EmojIconActions implements View.OnFocusChangeListener,
        EmojiconsPopup.OnSearchClicked{
    private boolean useSystemEmoji = false;
    private EmojiconsPopup popup;
    private Context context;
    private View rootView;
    private View onViewClick;
    private ImageView emojiButton;
    private int KeyBoardIcon = R.drawable.ic_action_keyboard;
    private int SmileyIcons = R.drawable.smiley;
    private KeyboardListener keyboardListener;
    private List<EmojiconEditText> emojiconEditTextList = new ArrayList<>();
    private EmojiconEditText emojiconEditText;
    private ArrayList<StickerData> stickerData;
    public StickerOnClickListener stickerOnClickListener;

    /**
     * Constructor
     *
     * @param ctx              The context of current activity.
     * @param rootView         The top most layout in your view hierarchy. The difference of this
     *                         view and the screen height will be used to calculate the keyboard
     *                         height.
     * @param emojiconEditText The Id of EditText.
     * @param emojiButton      The Id of ImageButton used to open Emoji
     */
    public EmojIconActions(Context ctx,
                           View rootView,
                           EmojiconEditText emojiconEditText,
                           ImageView emojiButton,
                           View viewOnClick,
                           String stickerData,
                           StickerOnClickListener stickerOnClickListener) {
        this.emojiButton = emojiButton;
        this.context = ctx;
        this.rootView = rootView;
        this.stickerOnClickListener = stickerOnClickListener;
        addEmojiconEditTextList(emojiconEditText);
        this.popup = new EmojiconsPopup(rootView, ctx, jsonToPojo(stickerData), useSystemEmoji, this);
        this.onViewClick = viewOnClick;
        initListeners();
    }

    /**
     * Constructor
     *
     * @param ctx              The context of current activity.
     * @param rootView         The top most layout in your view hierarchy. The difference of this
     *                         view and the screen height will be used to calculate the keyboard
     *                         height.
     * @param emojiconEditText The Id of EditText.
     */
    public EmojIconActions(Context ctx,
                           View rootView,
                           EmojiconEditText emojiconEditText,
                           View viewOnClick,
                           String stickerData,
                           StickerOnClickListener stickerOnClickListener) {
        addEmojiconEditTextList(emojiconEditText);
        this.context = ctx;
        this.rootView = rootView;
        this.popup = new EmojiconsPopup(rootView, ctx, jsonToPojo(stickerData), useSystemEmoji, this);
        this.onViewClick = viewOnClick;
        this.stickerOnClickListener = stickerOnClickListener;
        initListeners();
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (hasFocus) {
            if (view instanceof EmojiconEditText) {
                emojiconEditText = (EmojiconEditText) view;
            }
        }
    }

    private void refresh() {
        popup.updateUseSystemDefault(useSystemEmoji);
    }

    private void initEmojiButtonListener() {
        if (emojiButton != null) {
            emojiButton.setOnClickListener(v -> togglePopupVisibility());
        }
        onViewClick.setOnClickListener(v -> togglePopupVisibility());
    }

    private void togglePopupVisibility() {
        if (popup != null && !popup.isShowing()) {
            showPopup();
        } else {
            hidePopup();
        }
    }

    private void changeEmojiKeyboardIcon(ImageView iconToBeChanged, int drawableResourceId) {
        if (iconToBeChanged != null) {
            iconToBeChanged.setImageResource(drawableResourceId);
        }
    }

    public void initListeners() {
        if (emojiconEditText == null) {
            emojiconEditText = emojiconEditTextList.get(0);
        }
        //Will automatically set size according to the soft keyboard size
        popup.setSizeForSoftKeyboard();

        //If the emoji popup is dismissed, change emojiButton to smiley icon
        popup.setOnDismissListener(() -> changeEmojiKeyboardIcon(emojiButton, SmileyIcons));

        //If the text keyboard closes, also dismiss the emoji popup
        popup.setOnSoftKeyboardOpenCloseListener(new EmojiconsPopup.OnSoftKeyboardOpenCloseListener() {
            @Override
            public void onKeyboardOpen(int keyBoardHeight) {
                if (keyboardListener != null) {
                    keyboardListener.onKeyboardOpen();
                }
            }

            @Override
            public void onKeyboardClose() {
                if (keyboardListener != null) {
                    keyboardListener.onKeyboardClose();
                }
                if (popup != null && popup.isShowing()) {
                    popup.dismiss();
                }
            }
        });

        //On emoji clicked, add it to edittext
        popup.setOnEmojiconClickedListener(emojicon -> {
            if (emojicon == null) {
                return;
            }
            int start = emojiconEditText.getSelectionStart();
            int end = emojiconEditText.getSelectionEnd();
            if (start < 0) {
                emojiconEditText.append(emojicon.getEmoji());
            } else {
                Editable editableText = emojiconEditText.getText();
                if (editableText != null) {
                    editableText.replace(
                            Math.min(start, end),
                            Math.max(start, end),
                            emojicon.getEmoji(),
                            0,
                            emojicon.getEmoji().length());
                }
            }
        });

        //On backspace clicked, emulate the KEYCODE_DEL key event
        popup.setOnEmojiconBackspaceClickedListener(v -> {
            KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
            emojiconEditText.dispatchKeyEvent(event);
        });

        popup.setOnStickerClickedListener((url, type) -> {
            stickerOnClickListener.onClick(url, type);
        });


        // To toggle between text keyboard and emoji keyboard keyboard(Popup)
        initEmojiButtonListener();
    }

    public void addEmojiconEditTextList(EmojiconEditText... emojiconEditText) {
        Collections.addAll(emojiconEditTextList, emojiconEditText);
        for (EmojiconEditText editText : emojiconEditText) {
            editText.setOnFocusChangeListener(this);
        }
    }

    public EmojiconsPopup getPopup() {
        return popup;
    }

    /**
     * @param emojiButton The Id of ImageButton used to open Emoji
     */
    public void setEmojiButton(ImageView emojiButton) {
        this.emojiButton = emojiButton;
        initEmojiButtonListener();
    }

    /**
     * @param iconPressedColor The color of icons on tab
     * @param tabsColor        The color of tabs background
     * @param backgroundColor  The color of emoji background
     */
    public void setColors(int iconPressedColor, int tabsColor, int backgroundColor) {
        this.popup.setColors(iconPressedColor, tabsColor, backgroundColor);
    }

    public void setIconsIds(int keyboardIcon, int smileyIcon) {
        this.KeyBoardIcon = keyboardIcon;
        this.SmileyIcons = smileyIcon;
    }

//    public void setUseSystemEmoji(boolean useSystemEmoji) {
//        this.useSystemEmoji = useSystemEmoji;
//        for (EmojiconEditText editText : emojiconEditTextList) {
//            editText.setUseSystemDefault(useSystemEmoji);
//        }
//        refresh();
//    }

    public void showPopup() {
        if (emojiconEditText == null) {
            emojiconEditText = emojiconEditTextList.get(0);
        }
        if (popup.isKeyBoardOpen()) {
            popup.showAtBottom();
        } else {
            emojiconEditText.setFocusableInTouchMode(true);
            emojiconEditText.requestFocus();
            final InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                inputMethodManager.showSoftInput(emojiconEditText, InputMethodManager.SHOW_IMPLICIT);
            }
            popup.showAtBottomPending();
        }
        changeEmojiKeyboardIcon(emojiButton, KeyBoardIcon);
    }

    public void hidePopup() {
        if (popup != null && popup.isShowing()) {
            popup.dismiss();
        }
    }

    @Override
    public void onSearchClicked() {
        stickerOnClickListener.onSearchClick();
    }

    private ArrayList<StickerData> jsonToPojo(String json){
        Gson gson = new Gson();
        StickerModel a = gson.fromJson(json, StickerModel.class);
        if (a.getData()!=null){
            stickerData.addAll(a.getData());
        }
        return stickerData;
    }


    public interface StickerOnClickListener {
        void onClick(String url, String type);

        void onSearchClick();
    }

    public interface KeyboardListener {
        void onKeyboardOpen();

        void onKeyboardClose();
    }

    public void setKeyboardListener(KeyboardListener listener) {
        this.keyboardListener = listener;
    }

}
