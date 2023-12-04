package com.example.post31.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.post31.ui.navigation.Screen

class AppViewModel : ViewModel() {

    private val _currentScreen: MutableState<Screen> = mutableStateOf(Screen.UserExperience)
    val currentScreen: State<Screen>
        get() = _currentScreen

    fun setCurrentScreen(screen: Screen) {
        _currentScreen.value = screen
    }
}