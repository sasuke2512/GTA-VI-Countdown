package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircleOutline
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material.icons.rounded.SportsEsports
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.data.LocalAppStrings
import com.example.model.AppLanguage
import com.example.model.CountdownMode
import com.example.model.LaunchWaveItem
import com.example.ui.components.AppFooter
import com.example.ui.components.GtaCountdownHeroCard
import com.example.ui.components.LanguageFlagSelector
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.OutfitFontFamily
import com.example.ui.viewmodel.ViceTimeUiState

@Composable
fun GtaCountdownScreen(
  uiState: ViceTimeUiState,
  onToggleTheme: () -> Unit,
  onSelectLanguage: (AppLanguage) -> Unit,
  onSelectMode: (CountdownMode) -> Unit,
  onOpenCustomPicker: () -> Unit,
  onPinWaveCity: (String) -> Unit,
  modifier: Modifier = Modifier,
) {
  val strings = LocalAppStrings.current

  BoxWithConstraints(
    modifier = modifier.fillMaxSize(),
    contentAlignment = Alignment.TopCenter,
  ) {
    LazyColumn(
      contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp),
      modifier = Modifier
        .widthIn(max = 760.dp)
        .fillMaxSize(),
    ) {
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = strings.launchRadarTitle,
              style = MaterialTheme.typography.headlineMedium.copy(
                fontFamily = OutfitFontFamily,
                fontWeight = FontWeight.Bold,
              ),
              color = MaterialTheme.colorScheme.onBackground,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis,
            )
            Text(
              text = strings.launchRadarSubtitle,
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis,
            )
          }

          Spacer(modifier = Modifier.width(8.dp))

          Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
          ) {
            LanguageFlagSelector(
              selectedLanguage = uiState.selectedLanguage,
              onSelectLanguage = onSelectLanguage,
              testTag = "language_selector_button_radar",
            )

            Surface(
              modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(16.dp))
                .clickable(onClick = onToggleTheme)
                .testTag("toggle_theme_button_radar"),
              shape = RoundedCornerShape(16.dp),
              color = MaterialTheme.colorScheme.surfaceContainer,
              tonalElevation = 2.dp,
              border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            ) {
              Icon(
                imageVector = if (uiState.isDarkTheme) Icons.Rounded.LightMode else Icons.Rounded.DarkMode,
                contentDescription = if (uiState.isDarkTheme) strings.switchToLightMode else strings.switchToDarkMode,
                tint = if (uiState.isDarkTheme) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(12.dp),
              )
            }
          }
        }
      }

      item {
        GtaCountdownHeroCard(
          countdown = uiState.countdown,
          mode = uiState.countdownMode,
          onSelectMode = onSelectMode,
          onOpenCustomPicker = onOpenCustomPicker,
        )
      }

      item {
        Surface(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(20.dp),
          color = MaterialTheme.colorScheme.surfaceContainer,
          tonalElevation = 2.dp,
          border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
          ) {
            Surface(
              shape = RoundedCornerShape(14.dp),
              color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.14f),
              modifier = Modifier.size(48.dp),
            ) {
              Icon(
                imageVector = Icons.Rounded.SportsEsports,
                contentDescription = strings.releaseInfoTitle,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(12.dp),
              )
            }
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = strings.releaseInfoTitle,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
              )
              Text(
                text = strings.releaseInfoBody,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
              )
            }
          }
        }
      }

      item {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Text(
            text = strings.globalWaveHeader,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
          Text(
            text = strings.eastToWest,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.tertiary,
          )
        }
      }

      itemsIndexed(uiState.launchWaves, key = { _, item -> item.zoneId }) { index, wave ->
        LaunchWaveRow(
          index = index + 1,
          wave = wave,
          isPinned = wave.zoneId in uiState.pinnedZoneSet,
          onTogglePin = { onPinWaveCity(wave.zoneId) },
        )
      }

      item {
        Column {
          Spacer(modifier = Modifier.height(6.dp))
          AppFooter()
          Spacer(modifier = Modifier.height(12.dp))
        }
      }
    }
  }
}

@Composable
private fun LaunchWaveRow(
  index: Int,
  wave: LaunchWaveItem,
  isPinned: Boolean,
  onTogglePin: () -> Unit,
) {
  Surface(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(18.dp))
      .clickable(onClick = onTogglePin)
      .testTag("launch_wave_${wave.zoneId}"),
    shape = RoundedCornerShape(18.dp),
    color = MaterialTheme.colorScheme.surfaceContainer,
    tonalElevation = 1.dp,
    border = BorderStroke(
      width = 1.dp,
      color = if (wave.isUnlocked) {
        MaterialTheme.colorScheme.secondary.copy(alpha = 0.45f)
      } else {
        MaterialTheme.colorScheme.outlineVariant
      },
    ),
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 14.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween,
    ) {
      Row(
        modifier = Modifier.weight(1f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
      ) {
        Box(
          modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(
              if (wave.isUnlocked) {
                MaterialTheme.colorScheme.secondary.copy(alpha = 0.18f)
              } else {
                MaterialTheme.colorScheme.primary.copy(alpha = 0.16f)
              },
            ),
          contentAlignment = Alignment.Center,
        ) {
          Text(
            text = "$index",
            style = MaterialTheme.typography.labelSmall,
            color = if (wave.isUnlocked) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
          )
        }

        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = wave.cityName,
            style = MaterialTheme.typography.titleMedium.copy(fontFamily = OutfitFontFamily),
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
          )
          Text(
            text = wave.unlockInYourTime,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
          )
        }
      }

      Spacer(modifier = Modifier.width(8.dp))

      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
      ) {
        Surface(
          shape = RoundedCornerShape(50),
          color = if (wave.isUnlocked) {
            MaterialTheme.colorScheme.secondary.copy(alpha = 0.16f)
          } else {
            MaterialTheme.colorScheme.surfaceContainerHigh
          },
        ) {
          Text(
            text = wave.countdownShort,
            style = MaterialTheme.typography.labelSmall.copy(
              fontFamily = JetBrainsMonoFontFamily,
              fontFeatureSettings = "tnum",
            ),
            color = MaterialTheme.colorScheme.secondary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
          )
        }

        Icon(
          imageVector = if (isPinned) Icons.Rounded.CheckCircle else Icons.Rounded.AddCircleOutline,
          contentDescription = null,
          tint = if (isPinned) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.size(22.dp),
        )
      }
    }
  }
}
