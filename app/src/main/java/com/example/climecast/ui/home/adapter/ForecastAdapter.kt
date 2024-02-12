package com.example.climecast.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.climecast.R
import com.example.climecast.databinding.ItemForecastBinding
import com.example.climecast.domain.model.ForecastPreviewModel

class ForecastAdapter(
    private val forecastList: List<ForecastPreviewModel> = emptyList()
) : RecyclerView.Adapter<ForecastViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        return ForecastViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_forecast, parent, false)
        )
    }

    override fun getItemCount(): Int = forecastList.size

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        holder.render(forecastList[position])
    }


}