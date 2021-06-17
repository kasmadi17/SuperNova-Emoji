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
package hani.momanii.supernova_emoji_library.Actions

import android.content.Context
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import com.google.gson.Gson
import hani.momanii.supernova_emoji_library.R
import hani.momanii.supernova_emoji_library.emoji.Emojicon
import hani.momanii.supernova_emoji_library.helper.EmojiconEditText
import hani.momanii.supernova_emoji_library.helper.EmojiconsPopup
import hani.momanii.supernova_emoji_library.helper.EmojiconsPopup.OnSearchClicked
import hani.momanii.supernova_emoji_library.helper.EmojiconsPopup.OnSoftKeyboardOpenCloseListener
import hani.momanii.supernova_emoji_library.sticker.StickerData
import hani.momanii.supernova_emoji_library.sticker.StickerModel
import hani.momanii.supernova_emoji_library.sticker.StickerView.OnStickerClick
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author Hani Al Momani (hani.momanii@gmail.com)
 */
class EmojIconActions : View.OnFocusChangeListener, OnSearchClicked {
    private val useSystemEmoji = false
    var popup: EmojiconsPopup?
        private set
    private var context: Context
    private var rootView: View
    private var onViewClick: View
    private var emojiButton: ImageView? = null
    private var KeyBoardIcon = R.drawable.ic_action_keyboard
    private var SmileyIcons = R.drawable.smiley
    private var keyboardListener: KeyboardListener? = null
    private val emojiconEditTextList: MutableList<EmojiconEditText> = mutableListOf()
    private var emojiconEditText: EmojiconEditText? = null
    private val stickerData: ArrayList<StickerData> = arrayListOf()
    var stickerOnClickListener: StickerOnClickListener

    /**
     * Constructor
     *
     * @param ctx              The context of current activity.
     * @param rootView         The top most layout in your view hierarchy. The difference of this
     * view and the screen height will be used to calculate the keyboard
     * height.
     * @param emojiconEditText The Id of EditText.
     * @param emojiButton      The Id of ImageButton used to open Emoji
     */
    constructor(
        ctx: Context,
        rootView: View,
        emojiconEditText: EmojiconEditText?,
        emojiButton: ImageView?,
        viewOnClick: View,
        stickerData: String,
        stickerOnClickListener: StickerOnClickListener
    ) {
        jsonToPojo("")
        this.emojiButton = emojiButton
        context = ctx
        this.rootView = rootView
        this.stickerOnClickListener = stickerOnClickListener
        addEmojiconEditTextList(emojiconEditText!!)
        popup = EmojiconsPopup(rootView, ctx, jsonToPojo(stickerData), useSystemEmoji, this)
        onViewClick = viewOnClick
        initListeners()
    }

    /**
     * Constructor
     *
     * @param ctx              The context of current activity.
     * @param rootView         The top most layout in your view hierarchy. The difference of this
     * view and the screen height will be used to calculate the keyboard
     * height.
     * @param emojiconEditText The Id of EditText.
     */
    constructor(
        ctx: Context,
        rootView: View,
        emojiconEditText: EmojiconEditText?,
        viewOnClick: View,
        stickerData: String,
        stickerOnClickListener: StickerOnClickListener
    ) {
        addEmojiconEditTextList(emojiconEditText!!)
        context = ctx
        this.rootView = rootView
        popup = EmojiconsPopup(rootView, ctx, jsonToPojo(stickerData), useSystemEmoji, this)
        onViewClick = viewOnClick
        this.stickerOnClickListener = stickerOnClickListener
        initListeners()
    }

    override fun onFocusChange(view: View, hasFocus: Boolean) {
        if (hasFocus) {
            if (view is EmojiconEditText) {
                emojiconEditText = view
            }
        }
    }

    private fun refresh() {
        popup?.updateUseSystemDefault(useSystemEmoji)
    }

    private fun initEmojiButtonListener() {
        if (emojiButton != null) {
            emojiButton?.setOnClickListener { v: View? -> togglePopupVisibility() }
        }
        onViewClick.setOnClickListener { v: View? -> togglePopupVisibility() }
    }

    private fun togglePopupVisibility() {
        if (popup != null && !popup!!.isShowing) {
            showPopup()
        } else {
            hidePopup()
        }
    }

    private fun changeEmojiKeyboardIcon(iconToBeChanged: ImageView?, drawableResourceId: Int) {
        iconToBeChanged?.setImageResource(drawableResourceId)
    }

