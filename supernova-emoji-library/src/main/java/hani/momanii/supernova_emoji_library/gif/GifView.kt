package hani.momanii.supernova_emoji_library.gif

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hani.momanii.supernova_emoji_library.helper.EmojiconsPopup
import hani.momanii.supernova_emoji_library.R

class GifView(val context: Context, val popUp: EmojiconsPopup):GiftService.Callback {
    var rootView: View? = null

    private lateinit var adapter: GiftAdapter
    private var recycleView: RecyclerView? = null
    private var data:MutableList<MediaItem> = mutableListOf()
    private lateinit var giftService: GiftService

    init {
        onCreateView()
    }

    private fun onCreateView(): View? {
        // Inflate the layout for this fragment
        rootView = LayoutInflater.from(context).inflate(R.layout.fragment_gift, null, false)
        recycleView = rootView?.findViewById(R.id.rvGif)
        adapter = GiftAdapter(data) {
            popUp.onStickerClick.onStickerClicked(it.tinygif?.url, "gif")
        }
        giftService = GiftService("https://g.tenor.com/v1/trending?key=SGLHRLYSEO8R&limit=16&locale=id_ID",this)
        giftService.getData
        val layoutManager = GridLayoutManager(context,3,GridLayoutManager.VERTICAL, false)
        recycleView?.layoutManager = layoutManager
        recycleView?.adapter = adapter

        return rootView
    }

    override fun onSuccess(data: List<ResultsItem>?){

        for (i in data!!.indices){
            data[i].media?.let { this.data.addAll(it) }
        }

        adapter.notifyDataSetChanged()
    }
}