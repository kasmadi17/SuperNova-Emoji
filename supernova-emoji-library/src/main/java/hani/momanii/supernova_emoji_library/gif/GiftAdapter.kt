package hani.momanii.supernova_emoji_library.gif

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import hani.momanii.supernova_emoji_library.R

/**
 * @author kasmadi
 * @date 15/06/21
 */
class GiftAdapter(private val data: List<MediaItem>, val listener:(MediaItem) -> Unit) :
    RecyclerView.Adapter<GiftAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GiftAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_gift, parent, false)
        )
    }

    override fun onBindViewHolder(holder: GiftAdapter.ViewHolder, position: Int) {
        holder.bind(data[position], listener)
    }

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val image: ImageView = itemView.findViewById(R.id.imgGift)
        fun bind(url: MediaItem, listener: (MediaItem) -> Unit) {
            Glide.with(itemView)
                .asGif()
                .load(url.tinygif?.url)
                .override(354, 200)
                .centerCrop().placeholder(R.drawable.emoji_people)
                .into(image)
            image.setOnClickListener { listener(url) }
        }
    }

}

class GifSearchAdapter(private val data: List<MediaItem>, val listener:(MediaItem) -> Unit) :
    RecyclerView.Adapter<GifSearchAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifSearchAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_gift, parent, false)
        )
    }

    override fun onBindViewHolder(holder: GifSearchAdapter.ViewHolder, position: Int) {
        holder.bind(data[position], listener)
    }

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val image: ImageView = itemView.findViewById(R.id.imgGift)
        fun bind(url: MediaItem, listener: (MediaItem) -> Unit) {
            Glide.with(itemView)
                .asGif()
                .load(url.tinygif?.url)
                .override(354, 200)
                .centerCrop().placeholder(R.drawable.emoji_people)
                .into(image)
            image.setOnClickListener { listener(url) }
        }
    }


}