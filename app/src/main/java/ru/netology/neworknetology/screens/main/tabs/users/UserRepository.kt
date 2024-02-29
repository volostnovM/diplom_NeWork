package ru.netology.neworknetology.screens.main.tabs.users

import kotlinx.coroutines.flow.Flow
import ru.netology.neworknetology.dto.User

interface UserRepository {
    val data: Flow<List<User>>

    suspend fun getAll()
    suspend fun getUserById(id: Int): User
}