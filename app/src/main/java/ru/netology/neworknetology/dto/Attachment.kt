package ru.netology.neworknetology.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import ru.netology.neworknetology.model.enums.AttachmentType

@Parcelize
data class Attachment(
    val url: String,
    val type: AttachmentType
): Parcelable