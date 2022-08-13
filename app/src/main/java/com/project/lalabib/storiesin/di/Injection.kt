package com.project.lalabib.storiesin.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.project.lalabib.storiesin.data.StoryRepository
import com.project.lalabib.storiesin.data.model.UserPreference
import com.project.lalabib.storiesin.service.ApiConfig

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("storiesin")

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val preferences = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return StoryRepository.getInstance(preferences, apiService)
    }
}