package com.example.climecast.domain.model

import androidx.annotation.DrawableRes

data class ForecastPreviewModel(
    val temperature: Int,
    val hour: String,
    @DrawableRes val weatherState: Int
)
