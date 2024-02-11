package com.example.climecast.data.network

import com.example.climecast.data.network.response.LocationWeatherResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface LocationWeatherApiService {

    @GET("/weather/realtime/{latitude}/{longitude}")
    suspend fun getLocationWeather(
        @Path("latitude") latitude: String,
        @Path("longitude") longitude: String,
        @Path("apiKey") apiKey: String
    ): LocationWeatherResponse?
}