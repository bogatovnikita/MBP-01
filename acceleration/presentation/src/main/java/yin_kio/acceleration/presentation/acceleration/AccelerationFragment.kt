package yin_kio.acceleration.presentation.acceleration

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
import yin_kio.acceleration.presentation.inter.OlejaInter
import yin_kio.acceleration.presentation.R
import yin_kio.acceleration.presentation.databinding.FragmentAccelerationBinding

class AccelerationFragment : Fragment(R.layout.fragment_acceleration) {

    private val binding: FragmentAccelerationBinding by viewBinding()

    private val navigator: AccelerationNavigatorImpl by lifecycleAware { AccelerationNavigatorImpl(
        coroutineScope = viewModelScope,
    ) }
    private val useCases by lifecycleAware {
        val context = requireActivity().applicationContext
        AccelerationDomainFactory.createAccelerationUseCases(
            outer = AccelerationOuterImpl(
                navigator = navigator,
                viewModel = TODO(),
                presenter = TODO(),
                permissionRequester = TODO()
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
        navigator.inter = OlejaInter(
            activity = requireActivity(),
            onClose = { useCases.close() }
        )

    }

    override fun onPause() {
        super.onPause()
        // Ручное занулиение полей необходимо, так как активити и навконтроллер могут существовать
        // меьше, чем вьюмодель.
        navigator.navController = null
        navigator.inter = null
    }



}