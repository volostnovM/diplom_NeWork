package ru.netology.neworknetology.dto

import ru.netology.neworknetology.model.enums.AttachmentType

data class Attachment(
    val url: String,
    val type: AttachmentType
)