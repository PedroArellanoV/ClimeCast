package com.example.climecast.data.network

import com.example.climecast.data.network.response.LocationForecastResponse
import com.example.climecast.data.network.response.LocationWeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationWeatherApiService {

    @GET("weather/realtime")
    suspend fun getLocationWeather(
        @Query("location") location: String,
        @Query("apikey") apiKey: String
    ): LocationWeatherResponse?

    @GET("weather/forecast")
    suspend fun getForecast(
        @Query("location") location: String,
        @Query("timesteps") timesteps: String,
        @Query("apikey") apiKey: String
    ): LocationForecastResponse
}