package com.imahdev.myappstory.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.imahdev.myappstory.data.local.pref.UserPreferences
import com.imahdev.myappstory.data.remote.retrofit.ApiConfig
import com.imahdev.myappstory.data.repository.StoryRepository


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token")

object Injection {
    fun provideRepository(context: Context): StoryRepository {
    val pref = UserPreferences.getInstance(context.dataStore)
    val apiService = ApiConfig.getApiService()
    return StoryRepository.getInstance(apiService, pref)
}
}