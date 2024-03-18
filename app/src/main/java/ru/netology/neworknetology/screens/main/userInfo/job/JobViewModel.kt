package ru.netology.neworknetology.screens.main.userInfo.job

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.netology.neworknetology.dto.Job
import ru.netology.neworknetology.model.StateModel
import ru.netology.neworknetology.model.setting.AppSettings
import ru.netology.neworknetology.utils.MutableUnitLiveEvent
import ru.netology.neworknetology.utils.publishEvent
import ru.netology.neworknetology.utils.share
import javax.inject.Inject

@HiltViewModel
class JobViewModel @Inject constructor(
    private val jobRepository: JobRepository,
    private val appSettings: AppSettings
): ViewModel() {
    private val _dataState = MutableLiveData<StateModel>()
    val dataState: LiveData<StateModel> = _dataState.share()

    private val _navigateUpEvent = MutableUnitLiveEvent()
    val navigateToUpEvent = _navigateUpEvent.share()


    val data: LiveData<List<Job>> = jobRepository.data

    fun getCurrentUserId(): Int {
        return appSettings.getCurrentIdForUser()
    }

    fun getJobs(userId: Int?) = viewModelScope.launch {
        try {
            Log.d("picker", "getJobs userId = ${userId}")
            if (userId == null || userId == appSettings.getCurrentIdForUser()) {
                jobRepository.getMyJobs()
            } else {
                jobRepository.getJobs(userId)
            }
        } catch (e: Exception) {
            _dataState.value = StateModel(error = true, loading = false)
        }
    }

    fun saveJob(
        name: String,
        position: String,
        link: String?,
        startWork: String,
        finishWork: String
    ) = viewModelScope.launch {
        try {
            jobRepository.saveJob(
                Job(
                    id = 0,
                    name = name,
                    position = position,
                    start = startWork,
                    finish = finishWork,
                    link = link,
                )
            )
            _navigateUpEvent.publishEvent()
        } catch (e: Exception) {
            _dataState.value = StateModel(error = true, loading = false)
        }
    }

    fun deleteJob(id: Int) = viewModelScope.launch {
        try {
            jobRepository.deleteJob(id)
        } catch (e: Exception) {
            _dataState.value = StateModel(error = true, loading = false)
        }
    }
}

