package com.example.ui.screens

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbDownOffAlt
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.ThumbUpOffAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.model.CommentItem
import com.example.data.model.VideoItem
import com.example.player.AdFreePlayerView
import com.example.ui.theme.AdBlockBadgeBg
import com.example.ui.theme.AdBlockGreen
import com.example.ui.theme.PureTubeDarkBg
import com.example.ui.theme.PureTubeRedBright
import com.example.ui.theme.PureTubeSurfaceVariant
import kotlinx.coroutines.launch

@Composable
fun VideoDetailView(
    video: VideoItem,
    isPlaying: Boolean,
    onPlayingChange: (Boolean) -> Unit,
    isBookmarked: Boolean,
    onToggleBookmark: () -> Unit,
    isSubscribed: Boolean,
    onToggleSubscribe: () -> Unit,
    comments: List<CommentItem>,
    relatedVideos: List<VideoItem>,
    onSelectRelatedVideo: (VideoItem) -> Unit,
    onMinimize: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var isLiked by remember { mutableStateOf(false) }
    var isDisliked by remember { mutableStateOf(false) }
    var isDownloaded by remember { mutableStateOf(false) }
    var isDescriptionExpanded by remember { mutableStateOf(false) }
    var isFullscreen by remember { mutableStateOf(false) }

    var newCommentText by remember { mutableStateOf("") }
    val localComments = remember { mutableStateListOf(*comments.toTypedArray()) }

    BackHandler {
        if (isFullscreen) {
            isFullscreen = false
        } else {
            onMinimize()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PureTubeDarkBg)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Player Box
            AdFreePlayerView(
                video = video,
                isPlaying = isPlaying,
                onPlayingChange = onPlayingChange,
                isFullscreen = isFullscreen,
                onToggleFullscreen = { isFullscreen = !isFullscreen },
                onClosePlayer = onMinimize
            )

            // If fullscreen, hide scrollable content below
            if (!isFullscreen) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 14.dp)
                ) {
                    item {
                        Spacer(modifier = Modifier.height(10.dp))

                        // Video Title
                        Text(
                            text = video.title,
                            color = Color.White,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 22.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Views and Date + Ad-Free badge
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "${video.viewsCount} • ${video.publishedAt}",
                                color = Color(0xFFAAAAAA),
                                fontSize = 12.sp
                            )

                            Surface(
                                color = AdBlockBadgeBg,
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Security,
                                        contentDescription = null,
                                        tint = AdBlockGreen,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "0 РЕКЛАМЫ",
                                        color = AdBlockGreen,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Interactive Action Buttons Bar (Like, Dislike, Share, Download, Save)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            // Like
                            ActionButtonItem(
                                icon = if (isLiked) Icons.Default.ThumbUp else Icons.Default.ThumbUpOffAlt,
                                label = if (isLiked) "Нравится" else video.likesCount,
                                isHighlight = isLiked,
                                onClick = {
                                    isLiked = !isLiked
                                    if (isLiked) isDisliked = false
                                }
                            )

                            // Dislike
                            ActionButtonItem(
                                icon = if (isDisliked) Icons.Default.ThumbDown else Icons.Default.ThumbDownOffAlt,
                                label = "Не нравится",
                                isHighlight = isDisliked,
                                onClick = {
                                    isDisliked = !isDisliked
                                    if (isDisliked) isLiked = false
                                }
                            )

                            // Share
                            ActionButtonItem(
                                icon = Icons.Default.Share,
                                label = "Поделиться",
                                isHighlight = false,
                                onClick = {
                                    val sendIntent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(Intent.EXTRA_TEXT, "Смотри видео без рекламы в PureTube: https://youtu.be/${video.id}")
                                        type = "text/plain"
                                    }
                                    context.startActivity(Intent.createChooser(sendIntent, "Поделиться видео"))
                                }
                            )

                            // Download / Offline Save
                            ActionButtonItem(
                                icon = if (isDownloaded) Icons.Default.DownloadDone else Icons.Default.Download,
                                label = if (isDownloaded) "Скачано" else "Скачать",
                                isHighlight = isDownloaded,
                                onClick = {
                                    isDownloaded = !isDownloaded
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            if (isDownloaded) "Видео сохранено для офлайн просмотра" else "Видео удалено из офлайн"
                                        )
                                    }
                                }
                            )

                            // Save to Watch Later
                            ActionButtonItem(
                                icon = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                label = if (isBookmarked) "Сохранено" else "Позже",
                                isHighlight = isBookmarked,
                                onClick = onToggleBookmark
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Divider(color = Color(0xFF282828), thickness = 1.dp)
                        Spacer(modifier = Modifier.height(12.dp))

                        // Channel Info Bar
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(video.channelAvatarUrl)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = video.channelTitle,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(CircleShape)
                                        .background(Color.DarkGray)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = video.channelTitle,
                                        color = Color.White,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = "1.2M подписчиков",
                                        color = Color(0xFFAAAAAA),
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            // Subscribe Button
                            Button(
                                onClick = onToggleSubscribe,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isSubscribed) PureTubeSurfaceVariant else Color.White,
                                    contentColor = if (isSubscribed) Color.White else Color.Black
                                ),
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                if (isSubscribed) {
                                    Icon(
                                        imageVector = Icons.Default.NotificationsActive,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                }
                                Text(
                                    text = if (isSubscribed) "Вы подписаны" else "Подписаться",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Expandable Description Card
                        Card(
                            colors = CardDefaults.cardColors(containerColor = PureTubeSurfaceVariant),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { isDescriptionExpanded = !isDescriptionExpanded }
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Описание видео",
                                        color = Color.White,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 13.sp
                                    )
                                    Icon(
                                        imageVector = if (isDescriptionExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                        contentDescription = null,
                                        tint = Color.LightGray,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = video.description,
                                    color = Color(0xFFDDDDDD),
                                    fontSize = 12.sp,
                                    maxLines = if (isDescriptionExpanded) Int.MAX_VALUE else 2,
                                    overflow = TextOverflow.Ellipsis,
                                    lineHeight = 16.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Comments Section Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Комментарии (${localComments.size})",
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Comment Input Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = newCommentText,
                                onValueChange = { newCommentText = it },
                                placeholder = { Text("Оставить комментарий...", color = Color.Gray, fontSize = 12.sp) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(20.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedBorderColor = PureTubeRedBright,
                                    unfocusedBorderColor = Color(0xFF383838),
                                    focusedContainerColor = PureTubeSurfaceVariant,
                                    unfocusedContainerColor = PureTubeSurfaceVariant
                                ),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(
                                onClick = {
                                    if (newCommentText.isNotBlank()) {
                                        localComments.add(
                                            0,
                                            CommentItem(
                                                id = "user_${System.currentTimeMillis()}",
                                                authorName = "Вы",
                                                authorAvatarUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=100&auto=format&fit=crop&q=80",
                                                text = newCommentText.trim(),
                                                publishedTime = "только что",
                                                likesCount = "0"
                                            )
                                        )
                                        newCommentText = ""
                                    }
                                },
                                enabled = newCommentText.isNotBlank()
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Send,
                                    contentDescription = "Отправить",
                                    tint = if (newCommentText.isNotBlank()) PureTubeRedBright else Color.Gray
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Comments List preview (first 3)
                        localComments.take(3).forEach { comment ->
                            CommentRowItem(comment = comment)
                            Spacer(modifier = Modifier.height(10.dp))
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Divider(color = Color(0xFF282828), thickness = 1.dp)
                        Spacer(modifier = Modifier.height(14.dp))

                        // Up Next / Related Videos
                        Text(
                            text = "Следующие видео",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    items(relatedVideos.filter { it.id != video.id }) { relatedVideo ->
                        CompactVideoRow(
                            video = relatedVideo,
                            onClick = { onSelectRelatedVideo(relatedVideo) }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    item {
                        Spacer(modifier = Modifier.height(40.dp))
                    }
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        )
    }
}

@Composable
private fun ActionButtonItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isHighlight: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isHighlight) PureTubeRedBright else Color.White,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = if (isHighlight) PureTubeRedBright else Color(0xFFAAAAAA),
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun CommentRowItem(comment: CommentItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(comment.authorAvatarUrl)
                .crossfade(true)
                .build(),
            contentDescription = comment.authorName,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(Color.DarkGray)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = comment.authorName,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = comment.publishedTime,
                    color = Color.Gray,
                    fontSize = 11.sp
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = comment.text,
                color = Color(0xFFDDDDDD),
                fontSize = 12.sp,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
private fun CompactVideoRow(
    video: VideoItem,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .width(130.dp)
                .height(78.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Black)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(video.thumbnailUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = video.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(4.dp)
                    .background(Color.Black.copy(alpha = 0.8f), RoundedCornerShape(3.dp))
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            ) {
                Text(
                    text = video.durationFormatted,
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = video.title,
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 17.sp
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = video.channelTitle,
                color = Color(0xFFAAAAAA),
                fontSize = 11.sp
            )
            Text(
                text = "${video.viewsCount} • ${video.publishedAt}",
                color = Color(0xFF777777),
                fontSize = 11.sp
            )
        }
    }
}
