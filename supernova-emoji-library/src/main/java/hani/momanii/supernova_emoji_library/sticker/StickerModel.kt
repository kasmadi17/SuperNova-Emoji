package hani.momanii.supernova_emoji_library.sticker

import com.google.gson.annotations.SerializedName

/**
 * @author kasmadi
 * @date 06/06/21
 */

data class StickerModel(
    @SerializedName("data")
    val data: List<StickerData>? = null
) : BaseResponse()

data class StickerData(
    @SerializedName("group_id")
    val groupId: Int = 0,

    @SerializedName("group_name")
    val groupName: String? = null,

    @SerializedName("sorting")
    val sorting: Int = 0,

    @SerializedName("image_header")
    var imageHeader: String? = null,

    @SerializedName("stickers")
    val sticker: List<StickerItem>? = null

)

data class StickerItem(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("sticker_group_id")
    val stickerGroupId: Int = 0,
    @SerializedName("media_link")
    var url: String? = null,
    @SerializedName("sorting")
    val sorting: Int = 0

)

open class BaseResponse {
    @SerializedName("meta")
    var meta: Meta? = null
}

data class Meta(
    @SerializedName("image_path")
    var imagePath: String = ""
)