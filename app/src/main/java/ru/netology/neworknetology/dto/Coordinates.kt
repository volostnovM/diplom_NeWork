package ru.netology.neworknetology.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Coordinates(
    val lat: String,
    val long: String
): Parcelable