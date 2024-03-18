package ru.netology.neworknetology.screens.main.tabs.posts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.netology.neworknetology.dto.Post
import ru.netology.neworknetology.dto.User
import ru.netology.neworknetology.model.ForbiddenException
import ru.netology.neworknetology.model.StateModel
import ru.netology.neworknetology.model.setting.AppSettings
import ru.netology.neworknetology.utils.MutableLiveEvent
import ru.netology.neworknetology.utils.MutableUnitLiveEvent
import ru.netology.neworknetology.utils.publishEvent
import ru.netology.neworknetology.utils.share
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val appSettings: AppSettings
) : ViewModel() {
    private val _dataState = MutableLiveData<StateModel>()
    val dataState: LiveData<StateModel> = _dataState.share()

    private val _showForbiddenToastEvent = MutableUnitLiveEvent()
    val showForbiddenToastEvent = _showForbiddenToastEvent.share()

    private val _navigateToUserInfoEvent = MutableLiveEvent<User>()
    val navigateToUserInfoEvent = _navigateToUserInfoEvent.share()

    private val _userInfo = MutableLiveData<User>()
    val user: LiveData<User> = _userInfo.share()

    val data: Flow<List<Post>> = postRepository.data

    init {
        getAllPost()
    }

    fun getAllPost() = viewModelScope.launch {
        _dataState.postValue(StateModel(loading = true))
        try {
            postRepository.getAllPosts()
            _dataState.postValue(StateModel(loading = false))
        } catch (e: Exception) {
            _dataState.value = StateModel(error = true, loading = false)
        }
    }

    fun getCurrentUserId(): Int {
        return appSettings.getCurrentIdForUser()
    }

    fun likeById(post: Post) = viewModelScope.launch {
        try {
            if (!post.likedByMe) {
                postRepository.likeById(post.id)
            } else {
                postRepository.dislikeById(post.id)
            }
        } catch (e: Exception) {
            _dataState.value = StateModel(error = true, loading = false)
        }
    }

    fun removeById(post: Post) = viewModelScope.launch {
        try {
            postRepository.removeById(post.id)
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

    fun  getUserById(id: Int) = viewModelScope.launch {
        try {
            val response = postRepository.getUserById(id)
            _navigateToUserInfoEvent.publishEvent(response)
        } catch (e: Exception) {
            _dataState.value = StateModel(error = true, loading = false)
        }
    }

    private fun showForbiddenToast() = _showForbiddenToastEvent.publishEvent()
}