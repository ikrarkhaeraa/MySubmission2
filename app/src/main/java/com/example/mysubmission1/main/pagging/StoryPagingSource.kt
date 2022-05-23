package com.example.mysubmission1.main.pagging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.room.PrimaryKey
import com.example.mysubmission1.main.API.ApiService
import com.example.mysubmission1.main.UserPreferences
import com.example.mysubmission1.main.response.GetAllStoryResponse
import com.example.mysubmission1.main.response.ListStoryItem
import kotlinx.coroutines.flow.first

class StoryPagingSource(private val apiService: ApiService, private val preferences: UserPreferences) : PagingSource<Int, ListStoryItem>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int,  ListStoryItem> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val tokenUser = preferences.getUserSession().first().token
            Log.d("pagingSource", "$tokenUser")
            val responseData = apiService.getPaging(tokenUser, page, params.loadSize)
            Log.d("dataPaging", "$responseData")

            LoadResult.Page(
                data = responseData.listStory,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (responseData.listStory.isNullOrEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            Log.d("pagingError", exception.toString())
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int,  ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}