package com.onboarding.presentation.ui.first_steps

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.onboarding.presentation.R
import com.onboarding.presentation.base.BaseFragment
import com.onboarding.presentation.databinding.FragmentAboutDeviceBinding
import com.onboarding.presentation.utils.GetAboutDeviceInfo
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AboutDeviceFragment :
    BaseFragment<FragmentAboutDeviceBinding>(FragmentAboutDeviceBinding::inflate) {

    private lateinit var getAboutInfo: GetAboutDeviceInfo

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getAboutInfo = GetAboutDeviceInfo(requireContext())
        initProgress()
        renderState()
        initClickListener()
    }

    private fun renderState() {
        with(binding) {
            modelPhoneDescription.text = getAboutInfo.getDeviceModel()
            versionAndroidDescription.text = getAboutInfo.getAndroidVersion()
            timeWorkDescription.text = getAboutInfo.getTimeWork()
            cardVersionDescription.text = getAboutInfo.getCardVersion()
            displaySizeDescription.text = getAboutInfo.screenDiagonal()
            screenSizeDescription.text = getAboutInfo.screenSize()
            densityPxDescription.text = getAboutInfo.getPpi()
            batteryChargePercentDescription.text = getAboutInfo.getBatteryChargePercent()
            temperatureBatteryDescription.text = getAboutInfo.getBatteryTemperature()
        }
    }

    private fun initProgress() {
        lifecycleScope.launch {
            for (x in 0..7) {
                binding.progress.progress = x
                delay(1000)
            }
        }
        lifecycleScope.launch {
            for (x in 7 downTo 0) {
                binding.progressTextValue.text = String.format("0:0%d", x)
                delay(1000)
            }
        }
        navigateTo()
    }

    private fun initClickListener() {
        binding.resumeBtn.setOnClickListener {
            navigateTo()
        }
    }

    private fun navigateTo() {
        findNavController().navigate(R.id.action_aboutDeviceFragment_to_requiredOptimizationFragment)
    }
}