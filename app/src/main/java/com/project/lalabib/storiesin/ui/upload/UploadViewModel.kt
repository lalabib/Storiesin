package com.project.lalabib.storiesin.ui.upload

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.project.lalabib.storiesin.data.model.UserModel
import com.project.lalabib.storiesin.data.model.UserPreference
import com.project.lalabib.storiesin.data.response.AddStoryResponse
import com.project.lalabib.storiesin.service.ApiConfig
import com.project.lalabib.storiesin.utils.Event
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UploadViewModel(private val pref: UserPreference) : ViewModel() {

    private val _addStoryResponse = MutableLiveData<AddStoryResponse>()
    val addStoryResponse: LiveData<AddStoryResponse> = _addStoryResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _isLoading

    private val _txtMsg = MutableLiveData<Event<String>>()
    val txtMsg: LiveData<Event<String>> = _txtMsg


    fun postStory(token: String, file: MultipartBody.Part, description: RequestBody) {
        _isLoading.value = true
        ApiConfig.instance.postStory(token, file, description).enqueue(object :
            Callback<AddStoryResponse> {
            override fun onResponse(call: Call<AddStoryResponse>, response: Response<AddStoryResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _addStoryResponse.value = response.body()
                    _txtMsg.value = Event(response.body()?.message.toString())
                } else {
                    _txtMsg.value = Event(response.message().toString())
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<AddStoryResponse>, t: Throwable) {
                _isLoading.value = false
                _txtMsg.value = Event(t.message.toString())
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    companion object {
        private const val TAG = "UploadStory"
    }
}