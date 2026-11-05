package com.Razxoxo5.Toqsam.presentation.app

import androidx.lifecycle.ViewModel
import com.Razxoxo5.Toqsam.core.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel : ViewModel() {

    private val backStack = ArrayDeque<Screen>()

    private val _screen = MutableStateFlow<Screen>(Screen.Splash)
    val screen: StateFlow<Screen> = _screen.asStateFlow()

    private val _canGoBack = MutableStateFlow(false)
    val canGoBack: StateFlow<Boolean> = _canGoBack.asStateFlow()

    fun navigate(target: Screen) {
        val current = _screen.value
        if (current == target) return
        if (current != Screen.Splash) {
            backStack.addLast(current)
        }
        _screen.value = target
        syncBackState()
    }

    fun back() {
        val previous = backStack.removeLastOrNull() ?: return
        _screen.value = previous
        syncBackState()
    }

    fun restart(target: Screen) {
        if (backStack.lastOrNull() == target) {
            back()
            return
        }
        navigate(target)
    }

    fun backToMenu() {
        backStack.clear()
        _screen.value = Screen.Menu
        syncBackState()
    }

    private fun syncBackState() {
        _canGoBack.value = backStack.isNotEmpty()
    }
}
