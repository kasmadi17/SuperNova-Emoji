package momanii.hani.supernova_emoji;

import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;

public class MainActivity extends AppCompatActivity {
    CheckBox mCheckBox;
    EmojiconEditText emojiconEditText, emojiconEditText2;
    EmojiconTextView textView;
    ImageView emojiButton;
    ImageView submitButton;
    View rootView;
    EmojIconActions emojIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rootView = findViewById(R.id.root_view);
        emojiButton = findViewById(R.id.emoji_btn);
        submitButton = findViewById(R.id.submit_btn);
        mCheckBox = findViewById(R.id.use_system_default);
        emojiconEditText = findViewById(R.id.emojicon_edit_text);
        emojiconEditText2 = findViewById(R.id.emojicon_edit_text2);
        textView = findViewById(R.id.textView);
        emojIcon = new EmojIconActions(this, rootView, emojiconEditText, emojiButton);
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
            emojIcon.setUseSystemEmoji(b);
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
