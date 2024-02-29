package ru.netology.neworknetology.screens.main.tabs.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.netology.neworknetology.dto.User
import ru.netology.neworknetology.model.StateModel
import ru.netology.neworknetology.utils.share
import javax.inject.Inject

@HiltViewModel
class UsersViewModel  @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _dataState = MutableLiveData<StateModel>()
    val dataState: LiveData<StateModel> = _dataState.share()

    val data: Flow<List<User>> = userRepository.data

    init {
        getUserList()
    }

    fun getUserList() = viewModelScope.launch {
        _dataState.postValue(StateModel(loading = true))
        try {
            userRepository.getAll()
            _dataState.postValue(StateModel(loading = false))
        } catch (e: Exception) {
            _dataState.value = StateModel(error = true)
        }
    }

    fun  getUserById(id: Int) = viewModelScope.launch {
        _dataState.postValue(StateModel(loading = true))
        try {
            val user = userRepository.getUserById(id)
            _dataState.postValue(StateModel(loading = false))
        } catch (e: Exception) {
            _dataState.postValue(StateModel(error = true))
        }
    }
}