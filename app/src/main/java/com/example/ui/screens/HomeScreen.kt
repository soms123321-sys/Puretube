package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AdBlockStats
import com.example.data.model.VideoItem
import com.example.ui.components.CategoryChipsRow
import com.example.ui.components.TopNavBar
import com.example.ui.components.VideoCard
import com.example.ui.theme.AdBlockBadgeBg
import com.example.ui.theme.AdBlockGreen
import com.example.ui.theme.PureTubeDarkBg

@Composable
fun HomeScreen(
    videos: List<VideoItem>,
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    adBlockStats: AdBlockStats,
    onVideoClick: (VideoItem) -> Unit,
    onSaveToWatchLater: (VideoItem) -> Unit,
    onShareVideo: (VideoItem) -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PureTubeDarkBg)
    ) {
        // App Bar
        TopNavBar(
            adBlockStats = adBlockStats,
            onSearchClick = onSearchClick
        )

        // Category Filter Chips
        CategoryChipsRow(
            categories = categories,
            selectedCategory = selectedCategory,
            onCategorySelected = onCategorySelected
        )

        // Ad-Free Shield Notification Banner
        Surface(
            color = AdBlockBadgeBg,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 6.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = null,
                    tint = AdBlockGreen,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Защита активна • Без рекламы на YouTube",
                        color = AdBlockGreen,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Заблокировано рекламы: ${adBlockStats.totalAdsBlocked} • Сэкономлено: ${adBlockStats.timeSavedMinutes} мин.",
                        color = Color.LightGray,
                        fontSize = 11.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Videos Feed
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(videos, key = { it.id }) { video ->
                VideoCard(
                    video = video,
                    onClick = { onVideoClick(video) },
                    onSaveToWatchLater = { onSaveToWatchLater(video) },
                    onShare = { onShareVideo(video) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}
