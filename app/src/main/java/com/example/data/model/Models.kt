package com.example.data.model

data class VideoItem(
    val id: String,
    val title: String,
    val channelTitle: String,
    val channelId: String,
    val channelAvatarUrl: String,
    val thumbnailUrl: String,
    val viewsCount: String,
    val publishedAt: String,
    val durationFormatted: String,
    val category: String,
    val description: String,
    val likesCount: String,
    val commentsCount: String,
    val isLive: Boolean = false,
    val videoUrl: String? = null // Direct video URL or YouTube ID
)

data class CommentItem(
    val id: String,
    val authorName: String,
    val authorAvatarUrl: String,
    val text: String,
    val publishedTime: String,
    val likesCount: String
)

data class ChannelItem(
    val id: String,
    val name: String,
    val avatarUrl: String,
    val subscribersCount: String,
    val isSubscribed: Boolean = false,
    val bannerUrl: String? = null
)

data class AdBlockStats(
    val totalAdsBlocked: Int = 142,
    val timeSavedMinutes: Int = 47,
    val trackersStopped: Int = 389,
    val sponsorsSkipped: Int = 68
)
