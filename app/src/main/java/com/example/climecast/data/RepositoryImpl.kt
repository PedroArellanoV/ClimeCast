package com.example.climecast.data

import android.util.Log
import com.example.climecast.data.network.LocationWeatherApiService
import com.example.climecast.data.network.response.toDomain
import com.example.climecast.domain.Repository
import com.example.climecast.domain.model.ForecastResponseModel
import com.example.climecast.domain.model.RealtimeWeatherModel
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val apiService: LocationWeatherApiService) :
    Repository {

    private val apiKey = ""

    override suspend fun getRealtimeWeather(
        location: String
    ): RealtimeWeatherModel? {
        runCatching { apiService.getLocationWeather(location, apiKey) }
            .onSuccess { return it?.toDomain() }
            .onFailure { Log.i("api_error", "${it.message}") }
        return null
    }

    override suspend fun getWeatherForecast(location: String, timesteps: String): ForecastResponseModel? {
        runCatching { apiService.getForecast(location, timesteps, apiKey) }
            .onSuccess { return it.toDomain() }
            .onFailure { Log.i("api_error", "${it.message}") }
        return null
    }
}