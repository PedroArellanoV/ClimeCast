package com.example.climecast.domain.model


data class ForecastResponseModel (
    val timelines: TimelinesModel
)

data class TimelinesModel(
    val hourly: List<HourlyResponseModel>
)

data class HourlyResponseModel(
    val time: String,
    val values: HourlyValuesModel
)

data class HourlyValuesModel(
    val cloudCover: Double,
    val rainIntensity: Double,
    val snowIntensity: Double,
    val temperature: Double,
    val temperatureApparent: Double
)
