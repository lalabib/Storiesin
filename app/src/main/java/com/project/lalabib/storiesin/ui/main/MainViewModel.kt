package com.project.lalabib.storiesin.ui.main

import android.util.Log
import androidx.lifecycle.*
import com.project.lalabib.storiesin.data.model.UserModel
import com.project.lalabib.storiesin.data.model.UserPreference
import com.project.lalabib.storiesin.data.response.StoryResponse
import com.project.lalabib.storiesin.service.ApiConfig
import com.project.lalabib.storiesin.utils.Event
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val pref: UserPreference): ViewModel() {

    private val _storiesResponse = MutableLiveData<StoryResponse>()
    val storiesResponse: LiveData<StoryResponse> = _storiesResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _isLoading

    private val _txtMsg = MutableLiveData<Event<String>>()
    val txtMsg: LiveData<Event<String>> = _txtMsg

    fun getListStory(token: String) {
        _isLoading.value = true
        ApiConfig.instance.getStory(token).enqueue(object : Callback<StoryResponse> {
            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _storiesResponse.value = response.body()
                    _txtMsg.value = Event(response.body()?.message.toString())
                } else {
                    _txtMsg.value = Event(response.message().toString())
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                _isLoading.value = false
                _txtMsg.value = Event(t.message.toString())
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }

    companion object {
        private const val TAG = "ListStory"
    }
}