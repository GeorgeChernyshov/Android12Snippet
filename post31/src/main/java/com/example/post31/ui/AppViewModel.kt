package com.example.post31.ui

import android.os.Build
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.post31.ui.navigation.Screen

class AppViewModel : ViewModel() {

    private val _currentScreen: MutableState<Screen> = mutableStateOf(
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            Screen.Material
        else Screen.UserExperience
    )

    private val _dynamicColorEnabled: MutableState<Boolean> = mutableStateOf(true)

    val currentScreen: State<Screen>
        get() = _currentScreen

    val dynamicColorEnabled: State<Boolean>
        get() = _dynamicColorEnabled

    fun setCurrentScreen(screen: Screen) {
        _currentScreen.value = screen
    }

    fun setDynamicColorEnabled(enabled: Boolean) {
        _dynamicColorEnabled.value = enabled
    }
}