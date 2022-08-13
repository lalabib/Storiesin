package com.project.lalabib.storiesin.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.project.lalabib.storiesin.data.model.UserPreference
import com.project.lalabib.storiesin.data.response.ListStoryItem
import com.project.lalabib.storiesin.service.ApiService
import kotlinx.coroutines.flow.first

class StoryPagingSource(
    private val pref: UserPreference,
    private val apiService: ApiService
) : PagingSource<Int, ListStoryItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val token = pref.getUser().first().token

            if (token.isNotEmpty()) {
                val responseData = apiService.getListStories(token, page, params.loadSize)
                if (responseData.isSuccessful) {
                    LoadResult.Page(
                        data = responseData.body()?.listStory ?: emptyList(),
                        prevKey = if (page == INITIAL_PAGE_INDEX) null else page -1,
                        nextKey = if (responseData.body()?.listStory.isNullOrEmpty()) null else page + 1
                    )
                } else {
                    LoadResult.Error(Exception("Load Error"))
                }
            } else {
                LoadResult.Error(Exception("Failed"))
            }
        } catch (exception: Exception) {
            Log.d("Exception", "Load Error: ${exception.message}")
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}