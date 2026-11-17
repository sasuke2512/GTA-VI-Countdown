package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.EditCalendar
import androidx.compose.material.icons.rounded.Public
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.LocalAppStrings
import com.example.model.CountdownBreakdown
import com.example.model.CountdownMode
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.OutfitFontFamily
import com.example.ui.theme.ViceCyan
import com.example.ui.theme.ViceFlamingo
import com.example.ui.theme.ViceMint
import com.example.ui.theme.ViceObsidian
import com.example.ui.theme.ViceSunsetAmber
import java.util.Locale

@Composable
fun GtaCountdownCompactBanner(
  countdown: CountdownBreakdown?,
  mode: CountdownMode,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val strings = LocalAppStrings.current

  Surface(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(24.dp))
      .clickable(onClick = onClick)
      .testTag("gta_compact_banner"),
    shape = RoundedCornerShape(24.dp),
    border = BorderStroke(
      width = 1.dp,
      brush = Brush.horizontalGradient(listOf(ViceFlamingo.copy(alpha = 0.7f), ViceCyan.copy(alpha = 0.6f))),
    ),
    color = ViceObsidian,
  ) {
    Box(modifier = Modifier.fillMaxWidth()) {
      Image(
        painter = painterResource(id = R.drawable.img_vice_hero_banner),
        contentDescription = "Vice City sunset skyline",
        contentScale = ContentScale.Crop,
        alpha = 0.42f,
        modifier = Modifier.matchParentSize(),
      )
      Box(
        modifier = Modifier
          .matchParentSize()
          .background(
            Brush.horizontalGradient(
              listOf(
                ViceObsidian.copy(alpha = 0.92f),
                ViceObsidian.copy(alpha = 0.72f),
                ViceObsidian.copy(alpha = 0.88f),
              ),
            ),
          ),
      )

      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp),
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f),
          ) {
            Box(
              modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(if (countdown?.isUnlocked == true) ViceMint else ViceFlamingo),
            )
            Text(
              text = "GTA VI • ${strings.modeLabel(mode).uppercase(Locale.getDefault())}",
              style = MaterialTheme.typography.labelSmall,
              color = ViceCyan,
              fontWeight = FontWeight.Bold,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis,
            )
          }
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
          ) {
            Text(
              text = strings.launchRadarTitle,
              style = MaterialTheme.typography.labelSmall,
              color = Color.White.copy(alpha = 0.85f),
            )
            Icon(
              imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
              contentDescription = strings.launchRadarTitle,
              tint = ViceFlamingo,
              modifier = Modifier.size(16.dp),
            )
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (countdown == null) {
          Text(
            text = strings.syncingLaunchClock,
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
          )
        } else if (countdown.isUnlocked) {
          Text(
            text = strings.welcomeToLeonidaLive,
            style = MaterialTheme.typography.titleLarge.copy(fontFamily = OutfitFontFamily),
            color = ViceMint,
            fontWeight = FontWeight.Bold,
          )
        } else {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
          ) {
            Text(
              text = String.format(
                Locale.US,
                "%02dd : %02dh : %02dm : %02ds",
                countdown.days,
                countdown.hours,
                countdown.minutes,
                countdown.seconds,
              ),
              style = MaterialTheme.typography.headlineMedium.copy(
                fontFamily = JetBrainsMonoFontFamily,
                fontWeight = FontWeight.Bold,
                fontFeatureSettings = "tnum",
              ),
              color = Color.White,
            )
            Text(
              text = "19 Nov 2026",
              style = MaterialTheme.typography.labelSmall,
              color = ViceSunsetAmber,
            )
          }
        }
      }
    }
  }
}

