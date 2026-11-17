package com.example.data

import com.example.model.CatalogCity
import java.time.LocalDateTime
import java.time.ZoneId

object CityCatalog {

  // Official GTA VI Console Launch: November 19, 2026 at 00:00:00 local midnight
  val GTA_VI_LAUNCH_LOCAL: LocalDateTime = LocalDateTime.of(2026, 11, 19, 0, 0, 0)

  // Countdown campaign start baseline (May 2, 2025 announcement / 566-day radar window)
  val CAMPAIGN_START_LOCAL: LocalDateTime = LocalDateTime.of(2026, 1, 1, 0, 0, 0)

  val DEFAULT_PINNED_ZONES: List<String> = listOf(
    "America/New_York",
    "Pacific/Auckland",
    "Europe/London",
    "Asia/Tokyo",
    "Asia/Kolkata",
    "America/Los_Angeles",
  )

  val CURATED_CITIES: List<CatalogCity> = listOf(
    CatalogCity("Pacific/Auckland", "Auckland", "New Zealand", badge = "1st Wave"),
    CatalogCity("Australia/Sydney", "Sydney", "Australia", badge = "Early"),
    CatalogCity("Asia/Tokyo", "Tokyo", "Japan"),
    CatalogCity("Asia/Seoul", "Seoul", "South Korea"),
    CatalogCity("Asia/Singapore", "Singapore", "Singapore"),
    CatalogCity("Asia/Hong_Kong", "Hong Kong", "China"),
    CatalogCity("Asia/Shanghai", "Shanghai", "China"),
    CatalogCity("Asia/Bangkok", "Bangkok", "Thailand"),
    CatalogCity("Asia/Kolkata", "Mumbai & Delhi", "India"),
    CatalogCity("Asia/Dubai", "Dubai", "United Arab Emirates"),
    CatalogCity("Asia/Riyadh", "Riyadh", "Saudi Arabia"),
    CatalogCity("Europe/Istanbul", "Istanbul", "Türkiye"),
    CatalogCity("Africa/Cairo", "Cairo", "Egypt"),
    CatalogCity("Africa/Johannesburg", "Johannesburg", "South Africa"),
    CatalogCity("Africa/Lagos", "Lagos", "Nigeria"),
    CatalogCity("Europe/Athens", "Athens", "Greece"),
    CatalogCity("Europe/Berlin", "Berlin", "Germany"),
    CatalogCity("Europe/Paris", "Paris", "France"),
    CatalogCity("Europe/Madrid", "Madrid", "Spain"),
    CatalogCity("Europe/Rome", "Rome", "Italy"),
    CatalogCity("Europe/Amsterdam", "Amsterdam", "Netherlands"),
    CatalogCity("Europe/Zurich", "Zurich", "Switzerland"),
    CatalogCity("Europe/Stockholm", "Stockholm", "Sweden"),
    CatalogCity("Europe/Warsaw", "Warsaw", "Poland"),
    CatalogCity("Europe/London", "London", "United Kingdom"),
    CatalogCity("Europe/Dublin", "Dublin", "Ireland"),
    CatalogCity("Atlantic/Reykjavik", "Reykjavik", "Iceland"),
    CatalogCity("America/Sao_Paulo", "São Paulo", "Brazil"),
    CatalogCity("America/Argentina/Buenos_Aires", "Buenos Aires", "Argentina"),
    CatalogCity("America/Santiago", "Santiago", "Chile"),
    CatalogCity("America/Bogota", "Bogotá", "Colombia"),
    CatalogCity("America/New_York", "Miami / New York", "USA (Vice City)", badge = "Vice City"),
    CatalogCity("America/Toronto", "Toronto", "Canada"),
    CatalogCity("America/Chicago", "Chicago", "USA (Central)"),
    CatalogCity("America/Mexico_City", "Mexico City", "Mexico"),
    CatalogCity("America/Denver", "Denver", "USA (Mountain)"),
    CatalogCity("America/Phoenix", "Phoenix", "USA (Arizona)"),
    CatalogCity("America/Los_Angeles", "Los Angeles", "USA (Pacific)"),
    CatalogCity("America/Vancouver", "Vancouver", "Canada"),
    CatalogCity("America/Anchorage", "Anchorage", "USA (Alaska)"),
    CatalogCity("Pacific/Honolulu", "Honolulu", "USA (Hawaii)"),
    CatalogCity("UTC", "UTC", "Coordinated Universal Time"),
  )

  private val curatedByZone: Map<String, CatalogCity> = CURATED_CITIES.associateBy { it.zoneId }

  val LAUNCH_WAVE_ZONES: List<CatalogCity> = listOf(
    CatalogCity("Pacific/Auckland", "Auckland", "New Zealand", badge = "1st"),
    CatalogCity("Australia/Sydney", "Sydney", "Australia"),
    CatalogCity("Asia/Tokyo", "Tokyo", "Japan"),
    CatalogCity("Asia/Kolkata", "Mumbai", "India"),
    CatalogCity("Asia/Dubai", "Dubai", "UAE"),
    CatalogCity("Europe/Paris", "Paris", "France"),
    CatalogCity("Europe/London", "London", "United Kingdom"),
    CatalogCity("America/Sao_Paulo", "São Paulo", "Brazil"),
    CatalogCity("America/New_York", "Vice City (Miami)", "USA East", badge = "HQ"),
    CatalogCity("America/Los_Angeles", "Los Santos (LA)", "USA West"),
  )

  val ALL_CATALOG_CITIES: List<CatalogCity> by lazy {
    val curatedIds = curatedByZone.keys
    val remaining = ZoneId.getAvailableZoneIds()
      .asSequence()
      .filter { it !in curatedIds && !it.startsWith("Etc/GMT") && !it.startsWith("SystemV") }
      .sorted()
      .map { id ->
        val parts = id.split("/")
        val city = parts.last().replace("_", " ")
        val region = if (parts.size > 1) parts.dropLast(1).joinToString(" / ").replace("_", " ") else "Global"
        CatalogCity(zoneId = id, city = city, country = region)
      }
      .toList()
    CURATED_CITIES + remaining
  }

  fun resolveCity(zoneId: String): CatalogCity {
    curatedByZone[zoneId]?.let { return it }
    val parts = zoneId.split("/")
    val city = parts.last().replace("_", " ")
    val region = if (parts.size > 1) parts.dropLast(1).joinToString(" / ").replace("_", " ") else "Global"
    return CatalogCity(zoneId = zoneId, city = city, country = region)
  }
}
