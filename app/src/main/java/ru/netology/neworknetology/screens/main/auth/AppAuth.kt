package ru.netology.neworknetology.screens.main.auth

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.netology.neworknetology.api.AuthApiService
import ru.netology.neworknetology.model.ApiException
import ru.netology.neworknetology.model.NetworkException
import ru.netology.neworknetology.model.UnknownException
import ru.netology.neworknetology.model.setting.AppSettings
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppAuth @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val appSettings: AppSettings,
    private val networkManager: AuthApiService
) {

    suspend fun signIn(login: String, pass: String) {
        try {
            val response = networkManager.authenticationUser(login, pass)

            if (!response.isSuccessful) {
                throw ApiException(response.code(), response.message())
            }

            appSettings.setCurrentToken(requireNotNull(response.body()?.token))
            appSettings.setCurrentIdForUser(requireNotNull(response.body()?.id))

        }
        catch (e: IOException) {
            throw NetworkException
        } catch (e: Exception) {
            throw UnknownException
        }
    }

    suspend fun signUp(login: String, pass: String, name: String) {
        try {
            val response = networkManager.registrationUser(login, pass, name)

            Log.d("currentToken", "message = ${response.message()} errorBody = ${response.body().toString()} " +
                    "headers = ${response.headers()}  raw = ${response.raw()} code = ${response.code()}")

            if (!response.isSuccessful) {
                throw ApiException(response.code(), response.message())
            }

//            val tokenUser = response.body()?.token
//            appSettings.setCurrentToken(tokenUser)

            appSettings.setCurrentToken(requireNotNull(response.body()?.token))
        }
        catch (e: IOException) {
            throw NetworkException
        } catch (e: Exception) {
            throw UnknownException
        }
    }
}