package com.example.news_compose_c40.screens.settings

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(defaultLanguage: String): ViewModel() {

    private val _selectedLanguage = mutableStateOf(defaultLanguage)
    val selectedLanguage: String get() = _selectedLanguage.value

    private val _isExpanded = mutableStateOf<Boolean>(false)
    val isExpanded: Boolean get() = _isExpanded.value

    fun setIsExpanded(isExpanded: Boolean){
        _isExpanded.value = isExpanded

    }

    fun setSelectedLanguage(selectedLanguage: String){
        _selectedLanguage.value = selectedLanguage
    }
}