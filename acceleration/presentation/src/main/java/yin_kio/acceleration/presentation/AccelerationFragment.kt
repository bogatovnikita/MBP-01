package yin_kio.acceleration.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import jamycake.lifecycle_aware.lifecycleAware
import yin_kio.acceleration.data.AccelerationPermissions
import yin_kio.acceleration.data.AndroidApps
import yin_kio.acceleration.data.OlejaAds
import yin_kio.acceleration.data.RamInfoProvider
import yin_kio.acceleration.domain.AccelerationDomainFactory
import yin_kio.acceleration.presentation.databinding.FragmentAccelerationBinding

class AccelerationFragment : Fragment(R.layout.fragment_acceleration) {

    private val binding: FragmentAccelerationBinding by viewBinding()

    private val navigator by lifecycleAware { NavigatorImpl(viewModelScope) }
    private val useCases by lifecycleAware {
        val context = requireActivity().applicationContext
        AccelerationDomainFactory.createAccelerationUseCases(
            outer = AccelerationOuterImpl(
                navigator = navigator
            ),
            permissions = AccelerationPermissions(context),
            apps = AndroidApps(context),
            ramInfo = RamInfoProvider(context),
            coroutineScope = viewModelScope,
            ads = OlejaAds(context)
        )
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.back.setOnClickListener { useCases.close() }
        binding.accelerate.setOnClickListener { useCases.accelerate() }
        binding.stopSelected.setOnClickListener { useCases.uploadBackgroundProcess() }
    }

    override fun onResume() {
        super.onResume()
        navigator.navController = findNavController()
        navigator.activity = requireActivity()
    }

    override fun onPause() {
        super.onPause()
        navigator.activity = null
        navigator.navController = null
    }



}