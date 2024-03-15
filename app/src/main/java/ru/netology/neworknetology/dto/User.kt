package ru.netology.neworknetology.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class User(
    val id: Int,
    val login: String,
    val name: String,
    val avatar: String?,
    ):Parcelable