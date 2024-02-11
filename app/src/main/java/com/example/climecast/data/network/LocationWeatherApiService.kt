package com.example.climecast.data.network

import com.example.climecast.data.network.response.LocationWeatherResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface LocationWeatherApiService {

    @Headers("accept: application/json")
    @GET("weather/realtime")
    suspend fun getLocationWeather(
        @Query("location") location: String,
        @Query("apikey") apiKey: String
    ): LocationWeatherResponse?
}