package com.example.climecast.domain.usecase

import com.example.climecast.domain.Repository
import javax.inject.Inject

class GetRealtimeWeatherUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(latitude: String, longitude: String) =
        repository.getRealtimeWeather(latitude, longitude)
}