package com.example.climecast.ui.utils

import androidx.annotation.DrawableRes
import com.example.climecast.R
import java.util.Calendar
import java.util.TimeZone

enum class ServicedConfirmed {
    CONFIRMED, REQUIRED, NOT_FOUND, LOADING
}

sealed class WeatherState(@DrawableRes val icon: Int){
    data object ClearDay: WeatherState(R.drawable.ic_day)
    data object ClearNight: WeatherState(R.drawable.ic_night)
    data object Cloudy: WeatherState(R.drawable.ic_cloudy)
    data object SunnyRain: WeatherState(R.drawable.ic_rain_day)
    data object NightRain: WeatherState(R.drawable.ic_rain_night)
    data object CloudyRain: WeatherState(R.drawable.ic_cloud_rain)
    data object NightSnow: WeatherState(R.drawable.ic_snow_night)
    data object SunnySnow: WeatherState(R.drawable.ic_snow_day)
    data object CloudySnow: WeatherState(R.drawable.ic_cloud_snow)
}

