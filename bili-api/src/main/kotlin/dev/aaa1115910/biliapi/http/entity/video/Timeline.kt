@file:UseSerializers(LenientBooleanSerializer::class)

package dev.aaa1115910.biliapi.http.entity.video

import dev.aaa1115910.biliapi.http.util.LenientBooleanSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

/**
 * 放送时间表数据（App 端）
 */
@Serializable
data class TimelineAppData(
    @SerialName("current_time_text")
    val currentTimeText: String = "",
    val data: List<Timeline> = emptyList(),
    val filter: List<TimelineFilter> = emptyList(),
    @SerialName("is_night_mode")
    val isNightMode: Int = 0,
    @SerialName("navigation_title")
    val navigationTitle: String = ""
)

/**
 * 放送时间表
 */
@Serializable
data class Timeline(
    val date: String = "",
    @SerialName("date_ts")
    val dateTs: Long = 0L,
    @SerialName("day_of_week")
    val dayOfWeek: Int = 1,
    @SerialName("day_update_text")
    val dayUpdateText: String? = null,
    val episodes: List<Episode> = emptyList(),
    @SerialName("is_today")
    val isToday: Boolean = false
) {
    /**
     * 时间表剧集信息
     */
    @Serializable
    data class Episode(
        val cover: String = "",
        val delay: Int = 0,
        @SerialName("delay_id")
        val delayId: Int? = null,
        @SerialName("delay_index")
        val delayIndex: String? = null,
        @SerialName("delay_reason")
        val delayReason: String? = null,
        @SerialName("enable_vt")
        val enableVt: Boolean = false,
        @SerialName("ep_cover")
        val epCover: String? = null,
        @SerialName("episode_id")
        val episodeId: Int = 0,
        val follows: String? = null,
        val follow: Boolean = false,
        val plays: String? = null,
        @SerialName("pub_index")
        val pubIndex: String = "",
        @SerialName("pub_index_show")
        val pubIndexShow: String? = null,
        @SerialName("pub_time")
        val pubTime: String = "",
        @SerialName("pub_ts")
        val pubTs: Long = 0L,
        val published: Boolean = false,
        val report: Report? = null,
        @SerialName("season_id")
        val seasonId: Int = 0,
        @SerialName("season_type")
        val seasonType: Int? = null,
        @SerialName("square_cover")
        val squareCover: String? = null,
        val tags: List<Tag> = emptyList(),
        val title: String = "",
        val url: String? = null
    ) {
        @Serializable
        data class Report(
            val daynumber: Int = 0,
            @SerialName("ep_id")
            val epId: String = "",
            @SerialName("is_new")
            val isNew: String = "",
            @SerialName("is_published")
            val isPublished: String = "",
            @SerialName("season_id")
            val seasonId: Int = 0
        )

        @Serializable
        data class Tag(
            val text: String = "",
            val type: Int = 0
        )
    }
}

/**
 * 时间表筛选条件
 */
@Serializable
data class TimelineFilter(
    val desc: String = "",
    val type: Int = 0
)