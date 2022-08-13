package com.project.lalabib.storiesin.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.lalabib.storiesin.data.StoryRepository
import com.project.lalabib.storiesin.data.model.UserModel
import kotlinx.coroutines.launch

class LoginViewModel(private val repo: StoryRepository): ViewModel() {

    fun postLogin(email: String, password: String) = repo.postLogin(email, password)

    fun saveUser(user: UserModel) {
        viewModelScope.launch {
            repo.saveUser(user)
        }
    }

    fun login() {
        viewModelScope.launch {
            repo.login()
        }
    }
}