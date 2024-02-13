package com.example.climecast.ui.home

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.climecast.R
import com.example.climecast.databinding.FragmentHomeBinding
import com.example.climecast.domain.model.ForecastPreviewModel
import com.example.climecast.domain.model.HourlyResponseModel
import com.example.climecast.domain.model.HourlyValuesModel
import com.example.climecast.domain.model.RealtimeWeatherModel
import com.example.climecast.ui.home.adapter.ForecastAdapter
import com.example.climecast.ui.utils.ServiceConfirmed
import com.example.climecast.ui.utils.WeatherState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.TimeZone


/*
Cuando el usuario no acepte compartir la ubicación con la aplicación, deshabilitar las CardView de
Realtime Weather y notificaciones.
Dejar habilitada la busqueda de localizaciones.
 */

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val homeViewModel by viewModels<HomeViewModel>()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var forecastAdapter: ForecastAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
    }

    private fun initUI() {
        homeViewModel.getLocation(requireContext())
        drawForecast(emptyList())
        getRealtimeWeather()
        getForecastInfo()

    }

    private fun getForecastInfo() {
        lifecycleScope.launch {
            homeViewModel.forecast.collect {response ->
                confirmForecastService()
                forecastAdapter.updateList(response.map { hourlyResponse ->
                        mapToForecastModel(hourlyResponse)
                    })
                }
        }
    }

    private fun confirmForecastService() {
        when(homeViewModel.forecastConfirmed.value) {
            ServiceConfirmed.NOT_FOUND -> {}
            ServiceConfirmed.CONFIRMED -> binding.pbForecast.isVisible = false
            ServiceConfirmed.LOADING -> binding.pbForecast.isVisible = true
            ServiceConfirmed.REQUIRED -> {
                binding.pbForecast.isVisible = false
                binding.forecastRequirePermissions.isVisible = true
            }
        }
    }

    private fun getRealtimeWeather(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.realtimeWeather.collect() { response ->
                    realtimeWeatherCard(response, homeViewModel.serviceConfirm.value)
                }
            }
        }
    }

    private fun realtimeWeatherCard(
        realtimeResponse: RealtimeWeatherModel?,
        serviceConfirm: ServiceConfirmed
    ) {

        when {
            realtimeResponse != null && serviceConfirm == ServiceConfirmed.CONFIRMED -> {
                binding.pbWeather.isVisible = false
                binding.tvLocationName.text = homeViewModel.locationName.value
                binding.tvLocationTemp.text =
                    "${realtimeResponse.data?.values?.temperature?.toInt()}ºC"
                binding.tvHumidity.text =
                    "Humidity ${realtimeResponse.data?.values?.humidity?.toInt()}%"
                binding.tvWind.text =
                    "Wind ${realtimeResponse.data?.values?.windSpeed?.toInt()}km/h"
                binding.tvPrecipitationProb.text = "Precipitation ${
                    realtimeResponse.data?.values?.precipitationProb?.toInt()
                }%"
                drawWeatherIcon(realtimeResponse)
            }

            serviceConfirm == ServiceConfirmed.NOT_FOUND -> {
                val dialog = AlertDialog.Builder(requireContext()).setTitle(R.string.alertTitle)
                    .setMessage(R.string.alertDescription)
                    .setPositiveButton("Ok") { dialog, _ ->
                        dialog.dismiss()
                    }.create()

                dialog.show()
            }

            serviceConfirm == ServiceConfirmed.REQUIRED -> {
                binding.requirePermissions.isVisible = true
                binding.pbWeather.isVisible = false
            }

            serviceConfirm == ServiceConfirmed.LOADING -> {
                binding.requirePermissions.isVisible = false
            }
        }
    }

    private fun drawWeatherIcon(realtimeResponse: RealtimeWeatherModel) {

        val calendar = Calendar.getInstance()
        val timeZone = TimeZone.getDefault()
        calendar.timeZone = timeZone
        val localtime = calendar

        val response = realtimeResponse.data?.values

        binding.tvHour.text = "${localtime.time.hours}:00hs"

        when {
            response?.cloudCover!! <= 30 && response.rainIntensity <= 10 && response.snowIntensity <= 10 && localtime.time.hours <= 19 -> binding.imgWeatherState.setImageResource(
                WeatherState.ClearDay.icon
            )

            response.cloudCover <= 30 && response.rainIntensity <= 10 && response.snowIntensity <= 10 && localtime.time.hours > 19 -> binding.imgWeatherState.setImageResource(
                WeatherState.ClearNight.icon
            )

            response.cloudCover > 30 && response.rainIntensity <= 10 && response.snowIntensity <= 10 -> binding.imgWeatherState.setImageResource(
                WeatherState.Cloudy.icon
            )

            response.cloudCover <= 30 && response.rainIntensity > 10 && response.snowIntensity <= 10 && localtime.time.hours <= 19 -> binding.imgWeatherState.setImageResource(
                WeatherState.SunnyRain.icon
            )

            response.cloudCover <= 30 && response.rainIntensity > 10 && response.snowIntensity <= 10 && localtime.time.hours > 19 -> binding.imgWeatherState.setImageResource(
                WeatherState.NightRain.icon
            )

            response.cloudCover > 30 && response.rainIntensity > 10 && response.snowIntensity <= 10 -> binding.imgWeatherState.setImageResource(
                WeatherState.Cloudy.icon
            )

            response.cloudCover <= 30 && response.rainIntensity > 10 && response.snowIntensity <= 10 && localtime.time.hours <= 19 -> binding.imgWeatherState.setImageResource(
                WeatherState.SunnyRain.icon
            )

            response.cloudCover <= 30 && response.rainIntensity > 10 && response.snowIntensity <= 10 && localtime.time.hours > 19 -> binding.imgWeatherState.setImageResource(
                WeatherState.NightRain.icon
            )

            response.cloudCover > 30 && response.rainIntensity > 10 && response.snowIntensity <= 10 -> binding.imgWeatherState.setImageResource(
                WeatherState.CloudyRain.icon
            )

            response.cloudCover <= 30 && response.rainIntensity <= 10 && response.snowIntensity > 10 && localtime.time.hours <= 19 -> binding.imgWeatherState.setImageResource(
                WeatherState.SunnySnow.icon
            )

            response.cloudCover <= 30 && response.rainIntensity <= 10 && response.snowIntensity > 10 && localtime.time.hours > 19 -> binding.imgWeatherState.setImageResource(
                WeatherState.NightSnow.icon
            )

            response.cloudCover > 30 && response.rainIntensity <= 10 && response.snowIntensity > 10 -> binding.imgWeatherState.setImageResource(
                WeatherState.CloudySnow.icon
            )
        }
    }

    private fun drawForecast(forecastList: List<HourlyResponseModel>) {
        val forecastPreviewList = forecastList.map { hourlyResponse ->
            mapToForecastModel(hourlyResponse)
        }

        forecastAdapter = ForecastAdapter(forecastPreviewList)
        binding.rvForecast.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = forecastAdapter
        }
    }

    private fun mapToForecastModel(hourlyResponse: HourlyResponseModel): ForecastPreviewModel {
        val weatherState = determineWeatherState(hourlyResponse.values)
        return ForecastPreviewModel(
            temperature = hourlyResponse.values.temperature.toInt(),
            hour = transformDate(hourlyResponse.time),
            weatherState = weatherState
        )
    }

    private fun determineWeatherState(values: HourlyValuesModel): Int {
        return when {
            values.cloudCover <= 30 && values.rainIntensity <= 10 && values.snowIntensity <= 10 -> WeatherState.ClearDay.icon
            values.cloudCover > 30 && values.rainIntensity <= 10 && values.snowIntensity <= 10 -> WeatherState.Cloudy.icon
            values.cloudCover <= 30 && values.rainIntensity > 10 && values.snowIntensity <= 10 -> WeatherState.SunnyRain.icon
            values.cloudCover > 30 && values.rainIntensity > 10 && values.snowIntensity <= 10 -> WeatherState.Cloudy.icon
            values.cloudCover <= 30 && values.rainIntensity > 10 && values.snowIntensity <= 10 -> WeatherState.SunnyRain.icon
            values.cloudCover > 30 && values.rainIntensity > 10 && values.snowIntensity <= 10 -> WeatherState.CloudyRain.icon
            values.cloudCover <= 30 && values.rainIntensity <= 10 && values.snowIntensity > 10 -> WeatherState.SunnySnow.icon
            values.cloudCover > 30 && values.rainIntensity <= 10 && values.snowIntensity > 10 -> WeatherState.CloudySnow.icon
            else -> {
                return WeatherState.ClearDay.icon
            }
        }
    }

    fun transformDate(dateText: String): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        val date = sdf.parse(dateText)
        return SimpleDateFormat("HH:mm").format(date)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}