package com.example.climecast.ui.home.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.climecast.databinding.ItemForecastBinding
import com.example.climecast.domain.model.ForecastPreviewModel

class ForecastViewHolder(view: View): RecyclerView.ViewHolder(view){
    private val binding = ItemForecastBinding.bind(view)
    fun render(forecastModel: ForecastPreviewModel){
        binding.ivForecastWeather.setImageResource(forecastModel.weatherState)
        binding.tvHoures.text = forecastModel.hour
        binding.tvTemperature.text = "${forecastModel.temperature}ºC"
    }
}