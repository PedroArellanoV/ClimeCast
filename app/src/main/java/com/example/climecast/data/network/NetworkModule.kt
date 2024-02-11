package com.example.climecast.data.network

import com.example.climecast.data.RepositoryImpl
import com.example.climecast.data.core.RetrofitInterceptor
import com.example.climecast.domain.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://api.tomorrow.io/v4/"


    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit{
        return Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: RetrofitInterceptor): OkHttpClient{
        return OkHttpClient
            .Builder()
            .addInterceptor(authInterceptor)
            .build()
    }


    @Provides
    fun provideLocationWeatherApiService(retrofit: Retrofit): LocationWeatherApiService{
        return retrofit.create(LocationWeatherApiService::class.java)
    }


    @Provides
    fun provideRepository(apiService: LocationWeatherApiService): Repository{
        return RepositoryImpl(apiService)
    }
}