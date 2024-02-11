package com.example.climecast.data.network.response

import com.example.climecast.domain.model.Data
import com.example.climecast.domain.model.Location
import com.example.climecast.domain.model.RealtimeWeatherModel
import com.example.climecast.domain.model.Weather
import com.google.gson.annotations.SerializedName

data class LocationWeatherResponse(
    @SerializedName("data") val data : DataResponse?
)

data class DataResponse(
    @SerializedName("time") val time: String?,
    @SerializedName("values") val values: WeatherValues?,
    @SerializedName("location") val locationData: LocationData?
)

data class WeatherValues(
    @SerializedName("cloudBase") val cloudBase: Double?,
    @SerializedName("humidity") val humidity: Double?,
    @SerializedName("precipitationProbability") val precipitationProb: Double?,
    @SerializedName("temperature") val temperature: Double?,
    @SerializedName("temperatureApparent") val temperatureApparent: Double?,
    @SerializedName("windSpeed") val windSpeed: Double?
)

data class LocationData(
    @SerializedName("name") val name: String?
)


fun LocationWeatherResponse.toDomain(): RealtimeWeatherModel {
    return RealtimeWeatherModel(
        data?.toDomain()
    )
}

fun DataResponse.toDomain(): Data {
    return Data(
        time,
        values?.toDomain(),
        locationData?.toDomain()
    )
}

fun WeatherValues.toDomain(): Weather {
    return Weather(
        cloudBase,
        humidity,
        precipitationProb,
        temperature,
        temperatureApparent,
        windSpeed
    )
}

fun LocationData.toDomain(): Location {
    return Location(
        name
    )
}