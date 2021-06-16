package hani.momanii.supernova_emoji_library.sticker

/**
 * @author kasmadi
 * @date 06/06/21
 */

data class StickerData(
    var groupId: Int = 0,

    var groupName: String? = null,

    var sorting: Int = 0,

    var imageHeader: String? = null,

    var sticker: List<StickerItem>? = null

)

data class StickerItem(
    var id: Int = 0,
    var name: String? = null,
    var stickerGroupId: Int = 0,
    var url: String? = null,
    var sorting: Int = 0
)