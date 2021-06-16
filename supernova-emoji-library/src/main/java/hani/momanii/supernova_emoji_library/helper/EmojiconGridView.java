/*
 * Copyright 2014 Hieu Rocker
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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import hani.momanii.supernova_emoji_library.R;
import hani.momanii.supernova_emoji_library.emoji.Emojicon;
import hani.momanii.supernova_emoji_library.emoji.People;

/**
 * @author Hieu Rocker (rockerhieu@gmail.com)
 * @author Ankush Sachdeva (sankush@yahoo.co.in)
 * @author Hani Al Momani (hani.momanii@gmail.com)
 * @author Damai Subimawanto (damai.subimawanto@gmail.com)
 */
public class EmojiconGridView{
    public View rootView;
    EmojiconsPopup mEmojiconPopup;
    EmojiconRecents mRecents;
    Emojicon[] mData;

    public EmojiconGridView(Context context, Emojicon[] emojicons, EmojiconRecents recents, EmojiconsPopup emojiconPopup, boolean useSystemDefault) {
        mEmojiconPopup = emojiconPopup;
        rootView = LayoutInflater.from(context).inflate(R.layout.emojicon_grid, null);
        setRecents(recents);
        GridView gridView = rootView.findViewById(R.id.Emoji_GridView);
        if (emojicons== null) {
            mData = People.DATA;
        } else {
            mData = emojicons.clone();
        }
        EmojiAdapter mAdapter = new EmojiAdapter(rootView.getContext(), mData ,useSystemDefault);
        mAdapter.setEmojiClickListener(emojicon -> {
            if (mEmojiconPopup.onEmojiconClickedListener != null) {
                mEmojiconPopup.onEmojiconClickedListener.onEmojiconClicked(emojicon);
            }
            if (mRecents != null) {
                mRecents.addRecentEmoji(rootView.getContext(), emojicon);
            }
        });
        gridView.setAdapter(mAdapter);
    }

    private void setRecents(EmojiconRecents recents) {
        mRecents = recents;
    }

    public interface OnEmojiconClickedListener {
        void onEmojiconClicked(Emojicon emojicon);
    }
}