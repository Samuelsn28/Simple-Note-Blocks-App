package com.simplenoteblocks.ui.screens.settings

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.intl.Locale
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel

class SettingsViewModel : ViewModel() {
    var isOpenSetLanguageDialog by mutableStateOf(false)
        private set
    var chosenLanguageTag by mutableStateOf("en")
        private set

    fun openSetLanguageDialog() {
        isOpenSetLanguageDialog = true
    }

    fun closeSetLanguageDialog() {
        isOpenSetLanguageDialog = false
    }

    fun updateChosenLanguage(languageTag: String) {
        chosenLanguageTag = languageTag
    }
}