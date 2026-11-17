package com.example.model

import java.util.Locale

enum class AppLanguage(
  val code: String,
  val flag: String,
  val nativeName: String,
) {
  SYSTEM("system", "🌐", "System"),
  ENGLISH("en", "🇺🇸", "English"),
  SPANISH("es", "🇪🇸", "Español"),
  PORTUGUESE("pt", "🇧🇷", "Português"),
  FRENCH("fr", "🇫🇷", "Français"),
  GERMAN("de", "🇩🇪", "Deutsch"),
  JAPANESE("ja", "🇯🇵", "日本語");

  companion object {
    fun fromCode(code: String?): AppLanguage {
      return entries.firstOrNull { it.code.equals(code, ignoreCase = true) } ?: SYSTEM
    }

    fun resolveEffectiveLanguage(selected: AppLanguage, systemLocale: Locale = Locale.getDefault()): AppLanguage {
      if (selected != SYSTEM) return selected
      val sysLang = systemLocale.language.lowercase(Locale.US)
      return when (sysLang) {
        "es" -> SPANISH
        "pt" -> PORTUGUESE
        "fr" -> FRENCH
        "de" -> GERMAN
        "ja" -> JAPANESE
        else -> ENGLISH
      }
    }

    fun resolveLocale(selected: AppLanguage, systemLocale: Locale = Locale.getDefault()): Locale {
      return when (resolveEffectiveLanguage(selected, systemLocale)) {
        ENGLISH -> Locale.US
        SPANISH -> Locale("es", "ES")
        PORTUGUESE -> Locale("pt", "BR")
        FRENCH -> Locale.FRANCE
        GERMAN -> Locale.GERMANY
        JAPANESE -> Locale.JAPAN
        SYSTEM -> systemLocale
      }
    }
  }
}
