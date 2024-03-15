package ru.netology.neworknetology.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ru.netology.neworknetology.dto.Post
import ru.netology.neworknetology.entity.tuples.AttachmentTuple
import ru.netology.neworknetology.entity.tuples.CoordinatesTuple
import ru.netology.neworknetology.utils.Converters

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val authorId: Int,
    val author: String,
    val authorAvatar: String?,
    val authorJob: String?,
    val content: String,
    val published: String,
    val link: String?,
    @TypeConverters(Converters::class)
    val likeOwnerIds: List<Int>,
    @TypeConverters(Converters::class)
    val mentionIds: List<Int>,
    val mentionedMe: Boolean,
    val likedByMe: Boolean,
    val ownedByMe: Boolean,
    @Embedded
    var attachment: AttachmentTuple?,
    @Embedded
    @TypeConverters(Converters::class)
    val coords: CoordinatesTuple?
    ) {
    fun toDto() = Post(
        id,
        authorId,
        author,
        authorAvatar,
        authorJob,
        content,
        published,
        coords?.toDto(),
        link,
        likeOwnerIds,
        mentionIds,
        mentionedMe,
        likedByMe,
        attachment?.toDto(),
        ownedByMe,
    )

    companion object {
        fun fromDto(dto: Post) =
            PostEntity(
                dto.id,
                dto.authorId,
                dto.author,
                dto.authorAvatar,
                dto.authorJob,
                dto.content,
                dto.published,
                dto.link,
                dto.likeOwnerIds,
                dto.mentionIds,
                dto.mentionedMe,
                dto.likedByMe,
                dto.ownedByMe,
                AttachmentTuple.fromDto(dto.attachment),
                CoordinatesTuple.fromDto(dto.coords)
                )

    }
}


fun List<PostEntity>.toDto(): List<Post> = map(PostEntity::toDto)
fun List<Post>.toPostEntity(): List<PostEntity> =
    map {
        PostEntity.fromDto(it)
    }