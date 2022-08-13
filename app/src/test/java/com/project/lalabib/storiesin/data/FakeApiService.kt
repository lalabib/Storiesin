package com.project.lalabib.storiesin.data

import com.project.lalabib.storiesin.utils.DataDummy
import com.project.lalabib.storiesin.data.response.AddStoryResponse
import com.project.lalabib.storiesin.data.response.LoginResponse
import com.project.lalabib.storiesin.data.response.RegisterResponse
import com.project.lalabib.storiesin.data.response.StoryResponse
import com.project.lalabib.storiesin.service.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class FakeApiService: ApiService {
    private val dummyStoryResponse = DataDummy.generateDummyStoriesWithLocResponse()
    private val dummyRegisterResponse = DataDummy.generateDummyRegisterResponse()
    private val dummyLoginResponse = DataDummy.generateDummyLoginResponse()
    private val dummyAddNewStoryResponse = DataDummy.generateDummyUploadStoryResponse()

    override suspend fun postRegister(
        name: String,
        email: String,
        password: String
    ): RegisterResponse {
        return dummyRegisterResponse
    }

    override suspend fun postLogin(email: String, password: String): LoginResponse {
        return dummyLoginResponse
    }

    override suspend fun getListStories(
        token: String,
        page: Int,
        size: Int
    ): Response<StoryResponse> {
        TODO("Not yet implemented")
    }


    override suspend fun getStory(token: String, loc: Int, page: Int, size: Int): StoryResponse {
        return dummyStoryResponse
    }

    override suspend fun postStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody
    ): AddStoryResponse {
        return dummyAddNewStoryResponse
    }

}