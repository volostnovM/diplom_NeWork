package ru.netology.neworknetology.db

import ru.netology.neworknetology.entity.PostEntity
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.netology.neworknetology.dao.EventDao
import ru.netology.neworknetology.dao.JobDao
import ru.netology.neworknetology.dao.PostDao
import ru.netology.neworknetology.dao.UserDao
import ru.netology.neworknetology.entity.EventEntity
import ru.netology.neworknetology.entity.JobEntity
import ru.netology.neworknetology.entity.UserEntity
import ru.netology.neworknetology.utils.Converters


@Database(
    entities = [
    UserEntity::class,
    EventEntity::class,
    PostEntity::class,
    JobEntity::class
    ], version = 1
)
@TypeConverters(Converters::class)
abstract class AppDb : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun eventDao(): EventDao
    abstract fun postDao(): PostDao
    abstract fun jobDao(): JobDao
}