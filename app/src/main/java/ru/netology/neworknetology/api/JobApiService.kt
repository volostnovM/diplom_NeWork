package ru.netology.neworknetology.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ru.netology.neworknetology.dto.Job

interface JobApiService {
    @GET("/api/{userId}/jobs")
    suspend fun getJobs(@Path("userId")authorId: Int) : Response<List<Job>>

    @GET("/api/my/jobs")
    suspend fun getMyJob(): Response<List<Job>>

    @POST("/api/my/jobs")
    suspend fun saveJob(@Body job: Job): Response<Job>

    @DELETE("/api/my/jobs/{id}")
    suspend fun removeJobById(@Path("id") id: Int): Response<Unit>

}