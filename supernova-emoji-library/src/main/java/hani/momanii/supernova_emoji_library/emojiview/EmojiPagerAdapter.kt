package hani.momanii.supernova_emoji_library.emojiview

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import hani.momanii.supernova_emoji_library.helper.EmojiconGridView
import hani.momanii.supernova_emoji_library.helper.EmojiconRecentsGridView

/**
 * @author kasmadi
 * @date 07/06/21
 */
class EmojiPagerAdapter(private var gridView: List<EmojiconGridView>) : PagerAdapter() {

    override fun getCount(): Int = gridView.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return `object` == view
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = gridView[position].rootView
        container.addView(view, 0)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView((`object` as View))
    }

    fun getRecentFragment(): EmojiconRecentsGridView? {
        for (i in gridView) {
            if (i is EmojiconRecentsGridView) {
                return i
            }
        }
        return null
    }

    fun add(views: List<EmojiconGridView>) {
        this.gridView = views
    }
}