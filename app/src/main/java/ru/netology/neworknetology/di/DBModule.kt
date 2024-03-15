package ru.netology.neworknetology.di

import android.content.Context
import androidx.room.Room
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.netology.neworknetology.dao.EventDao
import ru.netology.neworknetology.dao.JobDao
import ru.netology.neworknetology.dao.PostDao
import ru.netology.neworknetology.dao.UserDao
import ru.netology.neworknetology.db.AppDb
import ru.netology.neworknetology.utils.Converters
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DBModule {

    @Singleton
    @Provides
    fun provideDb(
        @ApplicationContext
        context: Context,
        moshi: Moshi
    ): AppDb {
        Converters.initialize(moshi)
        return Room.databaseBuilder(context, AppDb::class.java, "app.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideUserDao(
        appDb: AppDb
    ): UserDao = appDb.userDao()

    @Provides
    fun provideEventDao(
        appDb: AppDb
    ): EventDao = appDb.eventDao()

    @Provides
    fun providePostDao(
        appDb: AppDb
    ): PostDao = appDb.postDao()

    @Provides
    fun provideJobDao(
        appDb: AppDb
    ): JobDao = appDb.jobDao()
}