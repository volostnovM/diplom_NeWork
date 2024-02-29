package ru.netology.neworknetology.api

import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ru.netology.neworknetology.dto.User
import ru.netology.neworknetology.model.auth.AuthModel

interface AuthApiService {
    @FormUrlEncoded
    @POST("api/users/authentication")
    suspend fun authenticationUser(
        @Field("login") login: String,
        @Field("pass") pass: String
    ): Response<AuthModel>

    @FormUrlEncoded
    @POST("api/users/registration")
    suspend fun registrationUser(
        @Field("login") login: String,
        @Field("pass") pass: String,
        @Field("name") name: String
    ): Response<AuthModel>


    @GET("/api/users")
    suspend fun getUserList(): Response<List<User>>

    @GET("/api/users/{id}")
    suspend fun getUserById(@Path("id") id: Int): Response<User>
}