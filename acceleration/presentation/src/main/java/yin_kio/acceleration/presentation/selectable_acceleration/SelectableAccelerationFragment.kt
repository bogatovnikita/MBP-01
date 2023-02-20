package yin_kio.acceleration.presentation.selectable_acceleration

import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import jamycake.lifecycle_aware.lifecycleAware
import yin_kio.acceleration.presentation.R
import yin_kio.acceleration.presentation.databinding.FragmentStopSelectedAppsBinding

class SelectableAccelerationFragment : Fragment(R.layout.fragment_stop_selected_apps) {

    private val binding: FragmentStopSelectedAppsBinding by viewBinding()
    private val viewModel by lifecycleAware { createViewModel() }


    private fun createViewModel() : SelectableAccelerationViewModel{
        return SelectableAccelerationViewModel()
    }


}