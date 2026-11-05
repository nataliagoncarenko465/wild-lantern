package com.Razxoxo5.Toqsam.presentation.collection

data class SceneCard(
    val id: String,
    val name: String,
    val condition: String,
    val unlocked: Boolean
)

data class CollectionUiState(
    val scenes: List<SceneCard> = emptyList(),
    val unlocked: Int = 0,
    val total: Int = 0,
    val selected: SceneCard? = null
)
