package ru.netology.neworknetology.screens.main.tabs.posts

import kotlinx.coroutines.flow.Flow
import ru.netology.neworknetology.dto.Post
import ru.netology.neworknetology.dto.User
import ru.netology.neworknetology.model.enums.AttachmentType
import java.io.InputStream

interface PostRepository {
    val data: Flow<List<Post>>
    suspend fun getAllPosts()
    suspend fun likeById(id: Int)
    suspend fun dislikeById(id: Int)
    suspend fun removeById(id: Int)
    suspend fun save(post: Post)
    suspend fun saveWithAttachment(inputStream: InputStream, type: AttachmentType, post: Post)
    suspend fun getUserById(id: Int): User
}