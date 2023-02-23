package com.battery_saving.presentation.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.battery_saving.presentation.base.BaseFragment
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
    }

    private fun initObserver() {
        lifecycleScope.launchWhenResumed {
            viewModel.state.collect { state ->
                withContext(Dispatchers.Main) { renderState(state) }
            }
        }
    }

    private fun renderState(state: BatterySavingState) {
        binding.percentChargeLevel.text =
            resources.getString(R.string.D_percent, state.batteryChargePercent)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.getBatteryPercent().cancel()
    }

}