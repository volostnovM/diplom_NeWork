package ru.netology.neworknetology.dto

import ru.netology.neworknetology.model.enums.EventType

data class Event (
    val id: Int,
    val authorId: Int,
    val author: String,
    val authorJob: String? = null,
    val authorAvatar: String? = null,
    val content: String,
    val datetime: String,
    val published: String,
    val coords: Coordinates? = null,
    val type: EventType,
    val likeOwnerIds: List<Int> = emptyList(),
    val likedByMe: Boolean = false,
    val speakerIds: List<Int> = emptyList(),
    val participantsIds: List<Int> = emptyList(),
    val participatedByMe: Boolean = false,
    val attachment: Attachment? = null,
    val link: String? = null,
)
