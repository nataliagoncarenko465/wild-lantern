package com.Razxoxo5.Toqsam.domain.repository

import com.Razxoxo5.Toqsam.domain.model.AppSettings
import kotlinx.coroutines.flow.StateFlow

interface SettingsRepository {

    val settings: StateFlow<AppSettings>

    fun update(settings: AppSettings)
}
