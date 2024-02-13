package com.example.climecast.ui.home

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.climecast.domain.model.HourlyResponseModel
import com.example.climecast.domain.model.LocationModel
import com.example.climecast.domain.model.RealtimeWeatherModel
import com.example.climecast.domain.usecase.GetForecastUseCase
import com.example.climecast.domain.usecase.GetRealtimeWeatherUseCase
import com.example.climecast.ui.utils.ServicedConfirmed
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getRealtimeWeatherUseCase: GetRealtimeWeatherUseCase,
    private val getForecastUseCase: GetForecastUseCase
) :
    ViewModel() {

    private val _locationData = MutableStateFlow<LocationModel?>(null)
    val locationData: StateFlow<LocationModel?> = _locationData

    private var _realtimeWeather = MutableStateFlow<RealtimeWeatherModel?>(null)
    val realtimeWeather: StateFlow<RealtimeWeatherModel?> = _realtimeWeather

    private var _forecast = MutableStateFlow<List<HourlyResponseModel>>(emptyList())
    val forecast: StateFlow<List<HourlyResponseModel>> = _forecast

    private var _locationName = MutableStateFlow("")
    val locationName: StateFlow<String> = _locationName

    private var _serviceConfirmed = MutableStateFlow(ServicedConfirmed.LOADING)
    val serviceConfirm: StateFlow<ServicedConfirmed> = _serviceConfirmed

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient


    fun getLocation(context: Context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {

            fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(context)
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    getLocationName(context, location.latitude, location.longitude)
                    _locationData.value = LocationModel(location.latitude, location.longitude)
                    getLocationWeather(location.latitude, location.longitude)
                    getForecast(location.latitude, location.longitude)
                    Log.d("pedro_location", "${locationData.value}")
                    Log.d("pedro_forecast", "${forecast.value}")
                    _serviceConfirmed.value = ServicedConfirmed.CONFIRMED
                } else {
                }
            }.addOnFailureListener {
                _serviceConfirmed.value = ServicedConfirmed.NOT_FOUND
            }
        } else {
            _serviceConfirmed.value = ServicedConfirmed.REQUIRED
        }
    }

    private fun getLocationName(context: Context, latitude: Double, longitude: Double) {
        val geocoder = Geocoder(context)
        try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses != null) {
                if (addresses.isNotEmpty()) {
                    val address = addresses[0]
                    _locationName.value = "${address.locality}, ${address.countryName}"
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun getLocationWeather(latitude: Double, longitude: Double) {
        val location = "$latitude, $longitude"
        Log.d("location_pedro", location)
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                getRealtimeWeatherUseCase(location)
            }
            if (result != null) {
                _realtimeWeather.value = result
                Log.d("pedro_weatherdetails", "${realtimeWeather.value}")
            } else {

            }
        }
    }

    private fun getForecast(latitude: Double, longitude: Double) {
        val location = "$latitude, $longitude"
        Log.d("revice_location", location)
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                getForecastUseCase(location, "1h")
            }
            if (result != null) {
                _forecast.value = result.timelines.hourly.take(24)
                Log.d("pedro_forecastdetails", "${forecast.value}")

            }
        }
    }

}

