package ru.netology.neworknetology.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.netology.neworknetology.screens.main.tabs.events.EventRepository
import ru.netology.neworknetology.screens.main.tabs.events.EventRepositoryImpl
import ru.netology.neworknetology.screens.main.tabs.users.UserRepository
import ru.netology.neworknetology.screens.main.tabs.users.UserRepositoryImpl
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
}