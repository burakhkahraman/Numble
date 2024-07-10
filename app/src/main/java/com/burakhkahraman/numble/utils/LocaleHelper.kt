package com.burakhkahraman.numble.utils

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import java.util.Locale

object LocaleHelper {

    fun setLocale(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val config = Configuration()
        config.locale = locale
        context.resources.updateConfiguration(config, context.resources.displayMetrics)

        return context
    }

    fun onAttach(context: Context, defaultLanguage: String): Context {
        val lang = getPersistedData(context, defaultLanguage)
        return setLocale(context, lang)
    }

    private fun getPersistedData(context: Context, defaultLanguage: String): String {
        val preferences = context.getSharedPreferences("language_settings", Context.MODE_PRIVATE)
        return preferences.getString("language", defaultLanguage) ?: defaultLanguage
    }

    fun persist(context: Context, language: String) {
        val preferences = context.getSharedPreferences("language_settings", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("language", language)
        editor.apply()
    }
}