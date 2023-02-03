package com.entertainment.event.ssearch.presentation.ui.dnd_settings

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.entertainment.event.ssearch.presentation.R
import com.entertainment.event.ssearch.presentation.databinding.FragmentDNDSettingsBinding
import com.entertainment.event.ssearch.presentation.models.DNDSettingsEvent
import com.entertainment.event.ssearch.presentation.models.DNDSettingsState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DNDSettingsFragment : Fragment(R.layout.fragment_d_n_d_settings) {

    private val binding: FragmentDNDSettingsBinding by viewBinding()

    private val viewModel: DNDSettingsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        observeState()
    }

    private fun observeState() {
        lifecycleScope.launch {
            viewModel.screenState.collect { state ->
                renderState(state)
            }
        }
    }

    private fun renderState(state: DNDSettingsState) {
        with(state) {
            binding.switchDndAutoMode.isChecked = isAutoModeSwitched
            binding.groupTimePicker.isVisible = (state.timeEndSelected || state.timeStartSelected)
            binding.tvTimeStart.text = endTime.toString()
            binding.tvTimeEnd.text = startTime.toString()
        }
    }

    private fun initListeners() {
        with(binding) {
            dayPicker.setOnChosenDaysListener {  }
            btnGoBack.setOnClickListener { findNavController().popBackStack() }
            switchDndAutoMode.setOnClickListener {
                viewModel.obtainEvent(DNDSettingsEvent.SetAutoModeDND(it.isActivated))
            }
            binding.tvTimeStart.setOnClickListener {
                viewModel.obtainEvent(DNDSettingsEvent.SelectStartTime)
            }
            binding.tvTimeEnd.setOnClickListener {
                viewModel.obtainEvent(DNDSettingsEvent.SelectEndTime)
            }
            binding.btnSave.setOnClickListener {
                viewModel.obtainEvent(DNDSettingsEvent.SaveTime(20)) //TODO
            }
        }
    }

    private fun initNumberPicker() {
        with(binding) {
        }
    }

}