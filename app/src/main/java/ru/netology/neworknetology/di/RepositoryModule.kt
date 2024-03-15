package ru.netology.neworknetology.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.netology.neworknetology.repository.draft.DraftNewEventRepository
import ru.netology.neworknetology.repository.draft.DraftNewEventRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindsDraftNewEventRepository(impl: DraftNewEventRepositoryImpl): DraftNewEventRepository
}