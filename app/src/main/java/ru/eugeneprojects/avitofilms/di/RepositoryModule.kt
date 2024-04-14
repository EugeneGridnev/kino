package ru.eugeneprojects.avitofilms.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.eugeneprojects.avitofilms.data.network.connection.ConnectivityRepository
import ru.eugeneprojects.avitofilms.data.network.connection.ConnectivityRepositoryIMPL
import ru.eugeneprojects.avitofilms.data.network.repository.KinopoiskRepository
import ru.eugeneprojects.avitofilms.data.network.repository.KinopoiskRepositoryIMPL

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun providesConnectivityRepository(repositoryIMPL: ConnectivityRepositoryIMPL): ConnectivityRepository

    @Binds
    abstract fun providesKinopoiskRepository(repositoryIMPL: KinopoiskRepositoryIMPL): KinopoiskRepository
}