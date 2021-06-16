package hani.momanii.supernova_emoji_library.sticker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hani.momanii.supernova_emoji_library.R

/**
 * @author kasmadi
 * @date 02/06/21
 */
class StickerAdapter(val listener: (StickerItem) -> Unit) :
    RecyclerView.Adapter<StickerAdapter.StickerViewHolder>() {

    private var data: List<StickerItem> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StickerViewHolder {
        return StickerViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_sticker_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: StickerViewHolder, position: Int) {
        holder.bind(data[position], listener)
    }

    override fun getItemCount(): Int = data.size

    fun setData(data: List<StickerItem>) {
        this.data = data
        notifyDataSetChanged()
    }

    inner class StickerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imgSticker: ImageView = itemView.findViewById(R.id.imgSticker)
        fun bind(data: StickerItem, listener: (StickerItem) -> Unit) {
            Picasso.get().load(data.url).error(R.drawable.emoji_people).into(imgSticker)
            imgSticker.setOnClickListener {listener(data) }
        }
    }
}