@Composable
fun GtaCountdownHeroCard(
  countdown: CountdownBreakdown?,
  mode: CountdownMode,
  onSelectMode: (CountdownMode) -> Unit,
  onOpenCustomPicker: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val strings = LocalAppStrings.current

  Surface(
    modifier = modifier
      .fillMaxWidth()
      .testTag("gta_hero_card"),
    shape = RoundedCornerShape(28.dp),
    border = BorderStroke(
      width = 1.5.dp,
      brush = Brush.linearGradient(
        listOf(
          ViceFlamingo.copy(alpha = 0.85f),
          ViceSunsetAmber.copy(alpha = 0.65f),
          ViceCyan.copy(alpha = 0.85f),
        ),
      ),
    ),
    color = ViceObsidian,
  ) {
    Column(modifier = Modifier.fillMaxWidth()) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(210.dp),
      ) {
        Image(
          painter = painterResource(id = R.drawable.img_vice_hero_banner),
          contentDescription = "Leonida coastal twilight illustration",
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxSize(),
        )
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(
              Brush.verticalGradient(
                listOf(
                  ViceObsidian.copy(alpha = 0.30f),
                  ViceObsidian.copy(alpha = 0.55f),
                  ViceObsidian.copy(alpha = 0.98f),
                ),
              ),
            ),
        )

        Column(
          modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
          verticalArrangement = Arrangement.SpaceBetween,
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Surface(
              shape = RoundedCornerShape(50),
              color = ViceObsidian.copy(alpha = 0.72f),
              border = BorderStroke(1.dp, ViceCyan.copy(alpha = 0.5f)),
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
              ) {
                Icon(
                  imageVector = Icons.Rounded.Bolt,
                  contentDescription = null,
                  tint = ViceCyan,
                  modifier = Modifier.size(14.dp),
                )
                Text(
                  text = strings.gtaRadarBadge,
                  style = MaterialTheme.typography.labelSmall,
                  color = ViceCyan,
                  fontWeight = FontWeight.Bold,
                )
              }
            }

            Surface(
              shape = RoundedCornerShape(50),
              color = ViceFlamingo.copy(alpha = 0.22f),
              border = BorderStroke(1.dp, ViceFlamingo.copy(alpha = 0.6f)),
            ) {
              Text(
                text = "19 NOV 2026",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
              )
            }
          }

          Column {
            Text(
              text = strings.gtaTitle,
              style = MaterialTheme.typography.displayMedium.copy(
                fontFamily = OutfitFontFamily,
                fontWeight = FontWeight.Bold,
              ),
              color = Color.White,
            )
            Text(
              text = strings.modeSubtitle(mode),
              style = MaterialTheme.typography.bodyMedium,
              color = Color.White.copy(alpha = 0.82f),
            )
          }
        }
      }

      Column(
        modifier = Modifier
          .fillMaxWidth()
          .background(ViceObsidian)
          .padding(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
      ) {
        if (countdown == null) {
          Text(
            text = strings.calculatingCountdown,
            color = Color.White,
            style = MaterialTheme.typography.bodyLarge,
          )
        } else if (countdown.isUnlocked) {
          Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            color = ViceMint.copy(alpha = 0.14f),
            border = BorderStroke(1.dp, ViceMint.copy(alpha = 0.6f)),
          ) {
            Column(
              modifier = Modifier.padding(16.dp),
              horizontalAlignment = Alignment.CenterHorizontally,
            ) {
              Text(
                text = strings.unlockedNowAvailable,
                style = MaterialTheme.typography.titleLarge,
                color = ViceMint,
                fontWeight = FontWeight.Bold,
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "${strings.targetReachedPrefix} ${countdown.targetFormatted}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.8f),
              )
            }
          }
        } else {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
          ) {
            CountdownDigitTile(
              value = String.format(Locale.US, "%02d", countdown.days),
              label = strings.unitDays,
              accent = ViceFlamingo,
              modifier = Modifier.weight(1f),
            )
            CountdownDigitTile(
              value = String.format(Locale.US, "%02d", countdown.hours),
              label = strings.unitHours,
              accent = ViceSunsetAmber,
              modifier = Modifier.weight(1f),
            )
            CountdownDigitTile(
              value = String.format(Locale.US, "%02d", countdown.minutes),
              label = strings.unitMinutes,
              accent = ViceCyan,
              modifier = Modifier.weight(1f),
            )
            CountdownDigitTile(
              value = String.format(Locale.US, "%02d", countdown.seconds),
              label = strings.unitSeconds,
              accent = ViceMint,
              modifier = Modifier.weight(1f),
            )
          }

          Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
            ) {
              Text(
                text = "${strings.targetPrefix} ${countdown.targetFormatted}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFFB5B0D1),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
              )
              Text(
                text = "${(countdown.progressFraction * 100f).toInt()}%",
                style = MaterialTheme.typography.labelSmall,
                color = ViceCyan,
                fontWeight = FontWeight.Bold,
              )
            }
            LinearProgressIndicator(
              progress = { countdown.progressFraction },
              modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(50)),
              color = ViceFlamingo,
              trackColor = Color.White.copy(alpha = 0.1f),
            )
          }
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text(
            text = strings.countdownRegionHeader,
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFFB5B0D1),
          )
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
          ) {
            CountdownMode.entries.forEach { entry ->
              val selected = entry == mode
              val bgColor by animateColorAsState(
                targetValue = if (selected) ViceFlamingo.copy(alpha = 0.22f) else Color.White.copy(alpha = 0.05f),
                label = "modeBg",
              )
              val borderColor by animateColorAsState(
                targetValue = if (selected) ViceFlamingo else Color.White.copy(alpha = 0.14f),
                label = "modeBorder",
              )
              Surface(
                modifier = Modifier
                  .weight(1f)
                  .height(48.dp)
                  .clip(RoundedCornerShape(14.dp))
                  .clickable {
                    if (entry == CountdownMode.CUSTOM) {
                      onOpenCustomPicker()
                    } else {
                      onSelectMode(entry)
                    }
                  }
                  .testTag("mode_${entry.name.lowercase(Locale.US)}"),
                shape = RoundedCornerShape(14.dp),
                color = bgColor,
                border = BorderStroke(1.dp, borderColor),
              ) {
                Box(
                  contentAlignment = Alignment.Center,
                  modifier = Modifier.padding(horizontal = 6.dp),
                ) {
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                  ) {
                    Icon(
                      imageVector = if (entry == CountdownMode.CUSTOM) Icons.Rounded.EditCalendar else Icons.Rounded.Public,
                      contentDescription = null,
                      tint = if (selected) ViceFlamingo else Color(0xFFB5B0D1),
                      modifier = Modifier.size(14.dp),
                    )
                    Text(
                      text = strings.modeShort(entry),
                      style = MaterialTheme.typography.labelLarge.copy(fontSize = 12.sp),
                      color = if (selected) Color.White else Color(0xFFB5B0D1),
                      maxLines = 1,
                      overflow = TextOverflow.Ellipsis,
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
}

@Composable
private fun CountdownDigitTile(
  value: String,
  label: String,
  accent: Color,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier
      .clip(RoundedCornerShape(16.dp))
      .background(Color(0xFF171429))
      .border(1.dp, accent.copy(alpha = 0.45f), RoundedCornerShape(16.dp))
      .padding(vertical = 12.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Text(
      text = value,
      style = MaterialTheme.typography.displayMedium.copy(
        fontFamily = JetBrainsMonoFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        fontFeatureSettings = "tnum",
      ),
      color = Color.White,
    )
    Spacer(modifier = Modifier.height(2.dp))
    Text(
      text = label,
      style = MaterialTheme.typography.labelSmall,
      color = accent,
      fontWeight = FontWeight.Bold,
    )
  }
}
