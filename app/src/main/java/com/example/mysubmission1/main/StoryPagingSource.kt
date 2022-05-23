//package com.example.mysubmission1.main
//
//
//import androidx.paging.PagingSource
//import androidx.paging.PagingState
//import com.example.mysubmission1.main.API.ApiService
//import com.example.mysubmission1.main.model.UserSession
//import com.example.mysubmission1.main.response.GetAllStoryResponse
//
//
//class StoryPagingSource(private val preferences: UserSession, private val apiService: ApiService) : PagingSource<Int, GetAllStoryResponse>() {
//
//    private companion object {
//        const val INITIAL_PAGE_INDEX = 1
//    }
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GetAllStoryResponse> {
//        return try {
//            val position = params.key ?: INITIAL_PAGE_INDEX
//            val token = preferences.token
//            val responseData = apiService.getStoryPagging(token, position, params.loadSize)
//            LoadResult.Page(
//                data = responseData,
//                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
//                nextKey = if (responseData.isNullOrEmpty()) null else position + 1
//            )
//        } catch (exception: Exception) {
//            return LoadResult.Error(exception)
//        }
//    }
//
//    override fun getRefreshKey(state: PagingState<Int, GetAllStoryResponse>): Int? {
//        return state.anchorPosition?.let { anchorPosition ->
//            val anchorPage = state.closestPageToPosition(anchorPosition)
//            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
//        }
//    }
//}