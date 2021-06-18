package hani.momanii.supernova_emoji_library.gif

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hani.momanii.supernova_emoji_library.helper.EmojiconsPopup
import hani.momanii.supernova_emoji_library.R
import retrofit2.Call
import retrofit2.Callback

class GifView(val context: Context, val popUp: EmojiconsPopup){
    var rootView: View? = null

    private lateinit var adapter: GiftAdapter
    private var recycleView: RecyclerView? = null
    private var data:MutableList<MediaItem> = mutableListOf()

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
        val layoutManager = GridLayoutManager(context,3,GridLayoutManager.VERTICAL, false)
        recycleView?.layoutManager = layoutManager
        recycleView?.adapter = adapter

        getTrending()

        return rootView
    }

    private fun getTrending() {
        data.clear()
        val client = Client()
        val call = client.makeService().trendingGift()
        call.enqueue(object : Callback<Response> {
            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                if (response.isSuccessful) {
                    val body = response.body()?.results
                    for (i in 0 until body?.size!!) {
                        body[i].media?.let { data.addAll(it) }
                    }
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<Response>, t: Throwable) {
                Log.e("TAG", "onFailure: ${t.message}", t)
            }

        })
    }
}