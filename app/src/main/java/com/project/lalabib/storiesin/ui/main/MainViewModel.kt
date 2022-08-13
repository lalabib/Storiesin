package com.project.lalabib.storiesin.ui.main

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.project.lalabib.storiesin.data.StoryRepository
import com.project.lalabib.storiesin.data.model.UserModel
import com.project.lalabib.storiesin.data.response.ListStoryItem
import kotlinx.coroutines.launch

class MainViewModel(private val repo: StoryRepository): ViewModel() {

    val getListStory: LiveData<PagingData<ListStoryItem>> =
        repo.getListStories().cachedIn(viewModelScope)

    fun getUser(): LiveData<UserModel> {
        return repo.getUser()
    }

    fun logout() {
        viewModelScope.launch {
            repo.logout()
        }
    }
}