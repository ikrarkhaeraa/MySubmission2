package com.example.mysubmission1.main

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.mysubmission1.main.API.ApiConfig
import com.example.mysubmission1.main.model.DataSource
import com.example.mysubmission1.main.pagging.PaggingRepository
import com.example.mysubmission1.main.pagging.PaggingDatabase


private val Context.database: DataStore<Preferences> by preferencesDataStore("token")

object Injection {
    fun provideRepository(context: Context): DataSource {
        val preferences = UserPreferences.getInstance(context.database)
        return DataSource.getInstance(preferences)
    }

    fun providePaging(context: Context): PaggingRepository {
        val database = PaggingDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        val preferences = UserPreferences.getInstance(context.database)
        return PaggingRepository(database, apiService, preferences)
    }

}