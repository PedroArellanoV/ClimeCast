package com.example.climecast.data.network.response

import com.example.climecast.domain.model.ForecastResponseModel
import com.example.climecast.domain.model.HourlyResponseModel
import com.example.climecast.domain.model.HourlyValuesModel
import com.example.climecast.domain.model.TimelinesModel
import com.google.gson.annotations.SerializedName

data class LocationForecastResponse(
    @SerializedName("timelines") val timelines: Timelines
)

data class Timelines(
    @SerializedName("hourly") val hourly: List<HourlyResponse>
)

data class HourlyResponse(
    @SerializedName("time") val time: String,
    @SerializedName("values") val values: HourlyValues
)

data class HourlyValues(
    @SerializedName("cloudCover") val cloudCover: Double,
    @SerializedName("rainIntensity") val rainIntensity: Double,
    @SerializedName("snowIntensity") val snowIntensity: Double,
    @SerializedName("temperature") val temperature: Double,
    @SerializedName("temperatureApparent") val temperatureApparent: Double
)

fun LocationForecastResponse.toDomain(): ForecastResponseModel {
    return ForecastResponseModel(timelines.toDomain())
}

fun Timelines.toDomain(): TimelinesModel {
    return TimelinesModel(hourly.toDomain())
}

fun List<HourlyResponse>.toDomain(): List<HourlyResponseModel> {
    return map { it.toDomain() }
}

fun HourlyResponse.toDomain(): HourlyResponseModel {
    return HourlyResponseModel(time, values.toDomain())
}

fun HourlyValues.toDomain(): HourlyValuesModel {
    return HourlyValuesModel(
        cloudCover,
        rainIntensity,
        snowIntensity,
        temperature,
        temperatureApparent
    )
}