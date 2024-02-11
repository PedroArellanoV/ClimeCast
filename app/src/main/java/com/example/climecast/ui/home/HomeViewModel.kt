package com.example.climecast.ui.home

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.climecast.domain.model.LocationModel
import com.example.climecast.domain.model.RealtimeWeatherModel
import com.example.climecast.domain.usecase.GetRealtimeWeatherUseCase
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

@HiltViewModel
class HomeViewModel @Inject constructor(private val getRealtimeWeatherUseCase: GetRealtimeWeatherUseCase) :
    ViewModel() {

    private val _locationData = MutableStateFlow<LocationModel?>(null)
    val locationData: StateFlow<LocationModel?> = _locationData

    private var _realtimeWeather = MutableStateFlow<RealtimeWeatherModel?>(null)
    val realtimeWeather: StateFlow<RealtimeWeatherModel?> = _realtimeWeather

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient


    fun getLocation(context: Context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(context)
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    _locationData.value = LocationModel(location.latitude, location.longitude)
                    getLocationWeather()
                    Log.d("pedro_location", "${locationData.value}")
                } else {

                }

            }.addOnFailureListener {

            }
        } else {

        }
    }

    private fun getLocationWeather() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                getRealtimeWeatherUseCase(
                    locationData.value!!.latitude.toString(),
                    locationData.value!!.longitude.toString()
                )
            }
            if(result != null){
                _realtimeWeather.value = result
            }else{}
        }
    }
}