    fun initListeners() {
        if (emojiconEditText == null) {
            emojiconEditText = emojiconEditTextList[0]
        }
        //Will automatically set size according to the soft keyboard size
        popup?.setSizeForSoftKeyboard()

        //If the emoji popup is dismissed, change emojiButton to smiley icon
        popup?.setOnDismissListener { changeEmojiKeyboardIcon(emojiButton, SmileyIcons) }

        //If the text keyboard closes, also dismiss the emoji popup
        popup?.setOnSoftKeyboardOpenCloseListener(object : OnSoftKeyboardOpenCloseListener {
            override fun onKeyboardOpen(keyBoardHeight: Int) {
                if (keyboardListener != null) {
                    keyboardListener!!.onKeyboardOpen()
                }
            }

            override fun onKeyboardClose() {
                if (keyboardListener != null) {
                    keyboardListener!!.onKeyboardClose()
                }
                if (popup != null && popup!!.isShowing) {
                    popup!!.dismiss()
                }
            }
        })

        //On emoji clicked, add it to edittext
        popup?.setOnEmojiconClickedListener { emojicon: Emojicon? ->
            if (emojicon == null) {
                return@setOnEmojiconClickedListener
            }
            val start = emojiconEditText!!.selectionStart
            val end = emojiconEditText!!.selectionEnd
            if (start < 0) {
                emojiconEditText!!.append(emojicon.emoji)
            } else {
                val editableText = emojiconEditText!!.text
                editableText?.replace(
                    Math.min(start, end),
                    Math.max(start, end),
                    emojicon.emoji,
                    0,
                    emojicon.emoji.length
                )
            }
        }

        //On backspace clicked, emulate the KEYCODE_DEL key event
        popup?.setOnEmojiconBackspaceClickedListener { v: View? ->
            val event =
                KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL)
            emojiconEditText!!.dispatchKeyEvent(event)
        }

        popup?.setOnStickerClickedListener(object : OnStickerClick {
            override fun onStickerClicked(url: String?, type: String) {
                stickerOnClickListener.onClick(url, type)
            }

        })


        // To toggle between text keyboard and emoji keyboard keyboard(Popup)
        initEmojiButtonListener()
    }

    fun addEmojiconEditTextList(vararg emojiconEditText: EmojiconEditText) {
        Collections.addAll(emojiconEditTextList, *emojiconEditText)
        for (editText in emojiconEditText) {
            editText.onFocusChangeListener = this
        }
    }

    /**
     * @param emojiButton The Id of ImageButton used to open Emoji
     */
    fun setEmojiButton(emojiButton: ImageView?) {
        this.emojiButton = emojiButton
        initEmojiButtonListener()
    }

    /**
     * @param iconPressedColor The color of icons on tab
     * @param tabsColor        The color of tabs background
     * @param backgroundColor  The color of emoji background
     */
    fun setColors(iconPressedColor: Int, tabsColor: Int, backgroundColor: Int) {
        popup?.setColors(iconPressedColor, tabsColor, backgroundColor)
    }

    fun setIconsIds(keyboardIcon: Int, smileyIcon: Int) {
        KeyBoardIcon = keyboardIcon
        SmileyIcons = smileyIcon
    }

    //    public void setUseSystemEmoji(boolean useSystemEmoji) {
    //        this.useSystemEmoji = useSystemEmoji;
    //        for (EmojiconEditText editText : emojiconEditTextList) {
    //            editText.setUseSystemDefault(useSystemEmoji);
    //        }
    //        refresh();
    //    }
    fun showPopup() {
        if (emojiconEditText == null) {
            emojiconEditText = emojiconEditTextList[0]
        }
        if (popup!!.isKeyBoardOpen) {
            popup!!.showAtBottom()
        } else {
            emojiconEditText!!.isFocusableInTouchMode = true
            emojiconEditText!!.requestFocus()
            val inputMethodManager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(
                emojiconEditText,
                InputMethodManager.SHOW_IMPLICIT
            )
            popup!!.showAtBottomPending()
        }
        changeEmojiKeyboardIcon(emojiButton, KeyBoardIcon)
    }

    fun hidePopup() {
        if (popup != null && popup!!.isShowing) {
            popup!!.dismiss()
        }
    }

    override fun onSearchClicked() {
        stickerOnClickListener.onSearchClick()
    }

    private fun jsonToPojo(json: String): ArrayList<StickerData>? {
        val gson = Gson()
        val data = gson.fromJson(json, StickerModel::class.java)
        if (data != null) {
            data.data?.map {
                it.imageHeader = data.meta?.imagePath + 354 + it.imageHeader
                it.sticker?.map { it1 ->
                    it1.url = data.meta?.imagePath + 354 + it1.url
                }
            }
            data.data?.let { stickerData.addAll(it) }
            return stickerData
        }
        return null
    }

    fun setStickerData(data:String){
        popup?.setData(jsonToPojo(data))
    }

    interface StickerOnClickListener {
        fun onClick(url: String?, type: String?)
        fun onSearchClick()
    }

    interface KeyboardListener {
        fun onKeyboardOpen()
        fun onKeyboardClose()
    }

    fun setKeyboardListener(listener: KeyboardListener?) {
        keyboardListener = listener
    }
}