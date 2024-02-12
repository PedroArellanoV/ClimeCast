package com.example.climecast.domain.usecase

import com.example.climecast.domain.Repository
import javax.inject.Inject

class GetForecastUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(location: String, timesteps: String) =
        repository.getWeatherForecast(location, timesteps)
}