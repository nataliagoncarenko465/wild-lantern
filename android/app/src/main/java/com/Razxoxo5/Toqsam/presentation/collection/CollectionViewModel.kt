package com.Razxoxo5.Toqsam.presentation.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Razxoxo5.Toqsam.domain.repository.PlannerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class CollectionViewModel(
    private val planner: PlannerRepository
) : ViewModel() {

    private val selectedId = MutableStateFlow<String?>(null)

    val state: StateFlow<CollectionUiState> = combine(
        planner.progress,
        selectedId
    ) { progress, selection ->
        val scenes = planner.sceneCatalog.map { scene ->
            SceneCard(
                id = scene.id,
                name = scene.name,
                condition = scene.condition,
                unlocked = scene.id in progress.sceneIds
            )
        }
        CollectionUiState(
            scenes = scenes,
            unlocked = scenes.count { it.unlocked },
            total = scenes.size,
            selected = scenes.firstOrNull { it.id == selection && it.unlocked }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS),
        initialValue = CollectionUiState()
    )

    fun onSceneSelected(id: String) {
        selectedId.value = id
    }

    fun onSceneDismissed() {
        selectedId.value = null
    }

    private companion object {
        const val STOP_TIMEOUT_MS = 5000L
    }
}
