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
   String stickerData;

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

        stickerData =
            "{\"data\":[{\"group_id\":2,\"group_name\":\"Greetings\",\"image_header\":\"/files/fta_rcti/Icon/0_icon/placeholder_icon.jpg\",\"sorting\":1,\"stickers\":[{\"id\":1,\"name\":\"hello\",\"sorting\":1,\"sticker_group_id\":0,\"media_link\":\"/files/fta_rcti/Icon/0_icon/placeholder_icon.jpg\"},{\"id\":11,\"name\":\"kenapa\",\"sorting\":1,\"sticker_group_id\":0,\"media_link\":\"/files/fta_rcti/Landscape/master_chef_indonesia_s05e01.png\"},{\"id\":2,\"name\":\"good morning\",\"sorting\":2,\"sticker_group_id\":0,\"media_link\":\"/files/fta_rcti/Icon/0_icon/placeholder_icon.jpg\"},{\"id\":12,\"name\":\"Chef Arnold\",\"sorting\":2,\"sticker_group_id\":0,\"media_link\":\"/files/fta_rcti/Landscape/master_chef_indonesia_s05e03.png\"},{\"id\":8,\"name\":\"Smile\",\"sorting\":3,\"sticker_group_id\":0,\"media_link\":\"/files/fta_rcti/Landscape/seindah_senyum_wiona/seindahsenyumwinonaeps13.png\"},{\"id\":9,\"name\":\"confuse face\",\"sorting\":3,\"sticker_group_id\":0,\"media_link\":\"/files/fta_rcti/Landscape/25_juta_semalam/25_juta_semalam.png\"},{\"id\":6,\"name\":\"upin\",\"sorting\":5,\"sticker_group_id\":0,\"media_link\":\"/files/fta_rcti/Banner_Landscape/new_upin_ipin_l.jpg\"},{\"id\":10,\"name\":\"kaget\",\"sorting\":6,\"sticker_group_id\":0,\"media_link\":\"/files/fta_rcti/Landscape/ijinkan_aku_bahagia_sekali_saja/i_drama___ijinkan_aku_bahagia_sekali_saja.png\"},{\"id\":24,\"name\":\"peluk sayang\",\"sorting\":9,\"sticker_group_id\":0,\"media_link\":\"/files/fta_rcti/Landscape/kalau_cinta_jangan_cengeng_movies/kalau_cinta_jangan.jpg\"},{\"id\":26,\"name\":\"kiko marah\",\"sorting\":10,\"sticker_group_id\":0,\"media_link\":\"/files/fta_rcti/Landscape/pra_kiko/pra_kiko_s01e12_kangen_dimarahin.png\"}]},{\"group_id\":1,\"group_name\":\"Ramadhan\",\"image_header\":\"/files/fta_rcti/Icon/0_icon/placeholder_icon.jpg\",\"sorting\":2,\"stickers\":[{\"id\":19,\"name\":\"sholat jamaah\",\"sorting\":0,\"sticker_group_id\":0,\"media_link\":\"/files/fta_rcti/Landscape/seusai_sholat_ied/seusai_sholat_ied.png\"},{\"id\":3,\"name\":\"berbuka\",\"sorting\":1,\"sticker_group_id\":0,\"media_link\":\"/files/fta_rcti/Icon/0_icon/placeholder_icon.jpg\"},{\"id\":4,\"name\":\"sahur\",\"sorting\":2,\"sticker_group_id\":0,\"media_link\":\"/files/fta_rcti/Icon/0_icon/placeholder_icon.jpg\"},{\"id\":15,\"name\":\"mengaji\",\"sorting\":3,\"sticker_group_id\":0,\"media_link\":\"/files/fta_rcti/Landscape/kisah_pencinta_al_quran/kisahpecintaal_quraneps12.png\"},{\"id\":16,\"name\":\"jilbab\",\"sorting\":4,\"sticker_group_id\":0,\"media_link\":\"/files/fta_rcti/Icon/jilbab_in_love/jilbabinlove_icon.jpg\"},{\"id\":20,\"name\":\"doa\",\"sorting\":5,\"sticker_group_id\":0,\"media_link\":\"/files/fta_rcti/Landscape/panggil_namaku_sekali_saja_ibu/panggil_namaku_sekali_saja_ibu_s01e07.png\"},{\"id\":21,\"name\":\"ustad\",\"sorting\":5,\"sticker_group_id\":0,\"media_link\":\"/files/fta_rcti/Landscape/aku_bukan_ustad/aku_bukan_ustad_s01e06.png\"},{\"id\":22,\"name\":\"ngambek\",\"sorting\":7,\"sticker_group_id\":0,\"media_link\":\"/files/fta_rcti/Landscape/mengintip_surga/mengintipsurgaeps16.png\"},{\"id\":23,\"name\":\"karena aku cinta baginda nabi\",\"sorting\":9,\"sticker_group_id\":0,\"media_link\":\"/files/fta_rcti/Portrait/karena_aku_cinta_baginda_nabi/karena_aku_cinta_baginda_nabi_768x1152.jpg\"},{\"id\":25,\"name\":\"surat kecil untuk tuhan\",\"sorting\":10,\"sticker_group_id\":0,\"media_link\":\"/files/fta_rcti/Icon/surat_kecil_untuk/suratkeciluntuktuhan_icon.jpg\"}]}],\"meta\":{\"image_path\":\"https://dev-static.rctiplus.id/media/\",\"pagination\":{\"current_page\":1,\"per_page\":10,\"total\":10,\"total_page\":1},\"video_path\":\"\"},\"status\":{\"code\":0,\"message_client\":\"Success\",\"message_server\":\"Success\",\"status\":0}}";

        emojIcon = new EmojIconActions(this,
                rootView,
                emojiconEditText,
                emojiButton,
                emojiButton,
                stickerData,
                new EmojIconActions.StickerOnClickListener() {
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
        });


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
