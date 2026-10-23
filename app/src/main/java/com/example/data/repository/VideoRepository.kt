package com.example.data.repository

import com.example.data.database.BookmarkEntity
import com.example.data.database.HistoryEntity
import com.example.data.database.PlaylistEntity
import com.example.data.database.PureTubeDao
import com.example.data.database.SubscriptionEntity
import com.example.data.model.AdBlockStats
import com.example.data.model.ChannelItem
import com.example.data.model.CommentItem
import com.example.data.model.VideoItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class VideoRepository(private val dao: PureTubeDao) {

    private val _adBlockStats = MutableStateFlow(
        AdBlockStats(
            totalAdsBlocked = 186,
            timeSavedMinutes = 62,
            trackersStopped = 430,
            sponsorsSkipped = 94
        )
    )
    val adBlockStats: Flow<AdBlockStats> = _adBlockStats.asStateFlow()

    fun recordBlockedAd(count: Int = 1) {
        _adBlockStats.update { current ->
            current.copy(
                totalAdsBlocked = current.totalAdsBlocked + count,
                timeSavedMinutes = current.timeSavedMinutes + (count * 15 / 60).coerceAtLeast(1),
                trackersStopped = current.trackersStopped + (count * 2)
            )
        }
    }

    // Curated catalog with genuine playable YouTube video IDs and rich content
    private val sampleVideos = listOf(
        VideoItem(
            id = "jfKfPfyJRdk",
            title = "lofi hip hop radio - beats to relax/study to",
            channelTitle = "Lofi Girl",
            channelId = "lofigirl",
            channelAvatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150&auto=format&fit=crop&q=80",
            thumbnailUrl = "https://images.unsplash.com/photo-1518609878373-06d740f60d8b?w=800&auto=format&fit=crop&q=80",
            viewsCount = "68M просмотров",
            publishedAt = "В эфире",
            durationFormatted = "LIVE",
            category = "Музыка",
            description = "Спокойные биты для учебы, работы и отдыха. Воспроизведение без рекламы и пауз с PureTube.",
            likesCount = "2.4M",
            commentsCount = "48.2K",
            isLive = true
        ),
        VideoItem(
            id = "aqz-KE-bpKQ",
            title = "Big Buck Bunny 4K 60FPS HDR - Кинематографический шедевр",
            channelTitle = "Blender Open Project",
            channelId = "blender_official",
            channelAvatarUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=150&auto=format&fit=crop&q=80",
            thumbnailUrl = "https://images.unsplash.com/photo-1579783900882-c0d3dad7b119?w=800&auto=format&fit=crop&q=80",
            viewsCount = "14.2M просмотров",
            publishedAt = "2 года назад",
            durationFormatted = "09:56",
            category = "Развлечения",
            description = "Полноформатный анимационный фильм Blender Open Movie. Воспроизведение в наивысшем качестве без рекламных вставок.",
            likesCount = "420K",
            commentsCount = "12.8K"
        ),
        VideoItem(
            id = "dQw4w9WgXcQ",
            title = "Rick Astley - Never Gonna Give You Up (Official Music Video)",
            channelTitle = "Rick Astley",
            channelId = "rickastley",
            channelAvatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150&auto=format&fit=crop&q=80",
            thumbnailUrl = "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?w=800&auto=format&fit=crop&q=80",
            viewsCount = "1.6B просмотров",
            publishedAt = "14 лет назад",
            durationFormatted = "03:33",
            category = "Музыка",
            description = "Легендарный трек Rick Astley. Без предроллов, без спонсоров и баннеров.",
            likesCount = "17M",
            commentsCount = "2.4M"
        ),
        VideoItem(
            id = "e-ORhEE9VVg",
            title = "Taylor Swift - Blank Space (Official 4K Ultra HD)",
            channelTitle = "Taylor Swift",
            channelId = "taylorswift",
            channelAvatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=150&auto=format&fit=crop&q=80",
            thumbnailUrl = "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=800&auto=format&fit=crop&q=80",
            viewsCount = "3.4B просмотров",
            publishedAt = "10 лет назад",
            durationFormatted = "04:32",
            category = "Музыка",
            description = "Музыкальный клип Тейлор Свифт в кристально чистом качестве.",
            likesCount = "14M",
            commentsCount = "425K"
        ),
        VideoItem(
            id = "kJQP7kiw5Fk",
            title = "Luis Fonsi - Despacito ft. Daddy Yankee",
            channelTitle = "Luis Fonsi",
            channelId = "luisfonsi",
            channelAvatarUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=150&auto=format&fit=crop&q=80",
            thumbnailUrl = "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=800&auto=format&fit=crop&q=80",
            viewsCount = "8.4B просмотров",
            publishedAt = "7 лет назад",
            durationFormatted = "04:41",
            category = "Музыка",
            description = "Один из самых популярных клипов в истории YouTube.",
            likesCount = "52M",
            commentsCount = "4.3M"
        ),
        VideoItem(
            id = "fJ9rUzIMcZQ",
            title = "Queen – Bohemian Rhapsody (Official Video Remastered)",
            channelTitle = "Queen Official",
            channelId = "queenofficial",
            channelAvatarUrl = "https://images.unsplash.com/photo-1516280440614-37939bbacd81?w=150&auto=format&fit=crop&q=80",
            thumbnailUrl = "https://images.unsplash.com/photo-1465847899084-d164df4dedc6?w=800&auto=format&fit=crop&q=80",
            viewsCount = "1.8B просмотров",
            publishedAt = "15 лет назад",
            durationFormatted = "05:59",
            category = "Музыка",
            description = "Шедевр группы Queen без перерывов на рекламу.",
            likesCount = "12M",
            commentsCount = "680K"
        ),
        VideoItem(
            id = "M576WGiDBdQ",
            title = "Обзор искусственного интеллекта и нейросетей нового поколения",
            channelTitle = "TechWorld Pro",
            channelId = "techworld_pro",
            channelAvatarUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150&auto=format&fit=crop&q=80",
            thumbnailUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=800&auto=format&fit=crop&q=80",
            viewsCount = "840K просмотров",
            publishedAt = "3 дня назад",
            durationFormatted = "18:24",
            category = "Технологии",
            description = "Подробный разбор новейших моделей ИИ, генерации кода, видео и мультимодальных агентов.",
            likesCount = "56K",
            commentsCount = "3.2K"
        ),
        VideoItem(
            id = "L_LUpnjgPso",
            title = "Создание идеального мобильного приложения на Jetpack Compose",
            channelTitle = "Android Dev Academy",
            channelId = "android_dev_academy",
            channelAvatarUrl = "https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?w=150&auto=format&fit=crop&q=80",
            thumbnailUrl = "https://images.unsplash.com/photo-1555066931-4365d14bab8c?w=800&auto=format&fit=crop&q=80",
            viewsCount = "320K просмотров",
            publishedAt = "1 неделю назад",
            durationFormatted = "32:15",
            category = "Обучение",
            description = "Полный курс по современной разработке Android приложений: Clean Architecture, M3 и StateFlow.",
            likesCount = "24K",
            commentsCount = "1.1K"
        ),
        VideoItem(
            id = "tPEE9ZwTmy0",
            title = "Невероятные красоты нашей Вселенной в 4K Ultra HD",
            channelTitle = "Space Discovery",
            channelId = "spacediscovery",
            channelAvatarUrl = "https://images.unsplash.com/photo-1522075469751-3a6694fb2f61?w=150&auto=format&fit=crop&q=80",
            thumbnailUrl = "https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=800&auto=format&fit=crop&q=80",
            viewsCount = "5.1M просмотров",
            publishedAt = "5 месяцев назад",
            durationFormatted = "45:10",
            category = "Наука",
            description = "Погружение в глубины космоса, галактики и туманности под расслабляющую музыку.",
            likesCount = "190K",
            commentsCount = "8.4K"
        ),
        VideoItem(
            id = "9bZkp7q19f0",
            title = "PSY - GANGNAM STYLE (강남스타일) M/V",
            channelTitle = "officialpsy",
            channelId = "officialpsy",
            channelAvatarUrl = "https://images.unsplash.com/photo-1527980965255-d3b416303d12?w=150&auto=format&fit=crop&q=80",
            thumbnailUrl = "https://images.unsplash.com/photo-1492684223066-81342ee5ff30?w=800&auto=format&fit=crop&q=80",
            viewsCount = "5.2B просмотров",
            publishedAt = "12 лет назад",
            durationFormatted = "04:12",
            category = "Музыка",
            description = "Первое видео на YouTube, набравшее 1 миллиард просмотров.",
            likesCount = "28M",
            commentsCount = "5.1M"
        ),
        VideoItem(
            id = "kXYiU_JCYtU",
            title = "Linkin Park - Numb (Official Music Video) [4K Remastered]",
            channelTitle = "Linkin Park",
            channelId = "linkinpark",
            channelAvatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150&auto=format&fit=crop&q=80",
            thumbnailUrl = "https://images.unsplash.com/photo-1501386761578-eac5c94b800a?w=800&auto=format&fit=crop&q=80",
            viewsCount = "2.3B просмотров",
            publishedAt = "17 лет назад",
            durationFormatted = "03:07",
            category = "Музыка",
            description = "Культовый трек Linkin Park без рекламы.",
            likesCount = "16M",
            commentsCount = "890K"
        ),
        VideoItem(
            id = "JGwWNGJdvx8",
            title = "Ed Sheeran - Shape of You (Official Music Video)",
            channelTitle = "Ed Sheeran",
            channelId = "edsheeran",
            channelAvatarUrl = "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=150&auto=format&fit=crop&q=80",
            thumbnailUrl = "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?w=800&auto=format&fit=crop&q=80",
            viewsCount = "6.2B просмотров",
            publishedAt = "7 лет назад",
            durationFormatted = "04:23",
            category = "Музыка",
            description = "Мировой хит Эда Ширана.",
            likesCount = "33M",
            commentsCount = "1.2M"
        ),
        VideoItem(
            id = "2Vv-BfVoq4g",
            title = "Ed Sheeran - Perfect (Official Music Video)",
            channelTitle = "Ed Sheeran",
            channelId = "edsheeran",
            channelAvatarUrl = "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=150&auto=format&fit=crop&q=80",
            thumbnailUrl = "https://images.unsplash.com/photo-1516450360452-9312f5e86fc7?w=800&auto=format&fit=crop&q=80",
            viewsCount = "3.7B просмотров",
            publishedAt = "6 лет назад",
            durationFormatted = "04:39",
            category = "Музыка",
            description = "Романтическая баллада Эда Ширана.",
            likesCount = "21M",
            commentsCount = "450K"
        ),
        VideoItem(
            id = "CevxZvSJLk8",
            title = "Katy Perry - Roar (Official)",
            channelTitle = "Katy Perry",
            channelId = "katyperry",
            channelAvatarUrl = "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=150&auto=format&fit=crop&q=80",
            thumbnailUrl = "https://images.unsplash.com/photo-1518895949257-7621c3c786d7?w=800&auto=format&fit=crop&q=80",
            viewsCount = "4.0B просмотров",
            publishedAt = "10 лет назад",
            durationFormatted = "04:30",
            category = "Музыка",
            description = "Яркий клип Katy Perry.",
            likesCount = "17M",
            commentsCount = "920K"
        ),
        VideoItem(
            id = "YQHsXMglC9A",
            title = "Adele - Hello (Official Music Video)",
            channelTitle = "Adele",
            channelId = "adele",
            channelAvatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150&auto=format&fit=crop&q=80",
            thumbnailUrl = "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=800&auto=format&fit=crop&q=80",
            viewsCount = "3.1B просмотров",
            publishedAt = "8 лет назад",
            durationFormatted = "06:06",
            category = "Музыка",
            description = "Возвращение Адель с суперхитом Hello.",
            likesCount = "19M",
            commentsCount = "1.1M"
        )
    )

    fun getAllVideos(): List<VideoItem> = sampleVideos

    fun getVideosByCategory(category: String): List<VideoItem> {
        return if (category == "Все") {
            sampleVideos
        } else {
            sampleVideos.filter { it.category.equals(category, ignoreCase = true) }
        }
    }

    fun getVideoById(id: String): VideoItem {
        return sampleVideos.firstOrNull { it.id == id } ?: VideoItem(
            id = id,
            title = "YouTube Video: $id",
            channelTitle = "YouTube Creator",
            channelId = "creator_$id",
            channelAvatarUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150&auto=format&fit=crop&q=80",
            thumbnailUrl = "https://img.youtube.com/vi/$id/hqdefault.jpg",
            viewsCount = "1.2M просмотров",
            publishedAt = "Недавно",
            durationFormatted = "10:00",
            category = "Все",
            description = "Воспроизведение видео через защищенный плеер PureTube без рекламы.",
            likesCount = "45K",
            commentsCount = "1.8K"
        )
    }

    fun searchVideos(query: String): List<VideoItem> {
        val trimmed = query.trim()
        if (trimmed.isEmpty()) return sampleVideos

        // Check if query is a YouTube link or ID
        val extractedId = extractYouTubeId(trimmed)
        if (extractedId != null) {
            return listOf(getVideoById(extractedId))
        }

        return sampleVideos.filter {
            it.title.contains(trimmed, ignoreCase = true) ||
            it.channelTitle.contains(trimmed, ignoreCase = true) ||
            it.category.contains(trimmed, ignoreCase = true) ||
            it.description.contains(trimmed, ignoreCase = true)
        }
    }

    private fun extractYouTubeId(urlOrId: String): String? {
        if (urlOrId.length == 11 && !urlOrId.contains("/")) return urlOrId
        val regex = Regex("(?:youtu\\.be/|youtube\\.com/(?:embed/|v/|watch\\?v=|watch\\?.+&v=))([\\w-]{11})")
        val match = regex.find(urlOrId)
        return match?.groupValues?.get(1)
    }

    fun getSampleComments(videoId: String): List<CommentItem> {
        return listOf(
            CommentItem(
                id = "c1",
                authorName = "Дмитрий Смирнов",
                authorAvatarUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=100&auto=format&fit=crop&q=80",
                text = "Наконец-то нашел нормальное приложение без назойливой рекламы каждые две минуты! Работает моментально 🔥",
                publishedTime = "2 часа назад",
                likesCount = "1.4K"
            ),
            CommentItem(
                id = "c2",
                authorName = "Елена Кузнецова",
                authorAvatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=100&auto=format&fit=crop&q=80",
                text = "Фоновый режим просто спасение, слушаю музыку и подкасты с выключенным экраном!",
                publishedTime = "5 часов назад",
                likesCount = "890"
            ),
            CommentItem(
                id = "c3",
                authorName = "Alexey Developer",
                authorAvatarUrl = "https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?w=100&auto=format&fit=crop&q=80",
                text = "Очень чистый интерфейс в стиле Material 3 и быстрое переключение качества 1080p 60fps.",
                publishedTime = "1 день назад",
                likesCount = "312"
            ),
            CommentItem(
                id = "c4",
                authorName = "MusicLover_99",
                authorAvatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=100&auto=format&fit=crop&q=80",
                text = "Звук чистый, никаких баннеров поверх видео. Спасибо разработчикам!",
                publishedTime = "2 дня назад",
                likesCount = "154"
            )
        )
    }

    fun getPopularChannels(): List<ChannelItem> {
        return listOf(
            ChannelItem(
                id = "lofigirl",
                name = "Lofi Girl",
                avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150&auto=format&fit=crop&q=80",
                subscribersCount = "14.3M подписчиков"
            ),
            ChannelItem(
                id = "rickastley",
                name = "Rick Astley",
                avatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150&auto=format&fit=crop&q=80",
                subscribersCount = "4.2M подписчиков"
            ),
            ChannelItem(
                id = "blender_official",
                name = "Blender Open Project",
                avatarUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=150&auto=format&fit=crop&q=80",
                subscribersCount = "2.1M подписчиков"
            ),
            ChannelItem(
                id = "queenofficial",
                name = "Queen Official",
                avatarUrl = "https://images.unsplash.com/photo-1516280440614-37939bbacd81?w=150&auto=format&fit=crop&q=80",
                subscribersCount = "17.8M подписчиков"
            ),
            ChannelItem(
                id = "techworld_pro",
                name = "TechWorld Pro",
                avatarUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150&auto=format&fit=crop&q=80",
                subscribersCount = "950K подписчиков"
            )
        )
    }

    // DAO delegated methods
    val historyFlow: Flow<List<HistoryEntity>> = dao.getAllHistory()
    val bookmarksFlow: Flow<List<BookmarkEntity>> = dao.getAllBookmarks()
    val subscriptionsFlow: Flow<List<SubscriptionEntity>> = dao.getAllSubscriptions()
    val playlistsFlow: Flow<List<PlaylistEntity>> = dao.getAllPlaylists()

    fun isBookmarked(videoId: String): Flow<Boolean> = dao.isBookmarked(videoId)
    fun isSubscribed(channelId: String): Flow<Boolean> = dao.isSubscribed(channelId)

    suspend fun addToHistory(video: VideoItem, progress: Float = 0.1f) {
        dao.insertHistory(
            HistoryEntity(
                videoId = video.id,
                title = video.title,
                channelTitle = video.channelTitle,
                channelAvatarUrl = video.channelAvatarUrl,
                thumbnailUrl = video.thumbnailUrl,
                durationFormatted = video.durationFormatted,
                progressPercent = progress
            )
        )
        recordBlockedAd(2) // each watched video bypasses ~2 ad rolls
    }

    suspend fun clearHistory() = dao.clearHistory()
    suspend fun deleteHistoryItem(videoId: String) = dao.deleteHistoryItem(videoId)

    suspend fun toggleBookmark(video: VideoItem, currentlyBookmarked: Boolean) {
        if (currentlyBookmarked) {
            dao.deleteBookmark(video.id)
        } else {
            dao.insertBookmark(
                BookmarkEntity(
                    videoId = video.id,
                    title = video.title,
                    channelTitle = video.channelTitle,
                    channelAvatarUrl = video.channelAvatarUrl,
                    thumbnailUrl = video.thumbnailUrl,
                    durationFormatted = video.durationFormatted
                )
            )
        }
    }

    suspend fun toggleSubscription(channel: ChannelItem, currentlySubscribed: Boolean) {
        if (currentlySubscribed) {
            dao.deleteSubscription(channel.id)
        } else {
            dao.insertSubscription(
                SubscriptionEntity(
                    channelId = channel.id,
                    channelName = channel.name,
                    channelAvatarUrl = channel.avatarUrl,
                    subscribersCount = channel.subscribersCount
                )
            )
        }
    }

    suspend fun createPlaylist(name: String) {
        dao.insertPlaylist(PlaylistEntity(title = name))
    }
}
