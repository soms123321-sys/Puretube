package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.BookmarkEntity
import com.example.data.database.HistoryEntity
import com.example.data.database.PlaylistEntity
import com.example.data.database.PureTubeDatabase
import com.example.data.database.SubscriptionEntity
import com.example.data.model.AdBlockStats
import com.example.data.model.ChannelItem
import com.example.data.model.CommentItem
import com.example.data.model.VideoItem
import com.example.data.repository.VideoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class NavigationTab {
    HOME,
    TRENDING,
    SUBSCRIPTIONS,
    LIBRARY,
    SEARCH
}

class PureTubeViewModel(application: Application) : AndroidViewModel(application) {

    private val database = PureTubeDatabase.getDatabase(application)
    private val repository = VideoRepository(database.pureTubeDao())

    val categories = listOf("Все", "Музыка", "Игры", "Технологии", "Наука", "Обучение", "Развлечения")

    private val _currentTab = MutableStateFlow(NavigationTab.HOME)
    val currentTab: StateFlow<NavigationTab> = _currentTab.asStateFlow()

    private val _selectedCategory = MutableStateFlow("Все")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _activeVideo = MutableStateFlow<VideoItem?>(null)
    val activeVideo: StateFlow<VideoItem?> = _activeVideo.asStateFlow()

    private val _isPlayerExpanded = MutableStateFlow(false)
    val isPlayerExpanded: StateFlow<Boolean> = _isPlayerExpanded.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val adBlockStats: StateFlow<AdBlockStats> = repository.adBlockStats
        .stateIn(viewModelScope, SharingStarted.Lazily, AdBlockStats())

    val history: StateFlow<List<HistoryEntity>> = repository.historyFlow
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val bookmarks: StateFlow<List<BookmarkEntity>> = repository.bookmarksFlow
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val subscriptions: StateFlow<List<SubscriptionEntity>> = repository.subscriptionsFlow
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val playlists: StateFlow<List<PlaylistEntity>> = repository.playlistsFlow
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val suggestedChannels: List<ChannelItem> = repository.getPopularChannels()

    val allVideos: List<VideoItem> = repository.getAllVideos()

    // Filtered feed for Home screen
    val homeVideos: StateFlow<List<VideoItem>> = _selectedCategory.combine(_selectedCategory) { category, _ ->
        repository.getVideosByCategory(category)
    }.stateIn(viewModelScope, SharingStarted.Lazily, repository.getAllVideos())

    // Search results flow
    val searchResults: StateFlow<List<VideoItem>> = _searchQuery.combine(_searchQuery) { query, _ ->
        repository.searchVideos(query)
    }.stateIn(viewModelScope, SharingStarted.Lazily, repository.getAllVideos())

    fun setTab(tab: NavigationTab) {
        _currentTab.value = tab
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
    }

    fun selectVideo(video: VideoItem) {
        _activeVideo.value = video
        _isPlayerExpanded.value = true
        _isPlaying.value = true
        viewModelScope.launch {
            repository.addToHistory(video)
        }
    }

    fun minimizePlayer() {
        _isPlayerExpanded.value = false
    }

    fun expandPlayer() {
        if (_activeVideo.value != null) {
            _isPlayerExpanded.value = true
        }
    }

    fun closePlayer() {
        _isPlayerExpanded.value = false
        _isPlaying.value = false
        _activeVideo.value = null
    }

    fun setPlaying(playing: Boolean) {
        _isPlaying.value = playing
    }

    fun togglePlay() {
        _isPlaying.value = !_isPlaying.value
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun toggleBookmark(video: VideoItem) {
        viewModelScope.launch {
            val isCurrentlyBookmarked = bookmarks.value.any { it.videoId == video.id }
            repository.toggleBookmark(video, isCurrentlyBookmarked)
        }
    }

    fun toggleSubscription(channel: ChannelItem) {
        viewModelScope.launch {
            val isCurrentlySubscribed = subscriptions.value.any { it.channelId == channel.id }
            repository.toggleSubscription(channel, isCurrentlySubscribed)
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            repository.clearHistory()
        }
    }

    fun createPlaylist(name: String) {
        viewModelScope.launch {
            repository.createPlaylist(name)
        }
    }

    fun getCommentsForActiveVideo(videoId: String): List<CommentItem> {
        return repository.getSampleComments(videoId)
    }

    fun isVideoBookmarked(videoId: String): Boolean {
        return bookmarks.value.any { it.videoId == videoId }
    }

    fun isChannelSubscribed(channelId: String): Boolean {
        return subscriptions.value.any { it.channelId == channelId }
    }

    fun getVideoById(videoId: String): VideoItem {
        return repository.getVideoById(videoId)
    }
}
