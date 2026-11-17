package com.example.model

import java.time.Instant

enum class DayPhase {
  DAWN,
  DAY,
  SUNSET,
  NIGHT,
}

enum class CountdownMode(val label: String, val subtitle: String, val zoneId: String?) {
  AUCKLAND("Auckland (First)", "First Global Midnight • UTC+13", "Pacific/Auckland"),
  VICE_CITY("Vice City (EST)", "Miami / Rockstar HQ • UTC-5", "America/New_York"),
  LOCAL("Your Local Time", "Device Local Midnight", null),
  CUSTOM("Custom Target", "Your Custom Launch Date", null),
}

data class CatalogCity(
  val zoneId: String,
  val city: String,
  val country: String,
  val badge: String? = null,
)

data class CountdownBreakdown(
  val days: Long,
  val hours: Long,
  val minutes: Long,
  val seconds: Long,
  val totalRemainingMillis: Long,
  val isUnlocked: Boolean,
  val progressFraction: Float,
  val targetInstant: Instant,
  val targetFormatted: String,
)

data class WorldClockUiItem(
  val zoneId: String,
  val cityName: String,
  val countryOrRegion: String,
  val formattedTime: String,
  val formattedAmPm: String,
  val formattedSeconds: String,
  val formattedDate: String,
  val utcOffsetLabel: String,
  val relativeOffsetLabel: String,
  val dayPhase: DayPhase,
  val isLocal: Boolean,
  val gtaUnlockLabel: String,
  val isGtaUnlockedInZone: Boolean,
  val badge: String? = null,
)

data class LaunchWaveItem(
  val cityName: String,
  val regionLabel: String,
  val zoneId: String,
  val unlockLocalTime: String,
  val unlockInYourTime: String,
  val countdownShort: String,
  val isUnlocked: Boolean,
)
