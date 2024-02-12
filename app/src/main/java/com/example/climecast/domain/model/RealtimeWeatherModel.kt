package com.example.climecast.domain.model

data class RealtimeWeatherModel(
    val data: Data?
)
data class Data(
    val time: String?,
    val values: Weather?
)

data class Weather(
    val cloudCover: Double,
    val humidity: Double,
    val precipitationProb: Double,
    val temperature: Double,
    val temperatureApparent: Double,
    val windSpeed: Double,
    val rainIntensity: Double,
    val snowIntensity: Double
)