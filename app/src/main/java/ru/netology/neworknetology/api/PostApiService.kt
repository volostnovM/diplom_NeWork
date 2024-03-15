package ru.netology.neworknetology.api

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import ru.netology.neworknetology.dto.Media
import ru.netology.neworknetology.dto.Post
import ru.netology.neworknetology.dto.User

interface PostApiService {

    @GET("/api/posts")
    suspend fun getAllPosts(): Response<List<Post>>
    @POST("posts")
    suspend fun save(@Body post: Post): Response<Post>

    @POST("posts/{id}/likes")
    suspend fun likeById(@Path("id") id: Int): Response<Post>

    @DELETE("posts/{id}/likes")
    suspend fun dislikeById(@Path("id") id: Int): Response<Post>

    @DELETE("posts/{id}")
    suspend fun removeById(@Path("id") id: Int): Response<Unit>

    @Multipart
    @POST("media")
    suspend fun saveMedia(@Part part: MultipartBody.Part): Response<Media>

    @GET("/api/users/{id}")
    suspend fun getUserById(@Path("id") id: Int): Response<User>
}