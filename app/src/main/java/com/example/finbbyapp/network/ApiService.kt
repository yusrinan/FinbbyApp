package com.example.finbbyapp.network

import com.example.finbbyapp.network.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("api/handler/register")
    fun register(
        @Body registerRequest: RegisterRequest
    ): Call<RegisterResponse>

    @POST("api/handler/login")
    fun login(
        @Query("email") email: String,
        @Query("password") password: String
    ): Call<LoginResponse>

    @Headers("Content-Type: application/json")
    @POST("api/handler/survey")
    fun addSurvey(
        @Body surveyRequest: SurveyRequest
    ): Call<AddSurveyResponse>

    @Headers("Content-Type: application/json")
    @Multipart
    @POST("api/handler/posting/creates")
    fun addPosting(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("title") title: RequestBody,
        @Part("topic") topic: RequestBody,
        @Part("content") content: RequestBody,
    ): Call<AddPostingResponse>


    @GET("api/handler/posting")
    fun getAllPosting(): Call<GetAllPostingResponse>

    @Headers("Content-Type: application/json")
    @POST("api/handler/forum/create/")
    fun addForum(
        @Body addForumRequest: AddForumRequest
    ): Call<AddForumResponse>

    @GET("api/handler/forum")
    fun getAllForum(): Call<GetAllForumResponse>

    @GET("api/handler/forum/{forum}")
    fun getForumByName(
        @Path("forum") forum: String
    ): Call<GetForumByNameResponse>

    @POST("api/handler/forum/join/{forum}")
    fun joinForum(
        @Path("forum") forum: String
    ): Call<JoinForumResponse>
}