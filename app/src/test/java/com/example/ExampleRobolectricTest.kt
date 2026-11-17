package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.CityCatalog
import com.example.data.ViceTimePreferencesRepository
import com.example.model.AppLanguage
import java.time.ZoneId
import java.util.Locale
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `app name matches ViceTime`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("ViceTime", appName)
  }

  @Test
  fun `city catalog resolves all curated zones validly`() {
    assertTrue(CityCatalog.CURATED_CITIES.isNotEmpty())
    CityCatalog.CURATED_CITIES.forEach { city ->
      val zone = ZoneId.of(city.zoneId)
      assertEquals(city.zoneId, zone.id)
    }
  }

  @Test
  fun `theme preference persists via DataStore`() = runTest {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val repository = ViceTimePreferencesRepository(context)

    assertTrue(repository.preferencesFlow.first().isDarkTheme)

    repository.setDarkTheme(false)
    assertFalse(repository.preferencesFlow.first().isDarkTheme)

    repository.setDarkTheme(true)
    assertTrue(repository.preferencesFlow.first().isDarkTheme)
  }

  @Test
  fun `language defaults to system and persists selected language`() = runTest {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val repository = ViceTimePreferencesRepository(context)

    assertEquals(AppLanguage.SYSTEM, repository.preferencesFlow.first().selectedLanguage)
    assertEquals(
      AppLanguage.SPANISH,
      AppLanguage.resolveEffectiveLanguage(AppLanguage.SYSTEM, Locale("es", "MX")),
    )

    repository.setLanguageCode(AppLanguage.JAPANESE.code)
    assertEquals(AppLanguage.JAPANESE, repository.preferencesFlow.first().selectedLanguage)

    repository.setLanguageCode(AppLanguage.SYSTEM.code)
    assertEquals(AppLanguage.SYSTEM, repository.preferencesFlow.first().selectedLanguage)
  }
}
