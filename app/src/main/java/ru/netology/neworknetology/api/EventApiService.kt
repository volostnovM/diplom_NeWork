package ru.netology.neworknetology.api

import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ru.netology.neworknetology.dto.Event



interface EventApiService {
    @GET("/api/events")
    suspend fun getAllEvent(): Response<List<Event>>

    @POST("/api/events/{id}/likes")
    suspend fun likeEventById(@Path("id") id: Int): Response<Event>

    @DELETE("/api/events/{id}/likes")
    suspend fun dislikeById(@Path("id") id: Int): Response<Event>

    @DELETE("/api/events/{id}")
    suspend fun removeById(@Path("id") id: Int): Response<Unit>
}