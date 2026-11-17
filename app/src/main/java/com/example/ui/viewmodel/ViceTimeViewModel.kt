package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.CityCatalog
import com.example.data.UserTimePreferences
import com.example.data.ViceTimePreferencesRepository
import com.example.model.CatalogCity
import com.example.model.CountdownBreakdown
import com.example.model.CountdownMode
import com.example.model.DayPhase
import com.example.model.LaunchWaveItem
import com.example.model.WorldClockUiItem
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ViceTimeUiState(
  val is24HourFormat: Boolean = false,
  val isDarkTheme: Boolean = true,
  val scrubberOffsetMinutes: Int = 0,
  val countdownMode: CountdownMode = CountdownMode.VICE_CITY,
  val countdown: CountdownBreakdown? = null,
  val pinnedClocks: List<WorldClockUiItem> = emptyList(),
  val launchWaves: List<LaunchWaveItem> = emptyList(),
  val searchQuery: String = "",
  val catalogResults: List<CatalogCityPreview> = emptyList(),
  val pinnedZoneSet: Set<String> = emptySet(),
  val localZoneName: String = ZoneId.systemDefault().id,
  val localFormattedTime: String = "",
)

data class CatalogCityPreview(
  val catalogCity: CatalogCity,
  val previewTime: String,
  val utcOffset: String,
  val isPinned: Boolean,
)

class ViceTimeViewModel(application: Application) : AndroidViewModel(application) {

  private val repository = ViceTimePreferencesRepository(application.applicationContext)

  private val scrubberOffsetMinutes = MutableStateFlow(0)
  private val searchQuery = MutableStateFlow("")

  private val tickerFlow = flow {
    while (true) {
      emit(Instant.now())
      val millisIntoSecond = System.currentTimeMillis() % 1000L
      delay((1000L - millisIntoSecond).coerceIn(100L, 1000L))
    }
  }

