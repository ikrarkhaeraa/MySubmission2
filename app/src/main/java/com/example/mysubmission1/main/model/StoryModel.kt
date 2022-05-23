package com.example.mysubmission1.main.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mysubmission1.main.response.GetAllStoryResponse
import kotlinx.coroutines.launch

class StoryModel(private val dataSource: DataSource) : ViewModel() {
    val listStory: LiveData<GetAllStoryResponse> = dataSource.listStory

    fun getStory(token: String) {
        viewModelScope.launch {
            dataSource.getStory(token)
            Log.e("token", "onResponse: $token")
        }
    }

    fun getUserSession(): LiveData<UserSession> {
        return dataSource.getUserSession()
    }

    fun userLogout() {
        viewModelScope.launch {
            dataSource.userLogout()
        }
    }
}