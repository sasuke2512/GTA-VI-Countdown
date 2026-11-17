package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.LocalAppStrings
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun TimeScrubberBar(
  offsetMinutes: Int,
  localTimePreview: String,
  onOffsetChange: (Int) -> Unit,
  onReset: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val strings = LocalAppStrings.current
  val isShifted = offsetMinutes != 0
  val offsetLabel = if (!isShifted) {
    "${strings.livePrefix} • $localTimePreview"
  } else {
    val sign = if (offsetMinutes > 0) "+" else "-"
    val totalAbs = abs(offsetMinutes)
    val h = totalAbs / 60
    val m = totalAbs % 60
    val deltaStr = if (m == 0) "${sign}${h}h" else "${sign}${h}h ${m}m"
    "${strings.shiftedPrefix} $deltaStr • $localTimePreview"
  }

  Surface(
    modifier = modifier
      .fillMaxWidth()
      .testTag("time_scrubber_card"),
    shape = RoundedCornerShape(20.dp),
    color = MaterialTheme.colorScheme.surfaceContainer,
    tonalElevation = 2.dp,
    border = BorderStroke(
      width = 1.dp,
      color = if (isShifted) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.65f)
      } else {
        MaterialTheme.colorScheme.outline
      },
    ),
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
          Icon(
            imageVector = Icons.Rounded.Schedule,
            contentDescription = null,
            tint = if (isShifted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
            modifier = Modifier.size(18.dp),
          )
          Column {
            Text(
              text = strings.timeMachineTitle,
              style = MaterialTheme.typography.titleMedium,
              color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
              text = offsetLabel,
              style = MaterialTheme.typography.labelSmall,
              color = if (isShifted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
              fontWeight = FontWeight.Bold,
            )
          }
        }

        AnimatedVisibility(visible = isShifted) {
          Surface(
            modifier = Modifier
              .clip(RoundedCornerShape(50))
              .clickable(onClick = onReset)
              .testTag("reset_time_scrubber_button"),
            shape = RoundedCornerShape(50),
            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.16f),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)),
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
              verticalAlignment = Alignment.CenterVertically,
            ) {
              Icon(
                imageVector = Icons.Rounded.Refresh,
                contentDescription = strings.liveNowButton,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(14.dp),
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = strings.liveNowButton,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold,
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(4.dp))

      Slider(
        value = offsetMinutes.toFloat(),
        onValueChange = { raw ->
          val snapped = (raw / 15f).roundToInt() * 15
          onOffsetChange(snapped)
        },
        valueRange = -720f..1440f,
        colors = SliderDefaults.colors(
          thumbColor = if (isShifted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
          activeTrackColor = if (isShifted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
          inactiveTrackColor = MaterialTheme.colorScheme.outline,
        ),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("time_scrubber_slider"),
      )

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
      ) {
        Text("-12h", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(strings.sliderNow, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text("+12h", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text("+24h", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
      }
    }
  }
}
