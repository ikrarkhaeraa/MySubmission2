package com.example.mysubmission1.main.API

import com.example.mysubmission1.main.model.UserSession
import com.example.mysubmission1.main.pagging.StoryEntity
import com.example.mysubmission1.main.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import java.util.Objects

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    fun uploadDataRegis(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun uploadDataLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @GET("stories")
    fun getStory(
        @Header("Authorization") token: String,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int? = null
    ): Call<GetAllStoryResponse>

    @GET("stories")
    suspend fun getPaging(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): GetAllStoryResponse

    @Multipart
    @POST("stories")
    fun uploadStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody?,
        @Part("lon") lon: RequestBody?
    ):Call<AddNewStoryResponse>
}