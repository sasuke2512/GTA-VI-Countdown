package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material.icons.rounded.Public
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.LocalAppStrings
import com.example.data.stringsFor
import com.example.ui.components.AddZoneBottomSheet
import com.example.ui.components.CustomTargetDialog
import com.example.ui.screens.GtaCountdownScreen
import com.example.ui.screens.WorldZonesScreen
import com.example.ui.theme.ViceTimeTheme
import com.example.ui.viewmodel.ViceTimeViewModel

enum class ViceTab {
  WORLD_ZONES,
  GTA_COUNTDOWN,
}

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge(
      statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
      navigationBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
    )
    setContent {
      val viewModel: ViceTimeViewModel = viewModel()
      val uiState by viewModel.uiState.collectAsStateWithLifecycle()
      val appStrings = remember(uiState.selectedLanguage) { stringsFor(uiState.selectedLanguage) }

      LaunchedEffect(uiState.isDarkTheme) {
        val barStyle = if (uiState.isDarkTheme) {
          SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
        } else {
          SystemBarStyle.light(
            android.graphics.Color.TRANSPARENT,
            android.graphics.Color.TRANSPARENT,
          )
        }
        enableEdgeToEdge(
          statusBarStyle = barStyle,
          navigationBarStyle = barStyle,
        )
      }

      CompositionLocalProvider(LocalAppStrings provides appStrings) {
        ViceTimeTheme(darkTheme = uiState.isDarkTheme) {
          ViceTimeApp(viewModel = viewModel)
        }
      }
    }
  }
}

@Composable
fun ViceTimeApp(viewModel: ViceTimeViewModel = viewModel()) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val strings = LocalAppStrings.current
  var currentTab by rememberSaveable { mutableStateOf(ViceTab.WORLD_ZONES) }
  var showAddZoneSheet by rememberSaveable { mutableStateOf(false) }
  var showCustomTargetDialog by rememberSaveable { mutableStateOf(false) }

  if (currentTab == ViceTab.GTA_COUNTDOWN && !showAddZoneSheet && !showCustomTargetDialog) {
    BackHandler { currentTab = ViceTab.WORLD_ZONES }
  }

  BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
    val useRail = maxWidth >= 600.dp

    Scaffold(
      modifier = Modifier.fillMaxSize(),
      containerColor = MaterialTheme.colorScheme.background,
      contentWindowInsets = WindowInsets.safeDrawing,
      bottomBar = {
        if (!useRail) {
          NavigationBar(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            contentColor = MaterialTheme.colorScheme.onSurface,
          ) {
            ViceTab.entries.forEach { tab ->
              val selected = tab == currentTab
              val label = when (tab) {
                ViceTab.WORLD_ZONES -> strings.navWorldZones
                ViceTab.GTA_COUNTDOWN -> strings.navGtaCountdown
              }
              val activeColor = if (tab == ViceTab.GTA_COUNTDOWN) {
                MaterialTheme.colorScheme.primary
              } else {
                MaterialTheme.colorScheme.secondary
              }
              NavigationBarItem(
                selected = selected,
                onClick = { currentTab = tab },
                icon = {
                  Icon(
                    imageVector = when (tab) {
                      ViceTab.WORLD_ZONES -> if (selected) Icons.Rounded.Public else Icons.Outlined.Public
                      ViceTab.GTA_COUNTDOWN -> if (selected) Icons.Rounded.Timer else Icons.Outlined.Timer
                    },
                    contentDescription = label,
                  )
                },
                label = { Text(label) },
                colors = NavigationBarItemDefaults.colors(
                  selectedIconColor = activeColor,
                  selectedTextColor = MaterialTheme.colorScheme.onSurface,
                  indicatorColor = activeColor.copy(alpha = 0.18f),
                  unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                  unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
                modifier = Modifier.testTag("nav_${tab.name.lowercase()}"),
              )
            }
          }
        }
      },
    ) { innerPadding ->
      Row(
        modifier = Modifier
          .fillMaxSize()
          .padding(innerPadding),
      ) {
        if (useRail) {
          NavigationRail(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            modifier = Modifier.windowInsetsPadding(WindowInsets(0, 0, 0, 0)),
          ) {
            ViceTab.entries.forEach { tab ->
              val selected = tab == currentTab
              val label = when (tab) {
                ViceTab.WORLD_ZONES -> strings.navWorldZones
                ViceTab.GTA_COUNTDOWN -> strings.navGtaCountdown
              }
              val activeColor = if (tab == ViceTab.GTA_COUNTDOWN) {
                MaterialTheme.colorScheme.primary
              } else {
                MaterialTheme.colorScheme.secondary
              }
              NavigationRailItem(
                selected = selected,
                onClick = { currentTab = tab },
                icon = {
                  Icon(
                    imageVector = when (tab) {
                      ViceTab.WORLD_ZONES -> if (selected) Icons.Rounded.Public else Icons.Outlined.Public
                      ViceTab.GTA_COUNTDOWN -> if (selected) Icons.Rounded.Timer else Icons.Outlined.Timer
                    },
                    contentDescription = label,
                  )
                },
                label = { Text(label) },
                colors = NavigationRailItemDefaults.colors(
                  selectedIconColor = activeColor,
                  selectedTextColor = MaterialTheme.colorScheme.onSurface,
                  indicatorColor = activeColor.copy(alpha = 0.18f),
                  unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                  unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
                modifier = Modifier.testTag("rail_${tab.name.lowercase()}"),
              )
            }
          }
        }

        Box(modifier = Modifier.weight(1f)) {
          when (currentTab) {
            ViceTab.WORLD_ZONES -> {
              WorldZonesScreen(
                uiState = uiState,
                onToggle24Hour = viewModel::toggle24HourFormat,
                onToggleTheme = viewModel::toggleDarkTheme,
                onSelectLanguage = viewModel::setLanguage,
                onOpenAddZone = { showAddZoneSheet = true },
                onOpenCountdownTab = { currentTab = ViceTab.GTA_COUNTDOWN },
                onScrubberChange = viewModel::setScrubberOffsetMinutes,
                onResetScrubber = viewModel::resetScrubber,
                onPromoteZone = viewModel::promoteZoneToTop,
                onRemoveZone = viewModel::removeZone,
              )
            }

            ViceTab.GTA_COUNTDOWN -> {
              GtaCountdownScreen(
                uiState = uiState,
                onToggleTheme = viewModel::toggleDarkTheme,
                onSelectLanguage = viewModel::setLanguage,
                onSelectMode = viewModel::setCountdownMode,
                onOpenCustomPicker = { showCustomTargetDialog = true },
                onPinWaveCity = viewModel::toggleZonePinned,
              )
            }
          }
        }
      }
    }
  }

  if (showAddZoneSheet) {
    AddZoneBottomSheet(
      searchQuery = uiState.searchQuery,
      results = uiState.catalogResults,
      onSearchQueryChange = viewModel::updateSearchQuery,
      onToggleZone = viewModel::toggleZonePinned,
      onDismiss = {
        showAddZoneSheet = false
        viewModel.updateSearchQuery("")
      },
    )
  }

  if (showCustomTargetDialog) {
    CustomTargetDialog(
      onConfirmEpochMillis = viewModel::setCustomTarget,
      onDismiss = { showCustomTargetDialog = false },
    )
  }
}
