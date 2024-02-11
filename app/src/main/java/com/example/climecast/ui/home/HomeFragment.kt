package com.example.climecast.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.climecast.R
import com.example.climecast.databinding.FragmentHomeBinding
import com.example.climecast.domain.model.RealtimeWeatherModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val homeViewModel by viewModels<HomeViewModel>()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
    }

    private fun initUI() {
        homeViewModel.getLocation(requireContext())
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.realtimeWeather.collect() { response ->
                    realtimeWeatherCard(response)
                }
            }
        }

    }

    private fun realtimeWeatherCard(realtimeResponse: RealtimeWeatherModel?) {
        binding.tvLocationName.text = homeViewModel.locationName.value
        binding.tvLocationTemp.text =
            "${realtimeResponse?.data?.values?.temperature?.toInt().toString()}ºC"
        binding.tvHumidity.text =
            "Humidity ${realtimeResponse?.data?.values?.humidity?.toInt().toString()}%"
        binding.tvWind.text =
            "Wind ${realtimeResponse?.data?.values?.windSpeed?.toInt().toString()}km/h"
        binding.tvPrecipitationProb.text = "Precipitation ${
            realtimeResponse?.data?.values?.precipitationProb?.toInt().toString()
        }%"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}