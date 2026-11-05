package com.Razxoxo5.Toqsam.data.repository

import com.Razxoxo5.Toqsam.core.config.GameConfig
import com.Razxoxo5.Toqsam.data.local.WildLanternPreferences
import com.Razxoxo5.Toqsam.domain.model.AppSettings
import com.Razxoxo5.Toqsam.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsRepositoryImpl(
    private val store: WildLanternPreferences
) : SettingsRepository {

    private val _settings = MutableStateFlow(read())
    override val settings: StateFlow<AppSettings> = _settings.asStateFlow()

    override fun update(settings: AppSettings) {
        _settings.value = settings
        store.writeBoolean(KEY_REMINDER, settings.reminderCard)
        store.writeBoolean(KEY_SOUND, settings.soundCues)
        store.writeBoolean(KEY_MOTION, settings.reducedMotion)
        store.writeInt(KEY_GOALS_PER_DAY, settings.goalsPerDay)
    }

    private fun read(): AppSettings = AppSettings(
        reminderCard = store.readBoolean(KEY_REMINDER, true),
        soundCues = store.readBoolean(KEY_SOUND, true),
        reducedMotion = store.readBoolean(KEY_MOTION, false),
        goalsPerDay = store.readInt(KEY_GOALS_PER_DAY, GameConfig.DEFAULT_GOALS_PER_DAY)
    )

    private companion object {
        const val KEY_REMINDER = "reminder_card"
        const val KEY_SOUND = "sound_cues"
        const val KEY_MOTION = "reduced_motion"
        const val KEY_GOALS_PER_DAY = "goals_per_day"
    }
}
