package com.project.lalabib.storiesin.ui.upload

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.project.lalabib.storiesin.data.StoryRepository
import com.project.lalabib.storiesin.data.model.UserModel
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UploadViewModel(private val repo: StoryRepository) : ViewModel() {

    fun postStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
    ) = repo.postStory(token, file, description)

    fun getUser(): LiveData<UserModel> {
        return repo.getUser()
    }
}