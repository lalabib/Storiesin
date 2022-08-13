package com.project.lalabib.storiesin.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.lalabib.storiesin.data.model.UserModel
import com.project.lalabib.storiesin.data.model.UserPreference
import com.project.lalabib.storiesin.data.response.LoginResponse
import com.project.lalabib.storiesin.service.ApiConfig
import com.project.lalabib.storiesin.utils.Event
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val pref: UserPreference): ViewModel() {

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _isLoading

    private val _txtMsg = MutableLiveData<Event<String>>()
    val txtMsg: LiveData<Event<String>> = _txtMsg

    fun postLogin(email: String, password: String) {
        _isLoading.value = true
        ApiConfig.instance.postLogin(email, password).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    _loginResponse.value = response.body()
                    _txtMsg.value = Event(response.body()?.message.toString())
                } else {
                    _txtMsg.value = Event(response.message().toString())
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                _txtMsg.value = Event(t.message.toString())
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun saveUser(user: UserModel) {
        viewModelScope.launch {
            pref.saveUser(user)
        }
    }

    fun login() {
        viewModelScope.launch {
            pref.login()
        }
    }

    companion object {
        private const val TAG = "Login"
    }
}