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
import ru.netology.neworknetology.dto.Event
import ru.netology.neworknetology.dto.Media


interface EventApiService {
    @GET("/api/events")
    suspend fun getAllEvent(): Response<List<Event>>

    @POST("/api/events/{id}/likes")
    suspend fun likeEventById(@Path("id") id: Int): Response<Event>

    @DELETE("/api/events/{id}/likes")
    suspend fun dislikeById(@Path("id") id: Int): Response<Event>

    @DELETE("/api/events/{id}")
    suspend fun removeById(@Path("id") id: Int): Response<Unit>

    @POST("api/events")
    suspend fun saveEvent(@Body event: Event): Response<Event>

    @Multipart
    @POST("api/media")
    suspend fun saveMedia(@Part media: MultipartBody.Part): Response<Media>

    @POST("/api/events/{id}/participants")
    suspend fun takePartAtEvent(@Path("id") id: Int): Response<Event>

    @DELETE("/api/events/{id}/participants")
    suspend fun deleteTakingPart(@Path("id") id: Int): Response<Event>
}