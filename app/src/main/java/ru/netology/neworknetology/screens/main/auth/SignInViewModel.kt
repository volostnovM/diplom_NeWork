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
class SignInViewModel @Inject constructor(
    private val appAuth: AppAuth
) : ViewModel() {

    private val _state = MutableLiveData<AuthModelState>()
    val state = _state.share()

    private val _clearPasswordEvent = MutableUnitLiveEvent()
    val clearPasswordEvent = _clearPasswordEvent.share()

    private val _showAuthErrorToastEvent = MutableLiveEvent<String>()
    val showAuthToastEvent = _showAuthErrorToastEvent.share()

    private val _navigateToTabsEvent = MutableUnitLiveEvent()
    val navigateToTabsEvent = _navigateToTabsEvent.share()

    fun signIn(login: String, password: String) = viewModelScope.launch {
        _state.value = AuthModelState(loading = true)
        try {
            appAuth.signIn(login, password)
            _state.value = AuthModelState(success = true)
            launchTabsScreen()
        } catch (e: Exception) {
            processAuthException(e.message)
        }
    }


    private fun processAuthException(message: String?) {
        _state.value = _state.value?.copy(
            loading = false
        )
        clearPasswordField()
        showAuthErrorToast(message)
    }

    fun clean() {
        _state.value = AuthModelState(loading = false, error = false, success = false)
    }

    private fun clearPasswordField() = _clearPasswordEvent.publishEvent()

    private fun showAuthErrorToast(message: String?) = _showAuthErrorToastEvent.publishEvent("Произошла ошибка аутентификации! ${message ?: ""}")

    private fun launchTabsScreen() = _navigateToTabsEvent.publishEvent()

}