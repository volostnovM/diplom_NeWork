package ru.netology.neworknetology.entity.tuples

import ru.netology.neworknetology.dto.Coordinates

data class CoordinatesTuple (
    val latitude: String,
    val longitude: String
) {
    fun toDto() = Coordinates(latitude, longitude)

    companion object {
        fun fromDto(dto: Coordinates?) = dto?.let {
            CoordinatesTuple(it.lat, it.long)
        }
    }
}