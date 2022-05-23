package com.example.mysubmission1.main.pagging

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.mysubmission1.main.Injection
import com.example.mysubmission1.main.response.GetAllStoryResponse
import com.example.mysubmission1.main.response.ListStoryItem

class PaggingModel(storyRepository: PaggingRepository) : ViewModel() {

    val pagingStory: LiveData<PagingData<ListStoryItem>> =
        storyRepository.getStoryPaging().cachedIn(viewModelScope)

    fun logModel() {
        Log.d("pagingModel", "$pagingStory")
    }
}

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PaggingModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PaggingModel(Injection.providePaging(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}