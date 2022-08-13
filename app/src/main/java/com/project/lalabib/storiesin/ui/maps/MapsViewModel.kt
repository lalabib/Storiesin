package com.project.lalabib.storiesin.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.project.lalabib.storiesin.data.StoryRepository
import com.project.lalabib.storiesin.data.model.UserModel

class MapsViewModel(private val repo: StoryRepository): ViewModel() {
    fun getStoryWithLocation(token: String) =
        repo.getStoryWithLocation(token)

    fun getUser(): LiveData<UserModel> {
        return repo.getUser()
    }
}