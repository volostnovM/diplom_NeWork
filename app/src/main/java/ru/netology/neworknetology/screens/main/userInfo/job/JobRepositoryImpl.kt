package ru.netology.neworknetology.screens.main.userInfo.job

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.neworknetology.api.JobApiService
import ru.netology.neworknetology.dto.Job
import ru.netology.neworknetology.model.ApiException
import ru.netology.neworknetology.model.NetworkException
import ru.netology.neworknetology.model.UnknownException
import java.io.IOException
import javax.inject.Inject

class JobRepositoryImpl @Inject constructor(
    private val jobApiService: JobApiService
): JobRepository {

    private val _dataJob = MutableLiveData<List<Job>>()
    override val data: LiveData<List<Job>> = _dataJob

    override suspend fun getMyJobs() {
        Log.d("picker", "getMyJob")
        try {
            val response = jobApiService.getMyJob()
            Log.d("picker", "getMyJob response = ${response}")
            if (!response.isSuccessful) {
                throw ApiException(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiException(response.code(), response.message())

            _dataJob.value = body
        }  catch (e: IOException) {
            Log.d("picker", "getMyJob IOException = ${e.printStackTrace()}")
            throw NetworkException
        } catch (e: Exception) {
            Log.d("picker", "getMyJob Exception = ${e.message}")
            throw UnknownException
        }
    }

    override suspend fun getJobs(userId: Int) {
        Log.d("picker", "getJobs userId = ${userId}")
        try {
            val response = jobApiService.getJobs(userId)
            Log.d("picker", "getJobs response = ${response}")
            if (!response.isSuccessful) {
                throw ApiException(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiException(response.code(), response.message())
            _dataJob.value = body
        } catch (e: IOException) {
            Log.d("picker", "getJobs IOException = ${e.printStackTrace()}")
            throw NetworkException
        } catch (e: Exception) {
            Log.d("picker", "getJobs Exception = ${e.message}")
            throw UnknownException
        }
    }

    override suspend fun saveJob(job: Job) {
        try {
            val response = jobApiService.saveJob(job)
            if (!response.isSuccessful) {
                throw ApiException(response.code(), response.message())
            }
            response.body() ?: throw ApiException(response.code(), response.message())

            getMyJobs()
        } catch (e: IOException) {
            throw NetworkException
        } catch (e: Exception) {
            throw UnknownException
        }
    }

    override suspend fun deleteJob(id: Int) {
        try {
            val response = jobApiService.removeJobById(id)
            if (!response.isSuccessful) {
                throw ApiException(response.code(), response.message())
            }

            _dataJob.value = _dataJob.value?.filter { it.id != id }
        } catch (e: IOException) {
            throw NetworkException
        } catch (e: Exception) {
            throw UnknownException
        }
    }
}