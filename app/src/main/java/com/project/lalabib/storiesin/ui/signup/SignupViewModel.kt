package com.project.lalabib.storiesin.ui.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.lalabib.storiesin.data.response.RegisterResponse
import com.project.lalabib.storiesin.service.ApiConfig
import com.project.lalabib.storiesin.utils.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupViewModel: ViewModel() {

    private val _registerResponse = MutableLiveData<RegisterResponse>()
    val registerResponse: LiveData<RegisterResponse> = _registerResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _isLoading

    private val _txtMsg = MutableLiveData<Event<String>>()
    val txtMsg: LiveData<Event<String>> = _txtMsg

    fun postRegister(name: String, email: String, password: String) {
        _isLoading.value = true
        ApiConfig.instance.postRegister(name, email, password).enqueue(object :
            Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    _registerResponse.value = response.body()
                    _txtMsg.value = Event(response.body()?.message.toString())
                } else {
                    _txtMsg.value = Event(response.message().toString())
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _isLoading.value = false
                _txtMsg.value = Event(t.message.toString())
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object {
        private const val TAG = "Signup"
    }
}