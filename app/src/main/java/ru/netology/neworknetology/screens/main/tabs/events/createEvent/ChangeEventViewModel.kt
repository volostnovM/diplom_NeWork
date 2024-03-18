package ru.netology.neworknetology.screens.main.tabs.events.createEvent

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.netology.neworknetology.dto.Attachment
import ru.netology.neworknetology.dto.Event
import ru.netology.neworknetology.model.StateModel
import ru.netology.neworknetology.model.enums.AttachmentType
import ru.netology.neworknetology.model.enums.EventType
import ru.netology.neworknetology.model.media.MediaModel
import ru.netology.neworknetology.repository.draft.DraftNewEventRepository
import ru.netology.neworknetology.screens.main.tabs.events.EventRepository
import ru.netology.neworknetology.utils.MutableUnitLiveEvent
import ru.netology.neworknetology.utils.publishEvent
import ru.netology.neworknetology.utils.share
import java.io.InputStream
import java.time.OffsetDateTime
import javax.inject.Inject

val emptyEvent = Event(
    id = 0,
    authorId = 0,
    author = "",
    authorJob = null,
    authorAvatar = null,
    content = "",
    datetime = OffsetDateTime.now().toString(),
    published = OffsetDateTime.now().toString(),
    coords = null,
    type = EventType.ONLINE,
    likeOwnerIds = listOf(),
    likedByMe = false,
    speakerIds = listOf(),
    participantsIds = listOf(),
    participatedByMe = false,
    attachment = null,
    link = null,
)

@HiltViewModel
class ChangeEventViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val draftRepository: DraftNewEventRepository,
)  : ViewModel() {

    private val _editedEvent = MutableLiveData(emptyEvent)
    val editedEvent: LiveData<Event> = _editedEvent

    private val _dataState = MutableLiveData<StateModel>()
    val dataState: LiveData<StateModel> = _dataState.share()

    private val _mediaState = MutableLiveData<MediaModel?>()
    val mediaState: LiveData<MediaModel?> = _mediaState.share()

    private val _navigateUpEvent = MutableUnitLiveEvent()
    val navigateToUpEvent = _navigateUpEvent.share()

    fun changeMedia(uri: Uri?, inputStream: InputStream?, type: AttachmentType?) {
        _mediaState.value = MediaModel(uri, inputStream, type)
    }

    fun setEditedEvent(event: Event) {
        _editedEvent.value = event
    }

    fun getEditedEvent(): Event? {
        return _editedEvent.value
    }

    fun saveEvent(content: String) {
        val text = content.trim()

        _editedEvent.value = _editedEvent.value?.copy(content = text)

        _editedEvent.value?.let {event ->
            viewModelScope.launch {
                try {
                    _mediaState.value?.let { media ->
                        media.inputStream?.let { eventRepository.saveEventWithAttachment(it,media.type!!, event) }
                    } ?: eventRepository.saveEvent(event)
                    _dataState.value = StateModel()
                    _navigateUpEvent.publishEvent()
                } catch (e: Exception) {
                    _dataState.value = StateModel(error = true, loading = false)
                }
            }
        }
        _editedEvent.value = emptyEvent
        _mediaState.value = null
        clear()
    }

    fun changeAttachmentPhoto(url: String) {
        if (_editedEvent.value?.attachment?.url == url.trim()) {
            return
        }
        if (url.isBlank()) {
            _editedEvent.value = _editedEvent.value?.copy(attachment = null)
        }
        _editedEvent.value =
            _editedEvent.value?.copy(attachment = Attachment(url.trim(), type = AttachmentType.IMAGE))
    }


    fun clear() {
        _editedEvent.value?.let {
            _editedEvent.value = emptyEvent
        }
    }

    fun getDraftDate(): String {
        return draftRepository.getDraftDate()
    }

    fun getDraftTime(): String {
        return draftRepository.getDraftTime()
    }


    fun saveDraftDate(text: String) {
        draftRepository.saveDraftDate(text)
    }

    fun saveDraftTime(text: String) {
        draftRepository.saveDraftTime(text)
    }

    fun saveDraftType(type: EventType) {
        draftRepository.saveDraftType(type.name)
    }
}