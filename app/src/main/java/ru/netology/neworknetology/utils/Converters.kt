package ru.netology.neworknetology.utils

import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import ru.netology.neworknetology.dto.Coordinates
import ru.netology.neworknetology.model.enums.AttachmentType

object Converters {
    private lateinit var moshi: Moshi

    fun initialize(moshi: Moshi){
        this.moshi = moshi
    }

    @TypeConverter
    fun toAttachmentType(value: String) = enumValueOf<AttachmentType>(value)

    @TypeConverter
    fun fromAttachmentType(value: AttachmentType) = value.name

    @TypeConverter
    fun fromListDto(list: List<Int>?): String {
        if (list == null) return ""
        return list.toString()
    }


    @TypeConverter
    fun toListDto(data: String?): List<Int>? {
        return if (data == "[]") emptyList()
        else {
            val substr = data?.substring(1, data.length - 1)
            substr?.split(", ")?.map { it.toInt() }
        }
    }


    @TypeConverter
    fun fromCoordinatesDto(coord: Coordinates?): String {
        if (coord == null) return ""
        val adapter = moshi.adapter(Coordinates::class.java)
        return adapter.toJson(coord)
    }

    @TypeConverter
    fun toCoordinatesDto(data: String?): Coordinates? {
        if (data.isNullOrEmpty()) return null
        val adapter = moshi.adapter(Coordinates::class.java)
        return adapter.fromJson(data)
    }

}