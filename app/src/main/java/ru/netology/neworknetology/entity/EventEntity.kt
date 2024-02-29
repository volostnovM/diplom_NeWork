package ru.netology.neworknetology.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ru.netology.neworknetology.dto.Event
import ru.netology.neworknetology.entity.tuples.AttachmentTuple
import ru.netology.neworknetology.entity.tuples.CoordinatesTuple
import ru.netology.neworknetology.model.enums.EventType
import ru.netology.neworknetology.utils.Converters

@Entity
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val authorId: Int,
    val author: String,
    val authorJob: String?,
    val authorAvatar: String?,
    val content: String,
    val datetime: String,
    val published: String,
    val type: EventType,
    @TypeConverters(Converters::class)
    val likeOwnerIds: List<Int>,
    val likedByMe: Boolean,
    @TypeConverters(Converters::class)
    val speakerIds: List<Int>,
    @TypeConverters(Converters::class)
    val participantsIds: List<Int>,
    val participatedByMe: Boolean,
    val link: String?,
    @Embedded
    val attachment: AttachmentTuple?,
    @Embedded
    @TypeConverters(Converters::class)
    val coords: CoordinatesTuple?
) {
    fun toDto() = Event(
        id,
        authorId,
        author,
        authorJob,
        authorAvatar,
        content,
        datetime,
        published,
        coords?.toDto(),
        type,
        likeOwnerIds,
        likedByMe,
        speakerIds,
        participantsIds,
        participatedByMe,
        attachment?.toDto(),
        link,
    )

    companion object {
        fun fromDto(dto: Event) =
            EventEntity(
                dto.id,
                dto.authorId,
                dto.author,
                dto.authorJob,
                dto.authorAvatar,
                dto.content,
                dto.datetime,
                dto.published,
                dto.type,
                dto.likeOwnerIds,
                dto.likedByMe,
                dto.speakerIds,
                dto.participantsIds,
                dto.participatedByMe,
                dto.link,
                AttachmentTuple.fromDto(dto.attachment),
                CoordinatesTuple.fromDto(dto.coords)
            )
    }
}

fun List<EventEntity>.toDto(): List<Event> = map(EventEntity::toDto)
fun List<Event>.toEventEntity(): List<EventEntity> =
    map {
        EventEntity.fromDto(it)
    }
