package com.example.post31.ui

import android.os.Build
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.post31.ui.navigation.Screen
import java.util.Stack

class AppViewModel : ViewModel() {

    private val _screenStack: Stack<Screen> = Stack()
    private val _currentScreen: MutableState<Screen> = mutableStateOf(Screen.Main)
    val currentScreen: State<Screen> = _currentScreen

    private val _dynamicColorEnabled: MutableState<Boolean> = mutableStateOf(true)

    init {
        _screenStack.push(Screen.Main)
    }

    fun navigateTo(screen: Screen) {
        _screenStack.push(screen)
        _currentScreen.value = screen
    }

    fun navigateBack(): Boolean {
        if (_screenStack.size > 1) {
            _screenStack.pop()
            _currentScreen.value = _screenStack.peek()

            return true
        }

        return false
    }

    val dynamicColorEnabled: State<Boolean>
        get() = _dynamicColorEnabled

    fun setCurrentScreen(screen: Screen) {
        _currentScreen.value = screen
    }

    fun setDynamicColorEnabled(enabled: Boolean) {
        _dynamicColorEnabled.value = enabled
    }
}