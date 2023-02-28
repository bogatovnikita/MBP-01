package com.battery_saving.presentation.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.battery_saving.presentation.base.BaseFragment
import com.battery_saving.presentation.ui.dialog.BatteryScanDialog
import com.hedgehog.battery_saving.presentation.R
import com.hedgehog.battery_saving.presentation.databinding.FragmentBatterySavingBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class BatterySavingFragment :
    BaseFragment<FragmentBatterySavingBinding>(FragmentBatterySavingBinding::inflate) {

    private val viewModel: BatterySavingViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        initCLickListener()
    }

    private fun initObserver() {
        lifecycleScope.launchWhenResumed {
            viewModel.state.collect { state ->
                withContext(Dispatchers.Main) { renderState(state) }
            }
        }
    }

    private fun renderState(state: BatterySavingState) {
        if (!state.isLoading) return
        binding.percentChargeLevel.text =
            resources.getString(R.string.D_percent, state.batteryChargePercent)

        binding.consumptionForHourPercent.text = when (state.consumptionPercent) {
            -1 -> getString(R.string.calculate)
            else -> getString(R.string.D_percent, state.consumptionPercent)
        }
    }

    private fun initCLickListener() {
        binding.backgroundArrowRight.setOnClickListener {
            val args = requireArguments().getInt("toScreenTimeId")
            findNavController().navigate(args)
        }

        binding.backArrow.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.deleteApp.setOnClickListener {
            BatteryScanDialog().show(childFragmentManager, "")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.getBatteryPercent().cancel()
    }

}