package ru.netology.neworknetology.model.media

import android.net.Uri
import ru.netology.neworknetology.model.enums.AttachmentType
import java.io.InputStream

data class MediaModel(
    val uri: Uri? = null,
    val inputStream: InputStream? = null,
    val type: AttachmentType? = null
)