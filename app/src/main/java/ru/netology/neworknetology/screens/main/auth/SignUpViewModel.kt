package ru.netology.neworknetology.screens.main.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.netology.neworknetology.model.auth.AuthModelState
import ru.netology.neworknetology.utils.MutableLiveEvent
import ru.netology.neworknetology.utils.MutableUnitLiveEvent
import ru.netology.neworknetology.utils.publishEvent
import ru.netology.neworknetology.utils.share
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val appAuth: AppAuth
) : ViewModel() {

    private val _state = MutableLiveData<AuthModelState>()
    val state = _state.share()

    private val _showAuthErrorToastEvent = MutableLiveEvent<String>()
    val showAuthToastEvent = _showAuthErrorToastEvent.share()

    private val _navigateToTabsEvent = MutableUnitLiveEvent()
    val navigateToTabsEvent = _navigateToTabsEvent.share()

    fun signUp(login: String, pass: String, name: String) = viewModelScope.launch {
        _state.value = AuthModelState(loading = true)
        try {
            appAuth.signUp(login, pass, name)
            _state.value = AuthModelState(success = true)
            launchTabsScreen()
        } catch (e: Exception) {
            processAuthException()
        }
    }

    private fun processAuthException() {
        _state.value = _state.value?.copy(
            loading = false
        )
        showAuthErrorToast(null)
    }


    private fun launchTabsScreen() = _navigateToTabsEvent.publishEvent()

    private fun showAuthErrorToast(message: String?) = _showAuthErrorToastEvent.publishEvent("Произошла ошибка аутентификации! ${message ?: ""}")


    fun clean() {
        _state.value = AuthModelState(loading = false, error = false, success = false)
    }
}