package com.example.climecast.ui.request

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.climecast.R
import com.example.climecast.databinding.FragmentRequestBinding

class RequestFragment : Fragment() {

    private var _binding: FragmentRequestBinding? = null
    private val binding get() = _binding!!

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRequestBinding.inflate(layoutInflater, container, false)

        initLauncher()
        onButtonClicked()

        return binding.root
    }

    private fun initLauncher() {
        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    onAcceptedLocationPermission()
                } else {
                }
            }
    }

    private fun onButtonClicked() {
        binding.btnRequest.setOnClickListener{
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
    }

    private fun onAcceptedLocationPermission(){
        val fadeOutAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_out_screen)
        view?.startAnimation(fadeOutAnimation)
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.remove(this)
        fragmentTransaction.commit()
        Log.d("RequestFragment", "Permisos de ubicación concedidos")
    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}