package com.example.climecast.domain

import com.example.climecast.domain.model.RealtimeWeatherModel

interface Repository {
    suspend fun getRealtimeWeather(latitude: String, longitude: String): RealtimeWeatherModel?
}