package com.example.climecast.domain.model

data class RealtimeWeatherModel(
    val data: Data?
)
data class Data(
    val time: String?,
    val values: Weather?,
    val locationData: Location?
)

data class Weather(
    val cloudBase: Double?,
    val humidity: Double?,
    val precipitationProb: Double?,
    val temperature: Double?,
    val temperatureApparent: Double?,
    val windSpeed: Double?
)

data class Location(
    val name: String?
)