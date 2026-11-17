package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LocalAppStrings
import com.example.model.AppLanguage

@Composable
fun LanguageFlagSelector(
  selectedLanguage: AppLanguage,
  onSelectLanguage: (AppLanguage) -> Unit,
  modifier: Modifier = Modifier,
  testTag: String = "language_selector_button",
) {
  val strings = LocalAppStrings.current
  var expanded by remember { mutableStateOf(false) }

  val effectiveSystemLang = AppLanguage.resolveEffectiveLanguage(AppLanguage.SYSTEM)
  val activeFlag = if (selectedLanguage == AppLanguage.SYSTEM) {
    effectiveSystemLang.flag
  } else {
    selectedLanguage.flag
  }

  Box(modifier = modifier) {
    Surface(
      modifier = Modifier
        .height(48.dp)
        .clip(RoundedCornerShape(16.dp))
        .clickable { expanded = true }
        .semantics { contentDescription = strings.languageSelectorDesc }
        .testTag(testTag),
      shape = RoundedCornerShape(16.dp),
      color = MaterialTheme.colorScheme.surfaceContainer,
      tonalElevation = 2.dp,
      border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    ) {
      Row(
        modifier = Modifier.padding(start = 12.dp, end = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
      ) {
        Text(
          text = activeFlag,
          fontSize = 19.sp,
        )
        Icon(
          imageVector = Icons.Rounded.ArrowDropDown,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.size(20.dp),
        )
      }
    }

    DropdownMenu(
      expanded = expanded,
      onDismissRequest = { expanded = false },
      containerColor = MaterialTheme.colorScheme.surfaceContainer,
    ) {
      AppLanguage.entries.forEach { lang ->
        val isSelected = lang == selectedLanguage
        val label = if (lang == AppLanguage.SYSTEM) {
          strings.languageSystemOption("${effectiveSystemLang.flag} ${effectiveSystemLang.nativeName}")
        } else {
          lang.nativeName
        }

        DropdownMenuItem(
          text = {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
              Text(text = lang.flag, fontSize = 18.sp)
              Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) {
                  MaterialTheme.colorScheme.primary
                } else {
                  MaterialTheme.colorScheme.onSurface
                },
              )
            }
          },
          trailingIcon = {
            if (isSelected) {
              Icon(
                imageVector = Icons.Rounded.Check,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp),
              )
            }
          },
          onClick = {
            onSelectLanguage(lang)
            expanded = false
          },
          modifier = Modifier.testTag("language_option_${lang.code}"),
        )
      }
    }
  }
}
