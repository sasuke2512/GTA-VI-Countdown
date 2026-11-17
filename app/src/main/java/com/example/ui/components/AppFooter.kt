package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LocalAppStrings

private const val CREATOR_X_URL = "https://x.com/saske2512"
private val HeartRed = Color(0xFFFF2E4C)

@Composable
fun AppFooter(modifier: Modifier = Modifier) {
  val uriHandler = LocalUriHandler.current
  val strings = LocalAppStrings.current

  Surface(
    modifier = modifier
      .fillMaxWidth()
      .testTag("app_footer"),
    shape = RoundedCornerShape(20.dp),
    color = MaterialTheme.colorScheme.surfaceContainerLow,
    tonalElevation = 1.dp,
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 14.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
      Row(
        modifier = Modifier
          .clip(RoundedCornerShape(12.dp))
          .clickable(
            role = Role.Button,
            onClickLabel = "Open SaSuKe2512 X profile",
          ) {
            runCatching { uriHandler.openUri(CREATOR_X_URL) }
          }
          .defaultMinSize(minHeight = 48.dp)
          .padding(horizontal = 10.dp, vertical = 6.dp)
          .testTag("footer_creator_link"),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
      ) {
        Text(
          text = buildAnnotatedString {
            append(strings.footerCreatedWith)
            withStyle(SpanStyle(color = HeartRed, fontWeight = FontWeight.Bold)) {
              append("♥")
            }
            append(strings.footerBy)
            withStyle(
              SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline,
              ),
            ) {
              append("SaSuKe2512")
            }
            append(" ")
            withStyle(
              SpanStyle(
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.SemiBold,
              ),
            ) {
              append("(@saske2512)")
            }
          },
          style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
          color = MaterialTheme.colorScheme.onSurface,
          textAlign = TextAlign.Center,
        )
      }

      HorizontalDivider(
        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f),
        modifier = Modifier.padding(horizontal = 12.dp),
      )

      Spacer(modifier = Modifier.height(2.dp))

      Text(
        text = strings.footerCopyrightTrademarks,
        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 11.5.sp, lineHeight = 16.sp),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
        modifier = Modifier.testTag("footer_copyright_trademarks"),
      )

      Text(
        text = strings.footerNotAffiliated,
        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp, lineHeight = 15.sp),
        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f),
        textAlign = TextAlign.Center,
        modifier = Modifier.testTag("footer_disclaimer"),
      )
    }
  }
}
