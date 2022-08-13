package com.project.lalabib.storiesin.ui.signup

import androidx.lifecycle.ViewModel
import com.project.lalabib.storiesin.data.StoryRepository

class SignupViewModel(private val repo: StoryRepository): ViewModel() {

    fun postRegister(name: String, email: String, password: String) =
        repo.postRegister(name, email, password)
}