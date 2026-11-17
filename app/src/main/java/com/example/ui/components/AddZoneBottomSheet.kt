package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.viewmodel.CatalogCityPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddZoneBottomSheet(
  searchQuery: String,
  results: List<CatalogCityPreview>,
  onSearchQueryChange: (String) -> Unit,
  onToggleZone: (String) -> Unit,
  onDismiss: () -> Unit,
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = MaterialTheme.colorScheme.surface,
    contentColor = MaterialTheme.colorScheme.onSurface,
    modifier = Modifier.testTag("add_zone_bottom_sheet"),
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp),
    ) {
      Text(
        text = "Add World Time Zone",
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.onSurface,
      )
      Text(
        text = "Search 590+ global IANA time zones and cities",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )

      Spacer(modifier = Modifier.height(14.dp))

      OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchQueryChange,
        placeholder = { Text("Search city, country, or zone…") },
        leadingIcon = {
          Icon(
            imageVector = Icons.Rounded.Search,
            contentDescription = "Search zones",
            tint = MaterialTheme.colorScheme.secondary,
          )
        },
        trailingIcon = {
          if (searchQuery.isNotEmpty()) {
            IconButton(
              onClick = { onSearchQueryChange("") },
              modifier = Modifier.size(48.dp),
            ) {
              Icon(
                imageVector = Icons.Rounded.Close,
                contentDescription = "Clear search",
              )
            }
          }
        },
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = MaterialTheme.colorScheme.secondary,
          unfocusedBorderColor = MaterialTheme.colorScheme.outline,
          focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
          unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("zone_search_input"),
      )

      Spacer(modifier = Modifier.height(14.dp))

      LazyColumn(
        contentPadding = PaddingValues(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth(),
      ) {
        items(results, key = { it.catalogCity.zoneId }) { preview ->
          val city = preview.catalogCity
          Surface(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(16.dp))
              .clickable { onToggleZone(city.zoneId) }
              .testTag("catalog_zone_${city.zoneId}"),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surfaceContainer,
            tonalElevation = 1.dp,
            border = BorderStroke(
              1.dp,
              if (preview.isPinned) {
                MaterialTheme.colorScheme.secondary.copy(alpha = 0.55f)
              } else {
                MaterialTheme.colorScheme.outlineVariant
              },
            ),
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically,
            ) {
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = city.city,
                  style = MaterialTheme.typography.titleMedium,
                  color = MaterialTheme.colorScheme.onSurface,
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis,
                )
                Text(
                  text = "${city.country} • ${preview.utcOffset}",
                  style = MaterialTheme.typography.bodyMedium,
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis,
                )
              }

              Spacer(modifier = Modifier.width(12.dp))

              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
              ) {
                Text(
                  text = preview.previewTime,
                  style = MaterialTheme.typography.titleMedium.copy(
                    fontFamily = JetBrainsMonoFontFamily,
                    fontFeatureSettings = "tnum",
                  ),
                  color = MaterialTheme.colorScheme.secondary,
                )

                Surface(
                  shape = RoundedCornerShape(50),
                  color = if (preview.isPinned) {
                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.18f)
                  } else {
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.18f)
                  },
                  modifier = Modifier.size(38.dp),
                ) {
                  Icon(
                    imageVector = if (preview.isPinned) Icons.Rounded.Check else Icons.Rounded.Add,
                    contentDescription = if (preview.isPinned) "Pinned" else "Pin city",
                    tint = if (preview.isPinned) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(8.dp),
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
