package ru.netology.neworknetology.screens.main.tabs.users

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.netology.neworknetology.api.AuthApiService
import ru.netology.neworknetology.dao.UserDao
import ru.netology.neworknetology.dto.User
import ru.netology.neworknetology.entity.toDto
import ru.netology.neworknetology.entity.toUserEntity
import ru.netology.neworknetology.model.ApiException
import ru.netology.neworknetology.model.NetworkException
import ru.netology.neworknetology.model.UnknownException
import java.io.IOException
import javax.inject.Inject


class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val authApiService: AuthApiService,
) : UserRepository {

    override val data: Flow<List<User>> =
        userDao.getAll()
            .map { it.toDto() }
            .flowOn(Dispatchers.Default)

    override suspend fun getAll() {
        try {
            userDao.getAll()
            val response = authApiService.getUserList()
            if (!response.isSuccessful) {
                throw ApiException(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiException(response.code(), response.message())
            userDao.insert(body.toUserEntity())
        } catch (e: IOException) {
            throw NetworkException
        } catch (e: Exception) {
            throw UnknownException
        }
    }

    override suspend fun getUserById(id: Int): User {
        try {
            val response = authApiService.getUserById(id)
            if (!response.isSuccessful) {
                throw ApiException(response.code(), response.message())
            }
            return response.body() ?: throw ApiException(response.code(), response.message())
        } catch (e: IOException) {
            throw NetworkException
        } catch (e: Exception) {
            throw UnknownException
        }
    }
}