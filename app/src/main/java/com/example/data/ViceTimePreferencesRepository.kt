package com.example.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.model.AppLanguage
import com.example.model.CountdownMode
import java.time.ZoneId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "vicetime_prefs")

data class UserTimePreferences(
  val pinnedZoneIds: List<String>,
  val is24HourFormat: Boolean,
  val isDarkTheme: Boolean,
  val selectedLanguage: AppLanguage,
  val countdownMode: CountdownMode,
  val customTargetEpochMillis: Long?,
)

class ViceTimePreferencesRepository(private val context: Context) {

  private object Keys {
    val PINNED_ZONES = stringPreferencesKey("pinned_zones_csv")
    val IS_24_HOUR = booleanPreferencesKey("is_24_hour")
    val IS_DARK_THEME = booleanPreferencesKey("is_dark_theme")
    val LANGUAGE_CODE = stringPreferencesKey("language_code")
    val COUNTDOWN_MODE = stringPreferencesKey("countdown_mode")
    val CUSTOM_TARGET_EPOCH = longPreferencesKey("custom_target_epoch_millis")
  }

  val preferencesFlow: Flow<UserTimePreferences> = context.dataStore.data.map { prefs ->
    val rawZones = prefs[Keys.PINNED_ZONES]
    val zones = if (rawZones.isNullOrBlank()) {
      buildDefaultZones()
    } else {
      rawZones.split("|")
        .map { it.trim() }
        .filter { it.isNotEmpty() && isValidZone(it) }
        .ifEmpty { buildDefaultZones() }
    }

    val modeName = prefs[Keys.COUNTDOWN_MODE]
    val mode = CountdownMode.entries.firstOrNull { it.name == modeName } ?: CountdownMode.VICE_CITY
    val language = AppLanguage.fromCode(prefs[Keys.LANGUAGE_CODE])

    UserTimePreferences(
      pinnedZoneIds = zones,
      is24HourFormat = prefs[Keys.IS_24_HOUR] ?: false,
      isDarkTheme = prefs[Keys.IS_DARK_THEME] ?: true,
      selectedLanguage = language,
      countdownMode = mode,
      customTargetEpochMillis = prefs[Keys.CUSTOM_TARGET_EPOCH],
    )
  }

  suspend fun toggleZonePinned(zoneId: String) {
    if (!isValidZone(zoneId)) return
    context.dataStore.edit { prefs ->
      val current = parseZones(prefs[Keys.PINNED_ZONES])
      val updated = if (zoneId in current) {
        if (current.size > 1) current - zoneId else current
      } else {
        current + zoneId
      }
      prefs[Keys.PINNED_ZONES] = updated.joinToString("|")
    }
  }

  suspend fun removeZone(zoneId: String) {
    context.dataStore.edit { prefs ->
      val current = parseZones(prefs[Keys.PINNED_ZONES])
      if (current.size > 1) {
        prefs[Keys.PINNED_ZONES] = (current - zoneId).joinToString("|")
      }
    }
  }

  suspend fun promoteZoneToTop(zoneId: String) {
    if (!isValidZone(zoneId)) return
    context.dataStore.edit { prefs ->
      val current = parseZones(prefs[Keys.PINNED_ZONES])
      val updated = listOf(zoneId) + current.filter { it != zoneId }
      prefs[Keys.PINNED_ZONES] = updated.joinToString("|")
    }
  }

  suspend fun set24HourFormat(is24Hour: Boolean) {
    context.dataStore.edit { prefs ->
      prefs[Keys.IS_24_HOUR] = is24Hour
    }
  }

  suspend fun setDarkTheme(isDark: Boolean) {
    context.dataStore.edit { prefs ->
      prefs[Keys.IS_DARK_THEME] = isDark
    }
  }

  suspend fun setLanguageCode(code: String) {
    context.dataStore.edit { prefs ->
      prefs[Keys.LANGUAGE_CODE] = AppLanguage.fromCode(code).code
    }
  }

  suspend fun setCountdownMode(mode: CountdownMode) {
    context.dataStore.edit { prefs ->
      prefs[Keys.COUNTDOWN_MODE] = mode.name
    }
  }

  suspend fun setCustomTargetEpoch(epochMillis: Long) {
    context.dataStore.edit { prefs ->
      prefs[Keys.CUSTOM_TARGET_EPOCH] = epochMillis
      prefs[Keys.COUNTDOWN_MODE] = CountdownMode.CUSTOM.name
    }
  }

  private fun parseZones(raw: String?): List<String> {
    if (raw.isNullOrBlank()) return buildDefaultZones()
    return raw.split("|")
      .map { it.trim() }
      .filter { it.isNotEmpty() && isValidZone(it) }
      .ifEmpty { buildDefaultZones() }
  }

  private fun buildDefaultZones(): List<String> {
    val localId = ZoneId.systemDefault().id
    return (listOf(localId) + CityCatalog.DEFAULT_PINNED_ZONES).distinct()
  }

  private fun isValidZone(zoneId: String): Boolean {
    return runCatching { ZoneId.of(zoneId) }.isSuccess
  }
}
