package ru.netology.neworknetology.entity.tuples

import ru.netology.neworknetology.dto.Attachment
import ru.netology.neworknetology.model.enums.AttachmentType


data class AttachmentTuple(
    val url: String,
    val attachmentType: AttachmentType,
) {
    fun toDto() = Attachment(url, attachmentType)

    companion object {
        fun fromDto(dto: Attachment?) = dto?.let {
            AttachmentTuple(it.url, it.type)
        }
    }
}