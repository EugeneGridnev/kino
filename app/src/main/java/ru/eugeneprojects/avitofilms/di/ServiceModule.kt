package ru.eugeneprojects.avitofilms.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.eugeneprojects.avitofilms.api.MoviesAPI
import ru.eugeneprojects.avitofilms.utils.Constants

@Module
@InstallIn(SingletonComponent::class)
internal object ServiceModule {

    @Provides
    fun provideProductApi(): MoviesAPI = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(MoviesAPI::class.java)
}