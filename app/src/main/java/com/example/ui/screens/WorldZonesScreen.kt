package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material.icons.rounded.Public
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.components.AppFooter
import com.example.ui.components.GtaCountdownCompactBanner
import com.example.ui.components.TimeScrubberBar
import com.example.ui.components.WorldClockCard
import com.example.ui.theme.OutfitFontFamily
import com.example.ui.viewmodel.ViceTimeUiState

@Composable
fun WorldZonesScreen(
  uiState: ViceTimeUiState,
  onToggle24Hour: () -> Unit,
  onToggleTheme: () -> Unit,
  onOpenAddZone: () -> Unit,
  onOpenCountdownTab: () -> Unit,
  onScrubberChange: (Int) -> Unit,
  onResetScrubber: () -> Unit,
  onPromoteZone: (String) -> Unit,
  onRemoveZone: (String) -> Unit,
  modifier: Modifier = Modifier,
) {
  BoxWithConstraints(
    modifier = modifier.fillMaxSize(),
    contentAlignment = Alignment.TopCenter,
  ) {
    val columns = if (maxWidth >= 600.dp) 2 else 1

    LazyVerticalGrid(
      columns = GridCells.Fixed(columns),
      contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
      horizontalArrangement = Arrangement.spacedBy(12.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp),
      modifier = Modifier
        .widthIn(max = 960.dp)
        .fillMaxSize(),
    ) {
      item(span = { GridItemSpan(columns) }) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
          // Header Row
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
              ) {
                Icon(
                  imageVector = Icons.Rounded.Public,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.secondary,
                  modifier = Modifier.size(22.dp),
                )
                Text(
                  text = "ViceTime",
                  style = MaterialTheme.typography.headlineMedium.copy(
                    fontFamily = OutfitFontFamily,
                    fontWeight = FontWeight.Bold,
                  ),
                  color = MaterialTheme.colorScheme.onBackground,
                )
              }
              Text(
                text = "Local: ${uiState.localZoneName} • ${uiState.pinnedClocks.size} zones",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
              )
            }

            Row(
              horizontalArrangement = Arrangement.spacedBy(8.dp),
              verticalAlignment = Alignment.CenterVertically,
            ) {
              // Light / Dark Theme Toggle
              Surface(
                modifier = Modifier
                  .size(48.dp)
                  .clip(RoundedCornerShape(16.dp))
                  .clickable(onClick = onToggleTheme)
                  .testTag("toggle_theme_button"),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceContainer,
                tonalElevation = 2.dp,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
              ) {
                Icon(
                  imageVector = if (uiState.isDarkTheme) Icons.Rounded.LightMode else Icons.Rounded.DarkMode,
                  contentDescription = if (uiState.isDarkTheme) "Switch to light mode" else "Switch to dark mode",
                  tint = if (uiState.isDarkTheme) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary,
                  modifier = Modifier.padding(12.dp),
                )
              }

              // 12H / 24H Toggle Pill
              Surface(
                modifier = Modifier
                  .height(48.dp)
                  .clip(RoundedCornerShape(16.dp))
                  .clickable(onClick = onToggle24Hour)
                  .testTag("toggle_time_format_button"),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceContainer,
                tonalElevation = 2.dp,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 14.dp),
                  verticalAlignment = Alignment.CenterVertically,
                ) {
                  Text(
                    text = if (uiState.is24HourFormat) "24H" else "12H",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold,
                  )
                }
              }

              // Add City Button
              Surface(
                modifier = Modifier
                  .height(48.dp)
                  .clip(RoundedCornerShape(16.dp))
                  .clickable(onClick = onOpenAddZone)
                  .testTag("add_city_button"),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.primary,
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 14.dp),
                  verticalAlignment = Alignment.CenterVertically,
                ) {
                  Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Add city time zone",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(18.dp),
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(
                    text = "Add",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold,
                  )
                }
              }
            }
          }

          // Compact GTA VI Countdown Banner
          GtaCountdownCompactBanner(
            countdown = uiState.countdown,
            mode = uiState.countdownMode,
            onClick = onOpenCountdownTab,
          )

          // Interactive Time Machine Scrubber
          TimeScrubberBar(
            offsetMinutes = uiState.scrubberOffsetMinutes,
            localTimePreview = uiState.localFormattedTime,
            onOffsetChange = onScrubberChange,
            onReset = onResetScrubber,
          )

          Text(
            text = "PINNED WORLD ZONES",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp),
          )
        }
      }

      items(uiState.pinnedClocks, key = { it.zoneId }) { clockItem ->
        WorldClockCard(
          item = clockItem,
          canRemove = uiState.pinnedClocks.size > 1,
          onPromoteToTop = { onPromoteZone(clockItem.zoneId) },
          onRemove = { onRemoveZone(clockItem.zoneId) },
        )
      }

      item(span = { GridItemSpan(columns) }) {
        Column {
          Spacer(modifier = Modifier.height(6.dp))
          AppFooter()
          Spacer(modifier = Modifier.height(12.dp))
        }
      }
    }
  }
}
