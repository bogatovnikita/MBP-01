package com.entertainment.event.ssearch.presentation.ui.dnd_settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.entertainment.event.ssearch.presentation.R
import com.entertainment.event.ssearch.presentation.databinding.FragmentDNDSettingsBinding

class DNDSettingsFragment : Fragment(R.layout.fragment_d_n_d_settings) {

    private val binding: FragmentDNDSettingsBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initListeners() {
        with(binding) {
            dayPicker.setOnChosenDaysListener {  }
            btnGoBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun initNumberPicker() {
        with(binding) {
        }
    }

}