package ru.netology.neworknetology.screens.main.tabs.events

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.netology.neworknetology.dto.Event
import ru.netology.neworknetology.model.ForbiddenException
import ru.netology.neworknetology.model.StateModel
import ru.netology.neworknetology.utils.MutableUnitLiveEvent
import ru.netology.neworknetology.utils.publishEvent
import ru.netology.neworknetology.utils.share
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {

    private val _dataState = MutableLiveData<StateModel>()
    val dataState: LiveData<StateModel> = _dataState.share()

    private val _showForbiddenToastEvent = MutableUnitLiveEvent()
    val showForbiddenToastEvent = _showForbiddenToastEvent.share()

    val data: Flow<List<Event>> = eventRepository.data

    init {
        getAllEvent()
    }

    fun getAllEvent() = viewModelScope.launch {
        _dataState.postValue(StateModel(loading = true))
        try {
            eventRepository.getAllEvent()
            _dataState.postValue(StateModel(loading = false))
        } catch (e: Exception) {
            _dataState.value = StateModel(error = true, loading = false)
        }
    }

    fun likeById(event: Event) = viewModelScope.launch {
        try {
            if (!event.likedByMe) {
                eventRepository.likeEventById(event.id)
            } else {
                eventRepository.dislikeEventById(event.id)
            }
        } catch (e: Exception) {
            _dataState.value = StateModel(error = true, loading = false)
        }
    }

    fun removeById(event: Event) = viewModelScope.launch {
        try {
            eventRepository.removeEventById(event.id)
        } catch (e: Exception) {
            e.printStackTrace()
            if ((e is ForbiddenException) && (e.code == 403)) {
                _dataState.value = StateModel(error = false, loading = false)
                showForbiddenToast()
            } else {
                _dataState.value = StateModel(error = true, loading = false)
            }
        }
    }

    private fun showForbiddenToast() = _showForbiddenToastEvent.publishEvent()

}