package com.entertainment.event.ssearch.presentation.ui.device_info

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.entertainment.event.ssearch.presentation.R
import com.entertainment.event.ssearch.presentation.databinding.FragmentDeviceInfoBinding

class DeviceInfoFragment : Fragment(R.layout.fragment_device_info) {

    private val binding: FragmentDeviceInfoBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding
    }
}