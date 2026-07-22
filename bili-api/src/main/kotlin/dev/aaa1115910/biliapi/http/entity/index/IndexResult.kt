package dev.aaa1115910.biliapi.http.entity.index

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IndexResultData(
    @SerialName("has_next")
    val hasNext: Int = 0,
    val list: List<IndexResultItem> = emptyList(),
    val num: Int = 0,
    val size: Int = 0,
    val total: Int = 0
) {
    @Serializable
    data class IndexResultItem(
        val badge: String? = null,
        @SerialName("badge_info")
        val badgeInfo: BadgeInfo? = null,
        @SerialName("badge_type")
        val badgeType: Int = 0,
        val cover: String = "",
        @SerialName("first_ep")
        val firstEp: FirstEp? = null,
        @SerialName("index_show")
        val indexShow: String? = null,
        @SerialName("is_finish")
        val isFinish: Int = 0,
        val link: String? = null,
        @SerialName("media_id")
        val mediaId: Int = 0,
        val order: String? = null,
        @SerialName("order_type")
        val orderType: String? = null,
        val score: String? = null,
        @SerialName("season_id")
        val seasonId: Int = 0,
        @SerialName("season_status")
        val seasonStatus: Int = 0,
        @SerialName("season_type")
        val seasonType: Int = 0,
        val subTitle: String? = null,
        val title: String = "",
        @SerialName("title_icon")
        val titleIcon: String? = null
    ) {
        @Serializable
        data class BadgeInfo(
            @SerialName("bg_color")
            val bgColor: String? = null,
            @SerialName("bg_color_night")
            val bgColorNight: String? = null,
            val text: String? = null
        )

        @Serializable
        data class FirstEp(
            val cover: String? = null,
            @SerialName("ep_id")
            val epId: Int = 0
        )
    }
}