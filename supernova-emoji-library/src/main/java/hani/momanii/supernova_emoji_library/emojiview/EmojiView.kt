package hani.momanii.supernova_emoji_library.emojiview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import hani.momanii.supernova_emoji_library.helper.*
import hani.momanii.supernova_emoji_library.R
import hani.momanii.supernova_emoji_library.emoji.*
import java.util.*

class EmojiView(
    val context: Context,
    val emojiPopup: EmojiconsPopup
) : ViewPager.OnPageChangeListener, EmojiconRecents {

    private lateinit var mEmojisAdapter: PagerAdapter
    private lateinit var mRecentsManager: EmojiconRecentsManager
    private var imageView: ArrayList<ImageView?> = arrayListOf()
    private var viewPager: ViewPager? = null
    private var emojiBackSpace: ImageView? = null

    private var mUseSystemDefault: Boolean = true
    private var mEmojiTabLastSelectedIndex: Int = -1

    var rootView: View? = null

    init {
        onCreateView()
    }


    private fun onCreateView() {
        rootView = LayoutInflater.from(context).inflate(R.layout.fragment_emoji, null, false)
        viewPager = rootView?.findViewById(R.id.emojis_pager)
        emojiBackSpace = rootView?.findViewById(R.id.emojis_backspace)
        imageView.add(0, rootView?.findViewById(R.id.emojis_tab_0_recents))
        imageView.add(1, rootView?.findViewById(R.id.emojis_tab_1_people))
        imageView.add(2, rootView?.findViewById(R.id.emojis_tab_2_nature))
        imageView.add(3, rootView?.findViewById(R.id.emojis_tab_3_food))
        imageView.add(4, rootView?.findViewById(R.id.emojis_tab_4_sport))
        imageView.add(5, rootView?.findViewById(R.id.emojis_tab_5_cars))
        imageView.add(6, rootView?.findViewById(R.id.emojis_tab_6_elec))
        imageView.add(7, rootView?.findViewById(R.id.emojis_tab_7_sym))

        setUpViewPager()

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

    override fun onPageSelected(position: Int) {
        if (mEmojiTabLastSelectedIndex == position) {
            return
        }
        when (position) {
            0, 1, 2, 3, 4, 5, 6, 7 -> {
                if (mEmojiTabLastSelectedIndex >= 0 && mEmojiTabLastSelectedIndex < imageView.size) {
                    imageView[mEmojiTabLastSelectedIndex]?.isSelected = false
                }

                imageView[position]?.isSelected = true
                mEmojiTabLastSelectedIndex = position
                mRecentsManager.recentPage = position
            }
        }
    }

    override fun onPageScrollStateChanged(state: Int) {}

    override fun addRecentEmoji(context: Context?, emojicon: Emojicon?) {
        val pagerAdapter = viewPager?.adapter
        if (pagerAdapter is EmojiPagerAdapter) {
            val fragment = (viewPager?.adapter as EmojiPagerAdapter).getRecentFragment()
            fragment?.addRecentEmoji(context, emojicon)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setUpViewPager() {
        viewPager?.clearOnPageChangeListeners()
        viewPager?.addOnPageChangeListener(this)
        val recent: EmojiconRecents = this

        mEmojisAdapter = EmojiPagerAdapter(
            listOf(
                EmojiconRecentsGridView(
                    context,
                    null,
                    null,
                    emojiPopup,
                    mUseSystemDefault
                ),
                EmojiconGridView(
                    context,
                    Nature.DATA,
                    recent,
                    emojiPopup,
                    mUseSystemDefault
                ),
                EmojiconGridView(
                    context,
                    Food.DATA,
                    recent,
                    emojiPopup,
                    mUseSystemDefault
                ),
                EmojiconGridView(
                    context,
                    Sport.DATA,
                    recent,
                    emojiPopup,
                    mUseSystemDefault
                ),
                EmojiconGridView(
                    context,
                    Cars.DATA,
                    recent,
                    emojiPopup,
                    mUseSystemDefault
                ),
                EmojiconGridView(
                    context,
                    Electr.DATA,
                    recent,
                    emojiPopup,
                    mUseSystemDefault
                ),
                EmojiconGridView(
                    context,
                    Symbols.DATA,
                    recent,
                    emojiPopup,
                    mUseSystemDefault
                )
            )
        )

        viewPager?.adapter = mEmojisAdapter

        for (i in 0 until imageView.size) {
            imageView[i]?.setOnClickListener {
                viewPager?.currentItem = i
            }
        }

//        viewPager.setBackgroundColor(backgroundColor);
//        tabs.setBackgroundColor(tabsColor);
        (emojiBackSpace as ImageButton ).setColorFilter(Color.parseColor("#8F8F8F"))
        for ( i in  imageView) {
            val btn = ( i as ImageButton)
            btn.setColorFilter(Color.parseColor("#8F8F8F"))
        }

        emojiBackSpace?.setOnTouchListener(RepeatListener(500, 50) {
            emojiPopup.onEmojiBackspaceClickedListener.onEmojiBackspaceClicked(it)
        })

        // get last selected page
        mRecentsManager = EmojiconRecentsManager.getInstance(context)
        var page = mRecentsManager.recentPage
        // last page was recents, check if there are recents to use
        // if none was found, go to page 1
        if (page == 0 && mRecentsManager.size == 0) {
            page = 1
        }

        if (page == 0) {
            onPageSelected(page)
        } else {
            viewPager?.setCurrentItem(page, false)
        }
    }
}