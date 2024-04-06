package ru.eugeneprojects.avitofilms.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.eugeneprojects.avitofilms.data.network.repository.MoviesRepository
import ru.eugeneprojects.avitofilms.data.network.repository.MoviesRepositoryIMPL

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun providesMoviesRepository(repositoryIMPL: MoviesRepositoryIMPL) : MoviesRepository
}