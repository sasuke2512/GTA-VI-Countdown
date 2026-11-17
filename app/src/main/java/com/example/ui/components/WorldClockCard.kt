package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.NightsStay
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.material.icons.rounded.WbTwilight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LocalAppStrings
import com.example.model.DayPhase
import com.example.model.WorldClockUiItem
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.OutfitFontFamily

@Composable
fun WorldClockCard(
  item: WorldClockUiItem,
  canRemove: Boolean,
  onPromoteToTop: () -> Unit,
  onRemove: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val strings = LocalAppStrings.current
  var expanded by remember { mutableStateOf(false) }

  val phaseAccent = when (item.dayPhase) {
    DayPhase.DAWN -> MaterialTheme.colorScheme.error
    DayPhase.DAY -> MaterialTheme.colorScheme.secondary
    DayPhase.SUNSET -> MaterialTheme.colorScheme.tertiary
    DayPhase.NIGHT -> Color(0xFF7B68EE)
  }

  val phaseIcon = when (item.dayPhase) {
    DayPhase.DAWN -> Icons.Rounded.WbTwilight
    DayPhase.DAY -> Icons.Rounded.WbSunny
    DayPhase.SUNSET -> Icons.Rounded.WbTwilight
    DayPhase.NIGHT -> Icons.Rounded.NightsStay
  }

  val phaseLabel = strings.dayPhaseLabel(item.dayPhase)

  Surface(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(22.dp))
      .clickable { expanded = !expanded }
      .testTag("world_clock_card_${item.zoneId}"),
    shape = RoundedCornerShape(22.dp),
    color = MaterialTheme.colorScheme.surfaceContainer,
    tonalElevation = 2.dp,
    border = BorderStroke(
      width = 1.dp,
      color = if (item.isLocal) {
        MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f)
      } else {
        phaseAccent.copy(alpha = 0.28f)
      },
    ),
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .background(
          Brush.horizontalGradient(
            listOf(
              phaseAccent.copy(alpha = 0.10f),
              Color.Transparent,
            ),
          ),
        ),
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 18.dp, vertical = 16.dp),
      ) {
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
                imageVector = phaseIcon,
                contentDescription = phaseLabel,
                tint = phaseAccent,
                modifier = Modifier.size(18.dp),
              )
              Text(
                text = item.cityName,
                style = MaterialTheme.typography.titleLarge.copy(fontFamily = OutfitFontFamily),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
              )
              if (item.badge != null || item.isLocal) {
                Surface(
                  shape = RoundedCornerShape(50),
                  color = if (item.isLocal) {
                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.16f)
                  } else {
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.16f)
                  },
                ) {
                  Text(
                    text = if (item.isLocal) strings.badgeLocal else item.badge.orEmpty().uppercase(),
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = if (item.isLocal) {
                      MaterialTheme.colorScheme.secondary
                    } else {
                      MaterialTheme.colorScheme.primary
                    },
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
              text = "${item.countryOrRegion} • ${item.utcOffsetLabel}",
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
              horizontalArrangement = Arrangement.spacedBy(8.dp),
              verticalAlignment = Alignment.CenterVertically,
            ) {
              Text(
                text = item.relativeOffsetLabel,
                style = MaterialTheme.typography.labelSmall,
                color = phaseAccent,
              )
              Text(
                text = "•",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
              )
              Text(
                text = item.gtaUnlockLabel,
                style = MaterialTheme.typography.labelSmall,
                color = if (item.isGtaUnlockedInZone) {
                  MaterialTheme.colorScheme.secondary
                } else {
                  MaterialTheme.colorScheme.primary
                },
                fontWeight = FontWeight.SemiBold,
              )
            }
          }

          Spacer(modifier = Modifier.width(12.dp))

          Column(horizontalAlignment = Alignment.End) {
            Row(verticalAlignment = Alignment.Bottom) {
              Text(
                text = item.formattedTime,
                style = MaterialTheme.typography.displayMedium.copy(
                  fontFamily = JetBrainsMonoFontFamily,
                  fontWeight = FontWeight.Bold,
                  fontSize = 30.sp,
                  fontFeatureSettings = "tnum",
                ),
                color = MaterialTheme.colorScheme.onSurface,
              )
              Spacer(modifier = Modifier.width(3.dp))
              Column(horizontalAlignment = Alignment.Start) {
                Text(
                  text = item.formattedSeconds,
                  style = MaterialTheme.typography.labelSmall,
                  color = phaseAccent,
                )
                if (item.formattedAmPm.isNotEmpty()) {
                  Text(
                    text = item.formattedAmPm,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                  )
                }
              }
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = item.formattedDate,
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
          }
        }

        AnimatedVisibility(visible = expanded) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(top = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Text(
              text = item.zoneId,
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
              TextButton(
                onClick = {
                  onPromoteToTop()
                  expanded = false
                },
                modifier = Modifier.testTag("promote_zone_${item.zoneId}"),
              ) {
                Icon(
                  imageVector = Icons.Rounded.ArrowUpward,
                  contentDescription = strings.pinTopAction,
                  modifier = Modifier.size(16.dp),
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(strings.pinTopAction)
              }
              if (canRemove) {
                IconButton(
                  onClick = onRemove,
                  modifier = Modifier
                    .size(48.dp)
                    .testTag("remove_zone_${item.zoneId}"),
                ) {
                  Icon(
                    imageVector = Icons.Rounded.DeleteOutline,
                    contentDescription = "${strings.removeZoneDesc} ${item.cityName}",
                    tint = MaterialTheme.colorScheme.error,
                  )
                }
              }
            }
          }
        }
      }
    }
  }
}
