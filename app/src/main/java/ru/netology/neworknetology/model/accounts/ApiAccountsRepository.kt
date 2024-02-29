package ru.netology.neworknetology.model.accounts

import kotlinx.coroutines.delay
import ru.netology.neworknetology.model.setting.AppSettings
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiAccountsRepository @Inject constructor(
    private val appSettings: AppSettings
)  {
    suspend fun isSignedIn(): Boolean {
        return appSettings.getCurrentToken() != null
    }
}