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
import com.example.climecast.R
import com.example.climecast.databinding.FragmentHomeBinding
import com.example.climecast.domain.model.RealtimeWeatherModel
import com.example.climecast.ui.utils.ServicesConfirmed
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
    }

    private fun initUI() {
        homeViewModel.getLocation(requireContext())
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
        serviceConfirm: ServicesConfirmed
    ) {

        when {
            realtimeResponse != null && serviceConfirm == ServicesConfirmed.CONFIRMED -> {
                binding.pbWeather.isVisible = false
                binding.tvLocationName.text = homeViewModel.locationName.value
                binding.tvLocationTemp.text =
                    "${realtimeResponse.data?.values?.temperature?.toInt().toString()}ºC"
                binding.tvHumidity.text =
                    "Humidity ${realtimeResponse.data?.values?.humidity?.toInt().toString()}%"
                binding.tvWind.text =
                    "Wind ${realtimeResponse.data?.values?.windSpeed?.toInt().toString()}km/h"
                binding.tvPrecipitationProb.text = "Precipitation ${
                    realtimeResponse.data?.values?.precipitationProb?.toInt().toString()
                }%"
                binding.tvHour.text = realtimeResponse.data?.time.toString()
            }

            serviceConfirm == ServicesConfirmed.NOT_FOUND -> {
                val dialog = AlertDialog.Builder(requireContext()).setTitle(R.string.alertTitle)
                    .setMessage(R.string.alertDescription)
                    .setPositiveButton("Ok") { dialog, _ ->
                        dialog.dismiss()
                    }.create()

                dialog.show()
            }

            serviceConfirm == ServicesConfirmed.REQUIRED -> {
                binding.requirePermissions.isVisible = true
                binding.pbWeather.isVisible = false
            }

            serviceConfirm == ServicesConfirmed.LOADING -> {
                binding.requirePermissions.isVisible = false
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}