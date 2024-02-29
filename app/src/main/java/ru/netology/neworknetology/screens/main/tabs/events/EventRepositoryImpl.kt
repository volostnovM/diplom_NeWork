package ru.netology.neworknetology.screens.main.tabs.events

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.netology.neworknetology.api.EventApiService
import ru.netology.neworknetology.dao.EventDao
import ru.netology.neworknetology.dto.Event
import ru.netology.neworknetology.entity.EventEntity
import ru.netology.neworknetology.entity.toDto
import ru.netology.neworknetology.entity.toEventEntity
import ru.netology.neworknetology.model.ApiException
import ru.netology.neworknetology.model.ForbiddenException
import ru.netology.neworknetology.model.NetworkException
import ru.netology.neworknetology.model.UnknownException
import ru.netology.neworknetology.model.enums.AttachmentType
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val eventApiService: EventApiService,
    private val eventDao: EventDao
) : EventRepository {

    override val data: Flow<List<Event>> =
        eventDao.getAllEvent()
            .map { it.toDto() }
            .flowOn(Dispatchers.Default)

    override suspend fun getAllEvent() {
        try {
            eventDao.getAllEvent()
            val response = eventApiService.getAllEvent()
            if (!response.isSuccessful) {
                throw ApiException(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiException(response.code(), response.message())
            eventDao.insert(body.toEventEntity())
        } catch (e: IOException) {
            throw NetworkException
        } catch (e: Exception) {
            throw UnknownException
        }
    }

    override suspend fun likeEventById(id: Int) {
        try {
            val response = eventApiService.likeEventById(id)
            val body = response.body() ?: throw ApiException(response.code(), response.message())
            eventDao.insert(EventEntity.fromDto(body.copy(likedByMe = true)))
            if (!response.isSuccessful) {
                throw ApiException(response.code(), response.message())
            }
        } catch (e: IOException) {
            throw NetworkException
        } catch (e: Exception) {
            throw UnknownException
        }
    }

    override suspend fun dislikeEventById(id: Int) {
        try {
            val response = eventApiService.dislikeById(id)
            val body = response.body() ?: throw ApiException(response.code(), response.message())
            eventDao.insert(EventEntity.fromDto(body.copy(likedByMe = false)))
            if (!response.isSuccessful) {
                throw ApiException(response.code(), response.message())
            }
        } catch (e: IOException) {
            throw NetworkException
        } catch (e: Exception) {
            throw UnknownException
        }
    }

    override suspend fun removeEventById(id: Int) {
        try {
            val response = eventApiService.removeById(id)
            if (!response.isSuccessful) {
                if (response.code() == 403) {
                    throw ForbiddenException(response.code(), response.message())
                } else {
                    throw ApiException(response.code(), response.message())
                }
            } else {
                eventDao.removeById(id)
            }

        } catch (e: ForbiddenException) {
            throw ForbiddenException(e.code, e.info)
        } catch (e: IOException) {
            throw NetworkException
        } catch (e: Exception) {
            throw UnknownException
        }
    }

    override suspend fun saveEvent(event: Event) {
        TODO("Not yet implemented")
    }

    override suspend fun saveEventWithAttachment(
        inputStream: InputStream,
        type: AttachmentType,
        event: Event
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun takeParticipantsAtEvent(id: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTakingParticipants(id: Int) {
        TODO("Not yet implemented")
    }


}