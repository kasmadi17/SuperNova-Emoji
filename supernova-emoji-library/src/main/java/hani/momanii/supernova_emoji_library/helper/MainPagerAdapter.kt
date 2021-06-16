package hani.momanii.supernova_emoji_library.helper

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter

/**
 * @author kasmadi
 * @date 06/06/21
 */
class MainPagerAdapter(val rootView: Array<View>) : PagerAdapter() {
    override fun getCount(): Int = rootView.size
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return `object` == view
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val v = rootView[position]
        container.addView(v, 0)
        return v
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView((`object` as View))
    }

}