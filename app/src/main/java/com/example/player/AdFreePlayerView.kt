package com.example.player

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color as AndroidColor
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Forward10
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay10
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.data.model.VideoItem
import com.example.ui.theme.AdBlockGreen
import com.example.ui.theme.PureTubeDarkBg
import com.example.ui.theme.PureTubeRedBright
import kotlinx.coroutines.delay

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun AdFreePlayerView(
    video: VideoItem,
    isPlaying: Boolean,
    onPlayingChange: (Boolean) -> Unit,
    isFullscreen: Boolean,
    onToggleFullscreen: () -> Unit,
    onClosePlayer: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showControls by remember { mutableStateOf(true) }
    var currentSpeed by remember { mutableFloatStateOf(1.0f) }
    var selectedQuality by remember { mutableStateOf("1080p") }
    var isBackgroundAudioEnabled by remember { mutableStateOf(true) }
    var showSpeedMenu by remember { mutableStateOf(false) }
    var showQualityMenu by remember { mutableStateOf(false) }

    var webViewInstance by remember { mutableStateOf<WebView?>(null) }
    val context = LocalContext.current

    // Auto-hide controls after 4 seconds of inactivity
    LaunchedEffect(showControls, isPlaying) {
        if (showControls && isPlaying) {
            delay(4000)
            showControls = false
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Black)
            .aspectRatio(if (isFullscreen) 16f / 9f else 16f / 9f)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                showControls = !showControls
            }
    ) {
        // Embedded Ad-Free Shield Player WebView
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                createAdFreeWebView(ctx, video.id).also { wv ->
                    webViewInstance = wv
                }
            },
            update = { wv ->
                // Ensure proper url is loaded if video changes
                val currentTag = wv.tag as? String
                if (currentTag != video.id) {
                    wv.tag = video.id
                    loadVideoIntoWebView(wv, video.id)
                }
            }
        )

        // Overlay Controls
        AnimatedVisibility(
            visible = showControls,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.55f))
            ) {
                // Top Action Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                        .align(Alignment.TopCenter),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Shield Ad-Free Badge
                    Surface(
                        color = Color(0xDD12301A),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.clip(RoundedCornerShape(20.dp))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = "Без рекламы",
                                tint = AdBlockGreen,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "БЕЗ РЕКЛАМЫ",
                                color = AdBlockGreen,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Background audio toggle
                        IconButton(
                            onClick = {
                                isBackgroundAudioEnabled = !isBackgroundAudioEnabled
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Headphones,
                                contentDescription = "Фоновый режим",
                                tint = if (isBackgroundAudioEnabled) AdBlockGreen else Color.White
                            )
                        }

                        // Quality menu
                        Box {
                            IconButton(onClick = { showQualityMenu = true }) {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = "Качество",
                                    tint = Color.White
                                )
                            }
                            DropdownMenu(
                                expanded = showQualityMenu,
                                onDismissRequest = { showQualityMenu = false }
                            ) {
                                listOf("1080p 60fps", "720p", "480p", "360p", "Auto").forEach { quality ->
                                    DropdownMenuItem(
                                        text = { Text(quality) },
                                        onClick = {
                                            selectedQuality = quality
                                            showQualityMenu = false
                                        }
                                    )
                                }
                            }
                        }

                        // Speed menu
                        Box {
                            IconButton(onClick = { showSpeedMenu = true }) {
                                Icon(
                                    imageVector = Icons.Default.Speed,
                                    contentDescription = "Скорость",
                                    tint = Color.White
                                )
                            }
                            DropdownMenu(
                                expanded = showSpeedMenu,
                                onDismissRequest = { showSpeedMenu = false }
                            ) {
                                listOf(0.5f, 0.75f, 1.0f, 1.25f, 1.5f, 2.0f).forEach { speed ->
                                    DropdownMenuItem(
                                        text = { Text("${speed}x") },
                                        onClick = {
                                            currentSpeed = speed
                                            showSpeedMenu = false
                                            webViewInstance?.evaluateJavascript(
                                                "if (window.player && window.player.setPlaybackRate) { window.player.setPlaybackRate($speed); } else { var v = document.querySelector('video'); if (v) v.playbackRate = $speed; }",
                                                null
                                            )
                                        }
                                    )
                                }
                            }
                        }

                        // Minimize / Close Player
                        IconButton(onClick = onClosePlayer) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Закрыть плеер",
                                tint = Color.White
                            )
                        }
                    }
                }

                // Center Play/Pause & Seek Controls
                Row(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Rewind 10s
                    IconButton(
                        onClick = {
                            webViewInstance?.evaluateJavascript(
                                "if (window.player && window.player.seekTo) { window.player.seekTo(Math.max(0, window.player.getCurrentTime() - 10), true); } else { var v = document.querySelector('video'); if (v) v.currentTime -= 10; }",
                                null
                            )
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color.Black.copy(alpha = 0.4f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Replay10,
                            contentDescription = "Назад на 10 сек",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(32.dp))

                    // Play / Pause
                    IconButton(
                        onClick = {
                            val newPlayState = !isPlaying
                            onPlayingChange(newPlayState)
                            if (newPlayState) {
                                webViewInstance?.evaluateJavascript(
                                    "if (window.player && window.player.playVideo) { window.player.playVideo(); } else { var v = document.querySelector('video'); if (v) v.play(); }",
                                    null
                                )
                            } else {
                                webViewInstance?.evaluateJavascript(
                                    "if (window.player && window.player.pauseVideo) { window.player.pauseVideo(); } else { var v = document.querySelector('video'); if (v) v.pause(); }",
                                    null
                                )
                            }
                        },
                        modifier = Modifier
                            .size(64.dp)
                            .background(PureTubeRedBright, CircleShape)
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Пауза" else "Воспроизведение",
                            tint = Color.White,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(32.dp))

                    // Forward 10s
                    IconButton(
                        onClick = {
                            webViewInstance?.evaluateJavascript(
                                "if (window.player && window.player.seekTo) { window.player.seekTo(window.player.getCurrentTime() + 10, true); } else { var v = document.querySelector('video'); if (v) v.currentTime += 10; }",
                                null
                            )
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color.Black.copy(alpha = 0.4f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Forward10,
                            contentDescription = "Вперед на 10 сек",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                // Bottom Bar with Title & Fullscreen
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .align(Alignment.BottomCenter),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = video.title,
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1
                        )
                        Text(
                            text = "${video.channelTitle} • $selectedQuality • ${currentSpeed}x",
                            color = Color.White.copy(alpha = 0.75f),
                            fontSize = 11.sp
                        )
                    }

                    IconButton(onClick = onToggleFullscreen) {
                        Icon(
                            imageVector = if (isFullscreen) Icons.Default.FullscreenExit else Icons.Default.Fullscreen,
                            contentDescription = if (isFullscreen) "Выйти из полноэкранного" else "Во весь экран",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }

    DisposableEffect(video.id) {
        onDispose {
            webViewInstance?.destroy()
            webViewInstance = null
        }
    }
}

private fun createAdFreeWebView(context: Context, videoId: String): WebView {
    val webView = WebView(context).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        setBackgroundColor(AndroidColor.BLACK)
        settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            mediaPlaybackRequiresUserGesture = false
            loadWithOverviewMode = true
            useWideViewPort = true
            cacheMode = WebSettings.LOAD_DEFAULT
            allowFileAccess = false
            builtInZoomControls = false
            displayZoomControls = false
        }
        webChromeClient = WebChromeClient()
        webViewClient = object : WebViewClient() {
            override fun shouldInterceptRequest(
                view: WebView?,
                request: WebResourceRequest?
            ): WebResourceResponse? {
                val url = request?.url?.toString() ?: ""
                // Intercept and nullify ad tracking and ad serving endpoints
                if (url.contains("doubleclick.net") ||
                    url.contains("googleads.g.doubleclick.net") ||
                    url.contains("youtube.com/pagead/") ||
                    url.contains("youtube.com/ptracking") ||
                    url.contains("adservice.google.com")
                ) {
                    return WebResourceResponse("text/plain", "utf-8", null)
                }
                return super.shouldInterceptRequest(view, request)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                injectAdBlockScript(view)
            }
        }
    }
    loadVideoIntoWebView(webView, videoId)
    return webView
}

private fun injectAdBlockScript(webView: WebView?) {
    val script = """
        (function() {
            // CSS Rule injection to hide any ad overlays
            var style = document.createElement('style');
            style.innerHTML = `
                .ytp-ad-module, .ytp-ad-overlay-container, .video-ads,
                .ytp-ad-player-overlay, .ad-showing, .ytp-ad-skip-button,
                .ytp-ad-preview-container, .ytp-ad-action-interstitial,
                .annotation, .ytp-paid-content-overlay {
                    display: none !important;
                    visibility: hidden !important;
                }
            `;
            document.head.appendChild(style);

            // Interval to auto-skip and mute ads if they appear
            setInterval(function() {
                // Auto click skip ad button
                var skipBtn = document.querySelector('.ytp-ad-skip-button, .ytp-ad-skip-button-modern, .ytp-skip-ad-button');
                if (skipBtn) {
                    skipBtn.click();
                }
                // If ad is playing on the video element, speed through to end
                var adElement = document.querySelector('.ad-showing video, .video-ads video');
                if (adElement) {
                    adElement.muted = true;
                    adElement.playbackRate = 16.0;
                    if (isFinite(adElement.duration)) {
                        adElement.currentTime = adElement.duration;
                    }
                }
            }, 500);
        })();
    """.trimIndent()
    webView?.evaluateJavascript(script, null)
}

private fun loadVideoIntoWebView(webView: WebView, videoId: String) {
    val html = """
        <!DOCTYPE html>
        <html>
        <head>
            <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
            <style>
                * { margin: 0; padding: 0; box-sizing: border-box; }
                html, body { width: 100%; height: 100%; background: #000; overflow: hidden; }
                #player-container { width: 100%; height: 100%; position: absolute; top: 0; left: 0; }
                iframe { width: 100% !important; height: 100% !important; border: 0 !important; }
            </style>
        </head>
        <body>
            <div id="player-container">
                <iframe 
                    id="puretube_iframe"
                    src="https://www.youtube-nocookie.com/embed/$videoId?autoplay=1&enablejsapi=1&playsinline=1&modestbranding=1&rel=0&iv_load_policy=3&fs=0&controls=1" 
                    allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" 
                    allowfullscreen>
                </iframe>
            </div>
            <script>
                var tag = document.createElement('script');
                tag.src = "https://www.youtube.com/iframe_api";
                var firstScriptTag = document.getElementsByTagName('script')[0];
                firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);

                var player;
                function onYouTubeIframeAPIReady() {
                    player = new YT.Player('puretube_iframe', {
                        events: {
                            'onReady': onPlayerReady
                        }
                    });
                }
                function onPlayerReady(event) {
                    event.target.playVideo();
                }
            </script>
        </body>
        </html>
    """.trimIndent()

    webView.loadDataWithBaseURL("https://www.youtube-nocookie.com", html, "text/html", "UTF-8", null)
}
