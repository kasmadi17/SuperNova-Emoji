package momanii.hani.supernova_emoji;

import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.gif.SearchGifFragment;
import hani.momanii.supernova_emoji_library.helper.EmojiconEditText;
import hani.momanii.supernova_emoji_library.helper.EmojiconTextView;
import hani.momanii.supernova_emoji_library.sticker.StickerData;
import hani.momanii.supernova_emoji_library.sticker.StickerItem;

public class MainActivity extends AppCompatActivity {
    CheckBox mCheckBox;
    EmojiconEditText emojiconEditText, emojiconEditText2;
    EmojiconTextView textView;
    ImageView emojiButton;
    ImageView submitButton;
    View rootView;
    EmojIconActions emojIcon;
    ArrayList<StickerData> stickerData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StickerData a = new StickerData();
        ArrayList<StickerItem> s = new ArrayList<>();

        a.setGroupId(0);
        a.setGroupName("test");
        a.setImageHeader("https://dev-static.rctiplus.id/media/50/files/fta_rcti/Icon/0_icon/placeholder_icon.jpg");
        a.setSticker(s);

        StickerItem c = new StickerItem();

        c.setId(0);
        c.setName("test");
        c.setUrl("https://dev-static.rctiplus.id/media/50/files/fta_rcti/Icon/0_icon/placeholder_icon.jpg");
        c.setId(1);
        c.setName("test");
        c.setUrl("https://dev-static.rctiplus.id/media/50/files/fta_rcti/Icon/0_icon/placeholder_icon.jpg");
        s.add(c);


        stickerData.add(a);


        rootView = findViewById(R.id.root_view);
        emojiButton = findViewById(R.id.emoji_btn);
        submitButton = findViewById(R.id.submit_btn);
        mCheckBox = findViewById(R.id.use_system_default);
        emojiconEditText = findViewById(R.id.emojicon_edit_text);
        emojiconEditText2 = findViewById(R.id.emojicon_edit_text2);
        textView = findViewById(R.id.textView);

        emojIcon = new EmojIconActions(this,
                rootView,
                emojiconEditText,
                emojiButton,
                stickerData,
                emojiButton, new EmojIconActions.StickerOnClickListener() {
            @Override
            public void onClick(String url, String type) {

                Log.i("TAG", "onClick: content clicked");
            }

            @Override
            public void onSearchClick() {
                SearchGifFragment dialog = new SearchGifFragment();
                dialog.show(getSupportFragmentManager(),
                        url -> Log.i("TAG", "onContentClicked: "));

            }
        }, getSupportFragmentManager());


        emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
                Log.e("Keyboard", "open");
            }

            @Override
            public void onKeyboardClose() {
                Log.e("Keyboard", "close");
            }
        });

        mCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
//            emojIcon.setUseSystemEmoji(b);
            textView.setUseSystemDefault(b);
        });
        emojIcon.addEmojiconEditTextList(emojiconEditText2);

        submitButton.setOnClickListener(v -> {
            Editable editableText = emojiconEditText.getText();
            if (editableText == null) {
                textView.setText("");
            } else {
                String newText = editableText.toString();
                textView.setText(newText);
            }
        });
    }
}
