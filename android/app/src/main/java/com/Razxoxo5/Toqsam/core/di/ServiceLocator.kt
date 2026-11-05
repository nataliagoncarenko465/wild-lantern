package com.Razxoxo5.Toqsam.core.di

import android.content.Context
import com.Razxoxo5.Toqsam.data.local.WildLanternPreferences
import com.Razxoxo5.Toqsam.data.repository.PlannerRepositoryImpl
import com.Razxoxo5.Toqsam.data.repository.SettingsRepositoryImpl

object ServiceLocator {

    private var factory: AppViewModelFactory? = null

    fun init(context: Context) {
        if (factory != null) return
        val store = WildLanternPreferences(context)
        factory = AppViewModelFactory(
            PlannerRepositoryImpl(store),
            SettingsRepositoryImpl(store)
        )
    }

    val viewModelFactory: AppViewModelFactory
        get() = checkNotNull(factory)
}
