package hani.momanii.supernova_emoji_library.sticker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import hani.momanii.supernova_emoji_library.R
import hani.momanii.supernova_emoji_library.helper.EmojiconsPopup

private const val ARG_STICKER = "ARG_STICKER"

class StickerView(
    val context: Context,
    val stickerData: ArrayList<StickerData>?,
    val popUp: EmojiconsPopup
) {
    private lateinit var adapter: StickerAdapter
    private var rvListSticker: RecyclerView? = null
    private var tabHeader: TabLayout? = null
    private var data: ArrayList<StickerData>? = arrayListOf()
    var rootView: View? = null
    private var tvNotFound:TextView?=null

    init {
        onCreateView()
    }

    private fun onCreateView(): View {
        // Inflate the layout for this fragment
        rootView = LayoutInflater.from(context).inflate(R.layout.fragment_sticker, null, false)
        rvListSticker = rootView?.findViewById(R.id.rvListSticker)
        tabHeader = rootView?.findViewById(R.id.tabHeaderSticker)
        tvNotFound = rootView?.findViewById(R.id.tvNotFound)
        data = arrayListOf()
        data?.clear()
        data = stickerData
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL)
        if (data != null && data!!.isNotEmpty()) {
            setUpTabLayout()
            adapter = StickerAdapter {
                popUp.onStickerClick.onStickerClicked(it.url, "sticker")

            }
            rvListSticker?.layoutManager = layoutManager
            rvListSticker?.adapter = adapter
            adapter.setData(data?.get(0)?.sticker!!)
            tvNotFound?.visibility = View.GONE
        } else {
            tvNotFound?.visibility = View.VISIBLE
        }

        return rootView!!
    }

    private fun setUpTabLayout() {
        for (i in data!!) {
            tabHeader?.addTab(tabHeader!!.newTab())
        }

        for (i in 0 until tabHeader!!.tabCount) {
            setTabLayout(i, data?.get(i)?.imageHeader)
        }

        tabHeader?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                data?.get(tab?.position ?: 0)?.sticker?.let { adapter.setData(it) }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}

        })
    }

    private fun setTabLayout(position: Int, image: String?) {
        val view =
            LayoutInflater.from(rootView?.context).inflate(R.layout.custom_tab_image_only, null)
        val tabIcon = view.findViewById<ImageView>(R.id.imgIcon)
        Glide.with(context).load(image).override(64, 64).error(R.drawable.emoji_people)
            .into(tabIcon)
        tabHeader?.getTabAt(position)?.customView = view
    }

    fun setData(stickerData: ArrayList<StickerData>) {
        data = arrayListOf()
        data?.clear()
        data = stickerData
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL)
        if (data != null && data!!.isNotEmpty()) {
            setUpTabLayout()
            adapter = StickerAdapter {
                popUp.onStickerClick.onStickerClicked(it.url, "sticker")

            }
            rvListSticker?.layoutManager = layoutManager
            rvListSticker?.adapter = adapter
            adapter.setData(data?.get(0)?.sticker!!)
            tvNotFound?.visibility = View.GONE
        } else {
            tvNotFound?.visibility = View.VISIBLE
        }
    }

    interface OnStickerClick {
        /**
         * @param [url] url media
         * @param [type] sticker | gif
         */
        fun onStickerClicked(url: String?, type: String)
    }
}