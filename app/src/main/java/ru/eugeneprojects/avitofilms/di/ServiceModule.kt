package ru.eugeneprojects.avitofilms.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.eugeneprojects.avitofilms.api.KinopoiskAPI
import ru.eugeneprojects.avitofilms.utils.Constants
import java.lang.reflect.Type
import java.time.OffsetDateTime
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
internal object ServiceModule {

    @Provides
    fun provideMoviesApi(gson: Gson): KinopoiskAPI = Retrofit.Builder()
        .client(OkHttpClient.Builder()
            .readTimeout(1, TimeUnit.MINUTES)
            .connectTimeout(1, TimeUnit.SECONDS)
            .build())
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(KinopoiskAPI::class.java)

    @Provides
    fun provideGson() = GsonBuilder().apply {
        registerTypeAdapter(OffsetDateTime::class.java, object: JsonDeserializer<OffsetDateTime> {
            override fun deserialize(
                json: JsonElement?,
                typeOfT: Type?,
                context: JsonDeserializationContext?
            ): OffsetDateTime? =
                json?.let{
                    OffsetDateTime.parse(it.asJsonPrimitive.asString)
                }

        })
    }.create()
}