package ru.netology.neworknetology.screens.main.tabs.events

import kotlinx.coroutines.flow.Flow
import ru.netology.neworknetology.dto.Event
import ru.netology.neworknetology.model.enums.AttachmentType
import java.io.InputStream

interface EventRepository {
    val data: Flow<List<Event>>

    suspend fun getAllEvent()
    suspend fun likeEventById(id: Int)
    suspend fun dislikeEventById(id: Int)
    suspend fun removeEventById(id: Int)
    suspend fun saveEvent(event: Event)
    suspend fun saveEventWithAttachment(inputStream: InputStream, type: AttachmentType, event: Event)
    suspend fun takeParticipantsAtEvent(id:Int)
    suspend fun deleteTakingParticipants(id:Int)
}