  val uiState: StateFlow<ViceTimeUiState> = combine(
    tickerFlow,
    repository.preferencesFlow,
    scrubberOffsetMinutes,
    searchQuery,
  ) { now, prefs, offsetMinutes, query ->
    buildUiState(now, prefs, offsetMinutes, query)
  }.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5_000),
    initialValue = ViceTimeUiState(),
  )

  fun toggle24HourFormat() {
    val current = uiState.value.is24HourFormat
    viewModelScope.launch { repository.set24HourFormat(!current) }
  }

  fun toggleDarkTheme() {
    val current = uiState.value.isDarkTheme
    viewModelScope.launch { repository.setDarkTheme(!current) }
  }

  fun toggleZonePinned(zoneId: String) {
    viewModelScope.launch { repository.toggleZonePinned(zoneId) }
  }

  fun removeZone(zoneId: String) {
    viewModelScope.launch { repository.removeZone(zoneId) }
  }

  fun promoteZoneToTop(zoneId: String) {
    viewModelScope.launch { repository.promoteZoneToTop(zoneId) }
  }

  fun setCountdownMode(mode: CountdownMode) {
    viewModelScope.launch { repository.setCountdownMode(mode) }
  }

  fun setCustomTarget(epochMillis: Long) {
    viewModelScope.launch { repository.setCustomTargetEpoch(epochMillis) }
  }

  fun setScrubberOffsetMinutes(minutes: Int) {
    scrubberOffsetMinutes.value = minutes.coerceIn(-720, 1440)
  }

  fun resetScrubber() {
    scrubberOffsetMinutes.value = 0
  }

  fun updateSearchQuery(query: String) {
    searchQuery.value = query
  }

  private fun buildUiState(
    realNow: Instant,
    prefs: UserTimePreferences,
    offsetMinutes: Int,
    query: String,
  ): ViceTimeUiState {
    val effectiveNow = realNow.plusSeconds(offsetMinutes * 60L)
    val localZone = ZoneId.systemDefault()
    val localZoned = effectiveNow.atZone(localZone)

    val countdown = computeCountdown(realNow, prefs, localZone)
    val pinnedClocks = prefs.pinnedZoneIds.map { zoneId ->
      buildWorldClockItem(
        zoneIdStr = zoneId,
        effectiveNow = effectiveNow,
        realNow = realNow,
        localZone = localZone,
        localDate = localZoned.toLocalDate(),
        is24Hour = prefs.is24HourFormat,
      )
    }

    val waves = CityCatalog.LAUNCH_WAVE_ZONES.map { city ->
      buildLaunchWaveItem(city, realNow, localZone, prefs.is24HourFormat)
    }

    val trimmedQuery = query.trim()
    val filteredCatalog = if (trimmedQuery.isEmpty()) {
      CityCatalog.CURATED_CITIES
    } else {
      CityCatalog.ALL_CATALOG_CITIES.filter {
        it.city.contains(trimmedQuery, ignoreCase = true) ||
          it.country.contains(trimmedQuery, ignoreCase = true) ||
          it.zoneId.contains(trimmedQuery, ignoreCase = true)
      }.take(60)
    }

    val pinnedSet = prefs.pinnedZoneIds.toSet()
    val previews = filteredCatalog.map { catalogCity ->
      val zdt = effectiveNow.atZone(ZoneId.of(catalogCity.zoneId))
      CatalogCityPreview(
        catalogCity = catalogCity,
        previewTime = formatClockMain(zdt, prefs.is24HourFormat) +
          if (prefs.is24HourFormat) "" else " ${zdt.format(AM_PM_FMT)}",
        utcOffset = formatUtcOffset(zdt),
        isPinned = catalogCity.zoneId in pinnedSet,
      )
    }

    val localCatalog = CityCatalog.resolveCity(localZone.id)
    val localTimeText = formatClockMain(localZoned, prefs.is24HourFormat) +
      if (prefs.is24HourFormat) "" else " ${localZoned.format(AM_PM_FMT)}"

    return ViceTimeUiState(
      is24HourFormat = prefs.is24HourFormat,
      isDarkTheme = prefs.isDarkTheme,
      scrubberOffsetMinutes = offsetMinutes,
      countdownMode = prefs.countdownMode,
      countdown = countdown,
      pinnedClocks = pinnedClocks,
      launchWaves = waves,
      searchQuery = query,
      catalogResults = previews,
      pinnedZoneSet = pinnedSet,
      localZoneName = localCatalog.city,
      localFormattedTime = localTimeText,
    )
  }

  private fun computeCountdown(
    now: Instant,
    prefs: UserTimePreferences,
    localZone: ZoneId,
  ): CountdownBreakdown {
    val targetInstant = when (prefs.countdownMode) {
      CountdownMode.AUCKLAND ->
        CityCatalog.GTA_VI_LAUNCH_LOCAL.atZone(ZoneId.of("Pacific/Auckland")).toInstant()
      CountdownMode.VICE_CITY ->
        CityCatalog.GTA_VI_LAUNCH_LOCAL.atZone(ZoneId.of("America/New_York")).toInstant()
      CountdownMode.LOCAL ->
        CityCatalog.GTA_VI_LAUNCH_LOCAL.atZone(localZone).toInstant()
      CountdownMode.CUSTOM -> {
        val saved = prefs.customTargetEpochMillis
        if (saved != null) {
          Instant.ofEpochMilli(saved)
        } else {
          CityCatalog.GTA_VI_LAUNCH_LOCAL.atZone(ZoneId.of("America/New_York")).toInstant()
        }
      }
    }

    val remainingMillis = Duration.between(now, targetInstant).toMillis()
    val isUnlocked = remainingMillis <= 0L
    val clampedSeconds = if (isUnlocked) 0L else remainingMillis / 1000L

    val days = clampedSeconds / 86_400L
    val hours = (clampedSeconds % 86_400L) / 3_600L
    val minutes = (clampedSeconds % 3_600L) / 60L
    val seconds = clampedSeconds % 60L

    val campaignStart = CityCatalog.CAMPAIGN_START_LOCAL.atZone(localZone).toInstant()
    val totalWindow = Duration.between(campaignStart, targetInstant).toMillis().coerceAtLeast(1L)
    val elapsed = Duration.between(campaignStart, now).toMillis().coerceIn(0L, totalWindow)
    val progress = if (isUnlocked) 1f else (elapsed.toFloat() / totalWindow.toFloat()).coerceIn(0f, 1f)

    val targetDisplayZone = prefs.countdownMode.zoneId?.let { ZoneId.of(it) } ?: localZone
    val targetFormatted = targetInstant.atZone(targetDisplayZone)
      .format(DateTimeFormatter.ofPattern("EEE, MMM d, yyyy • HH:mm z", Locale.US))

    return CountdownBreakdown(
      days = days,
      hours = hours,
      minutes = minutes,
      seconds = seconds,
      totalRemainingMillis = remainingMillis.coerceAtLeast(0L),
      isUnlocked = isUnlocked,
      progressFraction = progress,
      targetInstant = targetInstant,
      targetFormatted = targetFormatted,
    )
  }

  private fun buildWorldClockItem(
    zoneIdStr: String,
    effectiveNow: Instant,
    realNow: Instant,
    localZone: ZoneId,
    localDate: LocalDate,
    is24Hour: Boolean,
  ): WorldClockUiItem {
    val zone = ZoneId.of(zoneIdStr)
    val zdt = effectiveNow.atZone(zone)
    val localZdt = effectiveNow.atZone(localZone)
    val cityInfo = CityCatalog.resolveCity(zoneIdStr)

    val diffSeconds = zdt.offset.totalSeconds - localZdt.offset.totalSeconds
    val diffHours = diffSeconds / 3600
    val diffMins = (kotlin.math.abs(diffSeconds) % 3600) / 60

    val dayDelta = zdt.toLocalDate().toEpochDay() - localDate.toEpochDay()
    val dayWord = when {
      dayDelta > 0 -> "Tomorrow"
      dayDelta < 0 -> "Yesterday"
      else -> "Today"
    }

    val diffLabel = when {
      zoneIdStr == localZone.id -> "$dayWord • Local"
      diffSeconds == 0 -> "$dayWord • Same time"
      diffMins == 0 -> {
        val sign = if (diffHours > 0) "+" else ""
        "$dayWord • ${sign}${diffHours}h"
      }
      else -> {
        val sign = if (diffSeconds > 0) "+" else "-"
        "$dayWord • $sign${kotlin.math.abs(diffHours)}h ${diffMins}m"
      }
    }

    val zoneUnlockInstant = CityCatalog.GTA_VI_LAUNCH_LOCAL.atZone(zone).toInstant()
    val unlockRemMillis = Duration.between(realNow, zoneUnlockInstant).toMillis()
    val isGtaUnlocked = unlockRemMillis <= 0L
    val gtaLabel = if (isGtaUnlocked) {
      "GTA VI Live"
    } else {
      val remSec = unlockRemMillis / 1000L
      val d = remSec / 86_400L
      val h = (remSec % 86_400L) / 3_600L
      val m = (remSec % 3_600L) / 60L
      when {
        d > 0 -> "GTA VI in ${d}d ${h}h"
        h > 0 -> "GTA VI in ${h}h ${m}m"
        else -> "GTA VI in ${m}m"
      }
    }

    val hour = zdt.hour
    val dayPhase = when (hour) {
      in 5..7 -> DayPhase.DAWN
      in 8..16 -> DayPhase.DAY
      in 17..19 -> DayPhase.SUNSET
      else -> DayPhase.NIGHT
    }

    return WorldClockUiItem(
      zoneId = zoneIdStr,
      cityName = cityInfo.city,
      countryOrRegion = cityInfo.country,
      formattedTime = formatClockMain(zdt, is24Hour),
      formattedAmPm = if (is24Hour) "" else zdt.format(AM_PM_FMT),
      formattedSeconds = String.format(Locale.US, "%02d", zdt.second),
      formattedDate = zdt.format(DATE_FMT),
      utcOffsetLabel = formatUtcOffset(zdt),
      relativeOffsetLabel = diffLabel,
      dayPhase = dayPhase,
      isLocal = zoneIdStr == localZone.id,
      gtaUnlockLabel = gtaLabel,
      isGtaUnlockedInZone = isGtaUnlocked,
      badge = cityInfo.badge,
    )
  }

  private fun buildLaunchWaveItem(
    city: CatalogCity,
    now: Instant,
    localZone: ZoneId,
    is24Hour: Boolean,
  ): LaunchWaveItem {
    val zone = ZoneId.of(city.zoneId)
    val unlockInstant = CityCatalog.GTA_VI_LAUNCH_LOCAL.atZone(zone).toInstant()
    val unlockInLocal = unlockInstant.atZone(localZone)
    val remMillis = Duration.between(now, unlockInstant).toMillis()
    val isUnlocked = remMillis <= 0L

    val countdownShort = if (isUnlocked) {
      "UNLOCKED"
    } else {
      val sec = remMillis / 1000L
      val d = sec / 86_400L
      val h = (sec % 86_400L) / 3_600L
      val m = (sec % 3_600L) / 60L
      val s = sec % 60L
      if (d > 0) "${d}d ${h}h ${m}m" else String.format(Locale.US, "%02dh %02dm %02ds", h, m, s)
    }

    val localPattern = if (is24Hour) "MMM d, HH:mm" else "MMM d, h:mm a"
    val yourTimeStr = unlockInLocal.format(DateTimeFormatter.ofPattern(localPattern, Locale.US))

    return LaunchWaveItem(
      cityName = city.city,
      regionLabel = city.country,
      zoneId = city.zoneId,
      unlockLocalTime = "Nov 19, 00:00",
      unlockInYourTime = "Your time: $yourTimeStr",
      countdownShort = countdownShort,
      isUnlocked = isUnlocked,
    )
  }

  private fun formatClockMain(zdt: ZonedDateTime, is24Hour: Boolean): String {
    return zdt.format(if (is24Hour) TIME_24_FMT else TIME_12_FMT)
  }

  private fun formatUtcOffset(zdt: ZonedDateTime): String {
    val id = zdt.offset.id
    return if (id == "Z") "UTC+0" else "UTC$id"
  }

  companion object {
    private val TIME_24_FMT = DateTimeFormatter.ofPattern("HH:mm", Locale.US)
    private val TIME_12_FMT = DateTimeFormatter.ofPattern("h:mm", Locale.US)
    private val AM_PM_FMT = DateTimeFormatter.ofPattern("a", Locale.US)
    private val DATE_FMT = DateTimeFormatter.ofPattern("EEE, MMM d", Locale.US)
  }
}
