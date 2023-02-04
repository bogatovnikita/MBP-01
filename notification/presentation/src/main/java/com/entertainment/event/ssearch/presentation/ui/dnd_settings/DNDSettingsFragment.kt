package com.entertainment.event.ssearch.presentation.ui.dnd_settings

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.entertainment.event.ssearch.presentation.R
import com.entertainment.event.ssearch.presentation.databinding.FragmentDNDSettingsBinding
import com.entertainment.event.ssearch.presentation.extensions.toHours
import com.entertainment.event.ssearch.presentation.extensions.toMinutes
import com.entertainment.event.ssearch.presentation.extensions.toTime
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
                showToastIfWrongFormat(state)
            }
        }
    }

    private fun renderState(state: DNDSettingsState) {
        with(state) {
            binding.switchDndAutoMode.isChecked = isAutoModeSwitched
            binding.groupTimePicker.isVisible = (state.timeEndSelected || state.timeStartSelected)
            binding.tvTimeStart.text = startTime.toTime()
            binding.tvTimeEnd.text = endTime.toTime()
            setAccessibilityChanges(isAutoModeSwitched, timeStartSelected, timeEndSelected)
            renderSelectedPicker(timeStartSelected, startTime, endTime)
        }
    }

    private fun initListeners() {
        with(binding) {
            dayPicker.setOnChosenDaysListener { selectedDays ->
                viewModel.obtainEvent(DNDSettingsEvent.UpdateSelectedDayList(selectedDays))
            }
            btnGoBack.setOnClickListener { findNavController().popBackStack() }
            switchDndAutoMode.setOnClickListener {
                viewModel.obtainEvent(DNDSettingsEvent.SetAutoModeDND(binding.switchDndAutoMode.isChecked))
            }
            binding.tvTimeStart.setOnClickListener {
                viewModel.obtainEvent(DNDSettingsEvent.SelectStartTime)
            }
            binding.tvTimeEnd.setOnClickListener {
                viewModel.obtainEvent(DNDSettingsEvent.SelectEndTime)
            }
            binding.btnSave.setOnClickListener {
                viewModel.obtainEvent(
                    DNDSettingsEvent.SaveTime(
                        convertToTime(
                            binding.pickerHours.value,
                            binding.pickerMinutes.value,
                        )
                    )
                )
            }
        }
    }

    private fun setAccessibilityChanges(
        isAutoModeSwitched: Boolean,
        timeStartSelected: Boolean,
        timeEndSelected: Boolean
    ) {
        if (!isAutoModeSwitched && !timeStartSelected && !timeEndSelected) {
            binding.groupTimeAndDays.setAlphaAndAvailability(0.5f)
            binding.dayPicker.setAlphaAndAvailability(0.5f)
        } else if (!isAutoModeSwitched && timeStartSelected || !isAutoModeSwitched && timeEndSelected){
            binding.groupTimePicker.isVisible = false
            binding.groupTimeAndDays.setAlphaAndAvailability(0.5f)
            binding.dayPicker.setAlphaAndAvailability(0.5f)
        } else {
            binding.groupTimeAndDays.setAlphaAndAvailability(1f)
            binding.dayPicker.setAlphaAndAvailability(1f)
        }
    }

    private fun renderSelectedPicker(timeStartSelected: Boolean, startTime: Int, endTime: Int) {
        if (timeStartSelected) {
            setTimeToPicker(startTime)
        } else {
            setTimeToPicker(endTime)
        }
    }

    private fun setTimeToPicker(time: Int) {
        binding.pickerHours.value = time.toHours()
        binding.pickerMinutes.value = time.toMinutes()
    }

    private fun showToastIfWrongFormat(state: DNDSettingsState) {
        if (state.event is DNDSettingsEvent.WrongFormat) {
            Toast.makeText(
                requireContext(),
                requireContext().getText(R.string.notification_manager_wrong_format),
                Toast.LENGTH_SHORT
            ).show()
            viewModel.obtainEvent(DNDSettingsEvent.Default)
        }
    }

    private fun convertToTime(hours: Int, minutes: Int): Int = hours * 60 + minutes

}

fun Group.setAlphaAndAvailability(alpha: Float) = referencedIds.forEach {
    val view = rootView.findViewById<View>(it)
    view.alpha = alpha
    val available = alpha > 0.7
    view.isClickable = available
    view.isFocusable = available
    view.isEnabled = available
}