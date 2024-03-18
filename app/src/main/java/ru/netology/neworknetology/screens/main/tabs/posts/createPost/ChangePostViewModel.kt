package ru.netology.neworknetology.screens.main.tabs.posts.createPost

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.netology.neworknetology.dto.Attachment
import ru.netology.neworknetology.dto.Post
import ru.netology.neworknetology.model.StateModel
import ru.netology.neworknetology.model.enums.AttachmentType
import ru.netology.neworknetology.model.media.MediaModel
import ru.netology.neworknetology.screens.main.tabs.posts.PostRepository
import ru.netology.neworknetology.utils.MutableUnitLiveEvent
import ru.netology.neworknetology.utils.publishEvent
import ru.netology.neworknetology.utils.share
import java.io.InputStream
import java.time.OffsetDateTime
import javax.inject.Inject

private val emptyPost = Post(
    id = 0,
    authorId = 0,
    author = "Maxim",
    content = "",
    published = OffsetDateTime.now().toString(),
)

@HiltViewModel
class ChangePostViewModel @Inject constructor(
    private val postRepository: PostRepository,
)  : ViewModel() {
    private val _editedPost = MutableLiveData(emptyPost)
    val editedEvent: LiveData<Post> = _editedPost

    private val _dataState = MutableLiveData<StateModel>()
    val dataState: LiveData<StateModel> = _dataState.share()

    private val _mediaState = MutableLiveData<MediaModel?>()
    val mediaState: LiveData<MediaModel?> = _mediaState.share()

    private val _navigateUpEvent = MutableUnitLiveEvent()
    val navigateToUpEvent = _navigateUpEvent.share()

    fun changeMedia(uri: Uri?, inputStream: InputStream?, type: AttachmentType?) {
        _mediaState.value = MediaModel(uri, inputStream, type)
    }

    fun setEditedPost(post: Post) {
        _editedPost.value = post
    }

    fun getEditedPost(): Post? {
        return _editedPost.value
    }

    fun saveEvent(content: String) {
        val text = content.trim()

        _editedPost.value = _editedPost.value?.copy(content = text)

        _editedPost.value?.let {event ->
            viewModelScope.launch {
                try {
                    _mediaState.value?.let { media ->
                        media.inputStream?.let { postRepository.saveWithAttachment(it,media.type!!, event) }
                    } ?: postRepository.save(event)
                    _dataState.value = StateModel()
                    _navigateUpEvent.publishEvent()
                } catch (e: Exception) {
                    _dataState.value = StateModel(error = true, loading = false)
                }
            }
        }
        _editedPost.value = emptyPost
        _mediaState.value = null
        clear()
    }

    fun changeAttachmentPhoto(url: String) {
        if (_editedPost.value?.attachment?.url == url.trim()) {
            return
        }
        if (url.isBlank()) {
            _editedPost.value = _editedPost.value?.copy(attachment = null)
        }
        _editedPost.value =
            _editedPost.value?.copy(attachment = Attachment(url.trim(), type = AttachmentType.IMAGE))
    }


    fun clear() {
        _editedPost.value?.let {
            _editedPost.value = emptyPost
        }
    }
}