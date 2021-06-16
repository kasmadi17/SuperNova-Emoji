package hani.momanii.supernova_emoji_library.gif

import com.google.gson.annotations.SerializedName

data class Response(

    @field:SerializedName("results")
    val results: List<ResultsItem>? = null
)


data class NanoGift(

    @field:SerializedName("url")
    val url: String? = null,
)

data class ResultsItem(
    @field:SerializedName("media")
    val media: List<MediaItem>? = null,
)


data class MediaItem(

    @field:SerializedName("nanogif")
    val tinygif: NanoGift? = null
)
