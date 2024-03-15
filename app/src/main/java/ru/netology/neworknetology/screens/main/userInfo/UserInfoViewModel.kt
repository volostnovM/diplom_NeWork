package ru.netology.neworknetology.screens.main.userInfo

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.netology.neworknetology.model.setting.AppSettings
import javax.inject.Inject

@HiltViewModel
class UserInfoViewModel @Inject constructor(
    private val appSettings: AppSettings
) : ViewModel() {
    fun getCurrentUserId(): Int {
        return appSettings.getCurrentIdForUser()
    }
    fun clearSetting() {
        appSettings.clearAllSetting()
    }
}