package com.example.mysubmission1.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mysubmission1.main.model.*

class ModelFactory(private val dataSource: DataSource) : ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var instance: ModelFactory? = null
        fun getInstance(context: Context): ModelFactory {
            return instance ?: synchronized(this) {
                instance ?: ModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(StoryModel::class.java) -> {
                StoryModel(dataSource) as T
            }
            modelClass.isAssignableFrom(RegisterModel::class.java) -> {
                RegisterModel(dataSource) as T
            }
            modelClass.isAssignableFrom(LoginModel::class.java) -> {
                LoginModel(dataSource) as T
            }
            modelClass.isAssignableFrom(AddNewStoryModel::class.java) -> {
                AddNewStoryModel(dataSource) as T
            }
            else -> throw IllegalArgumentException("Unknown Model class: " + modelClass.name)
        }
    }
}