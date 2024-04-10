package ru.eugeneprojects.avitofilms.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.eugeneprojects.avitofilms.api.KinopoiskAPI
import ru.eugeneprojects.avitofilms.utils.Constants
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
internal object ServiceModule {

    @Provides
    fun provideMoviesApi(): KinopoiskAPI = Retrofit.Builder()
        .client(OkHttpClient.Builder()
            .readTimeout(1, TimeUnit.MINUTES)
            .connectTimeout(1, TimeUnit.SECONDS)
            .build())
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(KinopoiskAPI::class.java)
}