package ru.netology.neworknetology.model.event

import android.net.Uri
import ru.netology.neworknetology.model.enums.AttachmentType
import java.io.File

data class AttachmentModel(
    val attachmentType: AttachmentType,
    val uri: Uri,
    val file: File,
)