package ru.netology.neworknetology.screens.main.userInfo.job

import androidx.lifecycle.LiveData
import ru.netology.neworknetology.dto.Job

interface JobRepository {
    val data: LiveData<List<Job>>
    suspend fun getMyJobs()
    suspend fun getJobs(userId: Int)
    suspend fun saveJob(job: Job)
    suspend fun deleteJob(id: Int)
}