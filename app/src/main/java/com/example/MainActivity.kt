package com.example

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.Subscriptions
import androidx.compose.material.icons.outlined.VideoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.player.MiniPlayerBar
import com.example.ui.NavigationTab
import com.example.ui.PureTubeViewModel
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LibraryScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.screens.SubscriptionsScreen
import com.example.ui.screens.TrendingScreen
import com.example.ui.screens.VideoDetailView
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.PureTubeDarkBg
import com.example.ui.theme.PureTubeRedBright
import com.example.ui.theme.PureTubeSurface

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                PureTubeApp()
            }
        }
    }
}

@Composable
fun PureTubeApp(
    viewModel: PureTubeViewModel = viewModel()
) {
    val context = LocalContext.current
    val currentTab by viewModel.currentTab.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val homeVideos by viewModel.homeVideos.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val activeVideo by viewModel.activeVideo.collectAsState()
    val isPlayerExpanded by viewModel.isPlayerExpanded.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()
    val adBlockStats by viewModel.adBlockStats.collectAsState()
    val history by viewModel.history.collectAsState()
    val bookmarks by viewModel.bookmarks.collectAsState()
    val subscriptions by viewModel.subscriptions.collectAsState()
    val playlists by viewModel.playlists.collectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing),
        containerColor = PureTubeDarkBg,
        bottomBar = {
            if (!isPlayerExpanded) {
                NavigationBar(
                    containerColor = PureTubeSurface,
                    contentColor = Color.White,
                    modifier = Modifier
                        .background(PureTubeSurface)
                        .testTag("bottom_nav_bar")
                ) {
                    NavigationBarItem(
                        selected = currentTab == NavigationTab.HOME,
                        onClick = { viewModel.setTab(NavigationTab.HOME) },
                        icon = {
                            Icon(
                                imageVector = if (currentTab == NavigationTab.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                                contentDescription = "Главная"
                            )
                        },
                        label = { Text("Главная", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = PureTubeRedBright,
                            selectedTextColor = PureTubeRedBright,
                            unselectedIconColor = Color.LightGray,
                            unselectedTextColor = Color.LightGray,
                            indicatorColor = Color.Transparent
                        ),
                        modifier = Modifier.testTag("nav_home_button")
                    )

                    NavigationBarItem(
                        selected = currentTab == NavigationTab.TRENDING,
                        onClick = { viewModel.setTab(NavigationTab.TRENDING) },
                        icon = {
                            Icon(
                                imageVector = if (currentTab == NavigationTab.TRENDING) Icons.Filled.LocalFireDepartment else Icons.Outlined.LocalFireDepartment,
                                contentDescription = "В тренде"
                            )
                        },
                        label = { Text("В тренде", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = PureTubeRedBright,
                            selectedTextColor = PureTubeRedBright,
                            unselectedIconColor = Color.LightGray,
                            unselectedTextColor = Color.LightGray,
                            indicatorColor = Color.Transparent
                        ),
                        modifier = Modifier.testTag("nav_trending_button")
                    )

                    NavigationBarItem(
                        selected = currentTab == NavigationTab.SUBSCRIPTIONS,
                        onClick = { viewModel.setTab(NavigationTab.SUBSCRIPTIONS) },
                        icon = {
                            Icon(
                                imageVector = if (currentTab == NavigationTab.SUBSCRIPTIONS) Icons.Filled.Subscriptions else Icons.Outlined.Subscriptions,
                                contentDescription = "Подписки"
                            )
                        },
                        label = { Text("Подписки", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = PureTubeRedBright,
                            selectedTextColor = PureTubeRedBright,
                            unselectedIconColor = Color.LightGray,
                            unselectedTextColor = Color.LightGray,
                            indicatorColor = Color.Transparent
                        ),
                        modifier = Modifier.testTag("nav_subscriptions_button")
                    )

                    NavigationBarItem(
                        selected = currentTab == NavigationTab.LIBRARY,
                        onClick = { viewModel.setTab(NavigationTab.LIBRARY) },
                        icon = {
                            Icon(
                                imageVector = if (currentTab == NavigationTab.LIBRARY) Icons.Filled.VideoLibrary else Icons.Outlined.VideoLibrary,
                                contentDescription = "Библиотека"
                            )
                        },
                        label = { Text("Библиотека", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = PureTubeRedBright,
                            selectedTextColor = PureTubeRedBright,
                            unselectedIconColor = Color.LightGray,
                            unselectedTextColor = Color.LightGray,
                            indicatorColor = Color.Transparent
                        ),
                        modifier = Modifier.testTag("nav_library_button")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Main Tab Content
            when (currentTab) {
                NavigationTab.HOME -> {
                    HomeScreen(
                        videos = homeVideos,
                        categories = viewModel.categories,
                        selectedCategory = selectedCategory,
                        onCategorySelected = { viewModel.selectCategory(it) },
                        adBlockStats = adBlockStats,
                        onVideoClick = { viewModel.selectVideo(it) },
                        onSaveToWatchLater = { viewModel.toggleBookmark(it) },
                        onShareVideo = { video ->
                            val intent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, "Смотри видео без рекламы в PureTube: https://youtu.be/${video.id}")
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(intent, "Поделиться"))
                        },
                        onSearchClick = { viewModel.setTab(NavigationTab.SEARCH) }
                    )
                }

                NavigationTab.TRENDING -> {
                    TrendingScreen(
                        videos = viewModel.allVideos,
                        onVideoClick = { viewModel.selectVideo(it) }
                    )
                }

                NavigationTab.SUBSCRIPTIONS -> {
                    SubscriptionsScreen(
                        subscriptions = subscriptions,
                        suggestedChannels = viewModel.suggestedChannels,
                        subscriptionVideos = viewModel.allVideos.shuffled(),
                        onToggleSubscribeChannel = { viewModel.toggleSubscription(it) },
                        onVideoClick = { viewModel.selectVideo(it) },
                        onSaveToWatchLater = { viewModel.toggleBookmark(it) },
                        onShareVideo = { video ->
                            val intent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, "Смотри видео без рекламы в PureTube: https://youtu.be/${video.id}")
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(intent, "Поделиться"))
                        }
                    )
                }

                NavigationTab.LIBRARY -> {
                    LibraryScreen(
                        adBlockStats = adBlockStats,
                        history = history,
                        bookmarks = bookmarks,
                        playlists = playlists,
                        onClearHistory = { viewModel.clearHistory() },
                        onCreatePlaylist = { viewModel.createPlaylist(it) },
                        onSelectHistoryItem = { hist ->
                            viewModel.selectVideo(viewModel.getVideoById(hist.videoId))
                        },
                        onSelectBookmarkItem = { bm ->
                            viewModel.selectVideo(viewModel.getVideoById(bm.videoId))
                        }
                    )
                }

                NavigationTab.SEARCH -> {
                    SearchScreen(
                        searchQuery = searchQuery,
                        onSearchQueryChange = { viewModel.onSearchQueryChange(it) },
                        searchResults = searchResults,
                        onVideoClick = { viewModel.selectVideo(it) },
                        onSaveToWatchLater = { viewModel.toggleBookmark(it) },
                        onShareVideo = { video ->
                            val intent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, "Смотри видео без рекламы в PureTube: https://youtu.be/${video.id}")
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(intent, "Поделиться"))
                        },
                        onBack = { viewModel.setTab(NavigationTab.HOME) }
                    )
                }
            }

            // Floating Mini-Player bar above bottom navigation if player is minimized
            val currentActive = activeVideo
            if (!isPlayerExpanded && currentActive != null) {
                MiniPlayerBar(
                    video = currentActive,
                    isPlaying = isPlaying,
                    onTogglePlay = { viewModel.togglePlay() },
                    onExpand = { viewModel.expandPlayer() },
                    onClose = { viewModel.closePlayer() },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .testTag("mini_player_bar")
                )
            }

            // Full Expanded Player Screen Overlay
            AnimatedVisibility(
                visible = isPlayerExpanded && currentActive != null,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it }),
                modifier = Modifier.fillMaxSize()
            ) {
                if (currentActive != null) {
                    VideoDetailView(
                        video = currentActive,
                        isPlaying = isPlaying,
                        onPlayingChange = { viewModel.setPlaying(it) },
                        isBookmarked = viewModel.isVideoBookmarked(currentActive.id),
                        onToggleBookmark = { viewModel.toggleBookmark(currentActive) },
                        isSubscribed = viewModel.isChannelSubscribed(currentActive.channelId),
                        onToggleSubscribe = {
                            viewModel.toggleSubscription(
                                com.example.data.model.ChannelItem(
                                    id = currentActive.channelId,
                                    name = currentActive.channelTitle,
                                    avatarUrl = currentActive.channelAvatarUrl,
                                    subscribersCount = "1.2M подписчиков"
                                )
                            )
                        },
                        comments = viewModel.getCommentsForActiveVideo(currentActive.id),
                        relatedVideos = viewModel.allVideos,
                        onSelectRelatedVideo = { viewModel.selectVideo(it) },
                        onMinimize = { viewModel.minimizePlayer() },
                        modifier = Modifier.testTag("video_detail_view")
                    )
                }
            }
        }
    }
}
