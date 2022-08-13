package com.project.lalabib.storiesin.service

import com.project.lalabib.storiesin.data.response.AddStoryResponse
import com.project.lalabib.storiesin.data.response.LoginResponse
import com.project.lalabib.storiesin.data.response.RegisterResponse
import com.project.lalabib.storiesin.data.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun postRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun postLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("stories")
    suspend fun getListStories(
        @Header("Authorization") token : String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): Response<StoryResponse>

    @GET("stories")
    suspend fun getStory(
        @Header("Authorization") token: String,
        @Query("location") loc: Int = 1,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): StoryResponse

    @Multipart
    @POST("stories")
    suspend fun postStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): AddStoryResponse
}