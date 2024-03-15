package ru.netology.neworknetology.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.netology.neworknetology.screens.main.tabs.events.EventRepository
import ru.netology.neworknetology.screens.main.tabs.events.EventRepositoryImpl
import ru.netology.neworknetology.screens.main.tabs.posts.PostRepository
import ru.netology.neworknetology.screens.main.tabs.posts.PostRepositoryImpl
import ru.netology.neworknetology.screens.main.tabs.users.UserRepository
import ru.netology.neworknetology.screens.main.tabs.users.UserRepositoryImpl
import ru.netology.neworknetology.screens.main.userInfo.job.JobRepository
import ru.netology.neworknetology.screens.main.userInfo.job.JobRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SourcesModule {

    @Singleton
    @Binds
    abstract fun bindsUserRepository(impl: UserRepositoryImpl): UserRepository

    @Singleton
    @Binds
    abstract fun bindsEventRepository(impl: EventRepositoryImpl): EventRepository

    @Singleton
    @Binds
    abstract fun bindsPostRepository(impl: PostRepositoryImpl): PostRepository

    @Singleton
    @Binds
    abstract fun bindsJobRepository(impl: JobRepositoryImpl): JobRepository
}