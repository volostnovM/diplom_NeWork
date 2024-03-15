package ru.netology.neworknetology.screens.main.tabs.posts

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.neworknetology.api.PostApiService
import ru.netology.neworknetology.dao.PostDao
import ru.netology.neworknetology.dto.Attachment
import ru.netology.neworknetology.dto.Media
import ru.netology.neworknetology.dto.Post
import ru.netology.neworknetology.dto.User
import ru.netology.neworknetology.entity.PostEntity
import ru.netology.neworknetology.model.ApiException
import ru.netology.neworknetology.model.ForbiddenException
import ru.netology.neworknetology.model.NetworkException
import ru.netology.neworknetology.model.UnknownException
import ru.netology.neworknetology.model.enums.AttachmentType
import ru.netology.neworknetology.entity.toDto
import ru.netology.neworknetology.entity.toPostEntity
import ru.netology.neworknetology.model.setting.AppSettings
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject
import kotlin.properties.Delegates

class PostRepositoryImpl @Inject constructor(
    private val postApiService: PostApiService,
    private val postDao: PostDao,
    private val appSettings: AppSettings
) : PostRepository {
    private var userId by Delegates.notNull<Int>()

    init {
        userId = appSettings.getCurrentIdForUser()
    }

    override val data: Flow<List<Post>> =
        postDao.getAllPosts()
            .map {
                it.toDto()
            }
            .map {
                it.map { post ->
                    post.copy(ownedByMe = userId == post.authorId)
                }
            }
            .flowOn(Dispatchers.Default)

    override suspend fun getAllPosts() {
        try {
            postDao.getAllPosts()
            val response = postApiService.getAllPosts()
            if (!response.isSuccessful) {
                throw ApiException(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiException(response.code(), response.message())
            postDao.insert(body.toPostEntity())
        } catch (e: IOException) {
            throw NetworkException
        } catch (e: Exception) {
            throw UnknownException
        }
    }

    override suspend fun likeById(id: Int) {
        try {
            val response = postApiService.likeById(id)
            val body = response.body() ?: throw ApiException(response.code(), response.message())
            postDao.insert(PostEntity.fromDto(body.copy(likedByMe = true)))
            if (!response.isSuccessful) {
                throw ApiException(response.code(), response.message())
            }
        } catch (e: IOException) {
            throw NetworkException
        } catch (e: Exception) {
            throw UnknownException
        }
    }

    override suspend fun dislikeById(id: Int) {
        try {
            val response = postApiService.dislikeById(id)
            val body = response.body() ?: throw ApiException(response.code(), response.message())
            postDao.insert(PostEntity.fromDto(body.copy(likedByMe = false)))
            if (!response.isSuccessful) {
                throw ApiException(response.code(), response.message())
            }
        } catch (e: IOException) {
            throw NetworkException
        } catch (e: Exception) {
            throw UnknownException
        }
    }

    override suspend fun removeById(id: Int) {
        try {
            val response = postApiService.removeById(id)
            if (!response.isSuccessful) {
                if (response.code() == 403) {
                    throw ForbiddenException(response.code(), response.message())
                } else {
                    throw ApiException(response.code(), response.message())
                }
            } else {
                postDao.removeById(id)
            }
        } catch (e: ForbiddenException) {
            throw ForbiddenException(e.code, e.info)
        } catch (e: IOException) {
            throw NetworkException
        } catch (e: Exception) {
            throw UnknownException
        }
    }

    override suspend fun save(post: Post) {
        try {
            val response = postApiService.save(post)
            if (!response.isSuccessful) {
                if (response.code() == 403) {
                    throw ForbiddenException(response.code(), response.message())
                } else {
                    throw ApiException(response.code(), response.message())
                }
            } else {
                val body = response.body() ?: throw ApiException(response.code(), response.message())
                postDao.insert(PostEntity.fromDto(body))
            }
        } catch (e: ForbiddenException) {
            throw ForbiddenException(e.code, e.info)
        } catch (e: IOException) {
            throw NetworkException
        } catch (e: Exception) {
            throw UnknownException
        }
    }

    override suspend fun saveWithAttachment(
        inputStream: InputStream,
        type: AttachmentType,
        post: Post
    ) {
        try {
            val media = upload(inputStream)
            val response = postApiService.save(
                post.copy(
                    attachment = Attachment(
                        url = media.url,
                        type
                    )
                )
            )

            if (!response.isSuccessful) {
                if (response.code() == 403) {
                    throw ForbiddenException(response.code(), response.message())
                } else {
                    throw ApiException(response.code(), response.message())
                }
            }

            val body = response.body() ?: throw ApiException(response.code(), response.message())
            postDao.insert(PostEntity.fromDto(body))

        } catch (e: ForbiddenException) {
            throw ForbiddenException(e.code, e.info)
        } catch (e: IOException) {
            throw NetworkException
        } catch (e: Exception) {
            throw UnknownException
        }
    }

    private suspend fun upload(upload: InputStream): Media {
        val data = MultipartBody.Part.createFormData(
            "file", "name", upload.readBytes()
                .toRequestBody("*/*".toMediaTypeOrNull())
        )
        val response = postApiService.saveMedia(data)
        return response.body() ?: throw ApiException(response.code(), response.message())
    }

    override suspend fun getUserById(id: Int): User {
        try {
            val response = postApiService.getUserById(id)
            if (!response.isSuccessful) {
                if (response.code() == 403) {
                    throw ForbiddenException(response.code(), response.message())
                } else {
                    throw ApiException(response.code(), response.message())
                }
            }
            return response.body() ?: throw ApiException(response.code(), response.message())
        } catch (e: ForbiddenException) {
            throw ForbiddenException(e.code, e.info)
        } catch (e: IOException) {
            throw NetworkException
        } catch (e: Exception) {
            throw UnknownException
        }
    }

}