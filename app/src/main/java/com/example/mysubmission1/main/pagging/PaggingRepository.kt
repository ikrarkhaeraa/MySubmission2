package com.example.mysubmission1.main.pagging

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.mysubmission1.main.API.ApiService
import com.example.mysubmission1.main.UserPreferences
import com.example.mysubmission1.main.pagging.PaggingDatabase
import com.example.mysubmission1.main.pagging.StoryPagingSource
import com.example.mysubmission1.main.response.GetAllStoryResponse
import com.example.mysubmission1.main.response.ListStoryItem

class PaggingRepository(private val storyDatabase: PaggingDatabase, private val apiService: ApiService, private val preferences: UserPreferences) {
    fun getStoryPaging(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService, preferences)
            }
        ).liveData
    }
}