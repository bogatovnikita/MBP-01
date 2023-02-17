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
import yin_kio.acceleration.presentation.PermissionRequesterImpl
import yin_kio.acceleration.presentation.inter.OlejaInter
import yin_kio.acceleration.presentation.R
import yin_kio.acceleration.presentation.databinding.FragmentAccelerationBinding

class AccelerationFragment : Fragment(R.layout.fragment_acceleration) {

    private val binding: FragmentAccelerationBinding by viewBinding()

    private val navigator: AccelerationNavigatorImpl by lifecycleAware { AccelerationNavigatorImpl(
        coroutineScope = viewModelScope,
    ) }
    private val permissionRequester: PermissionRequesterImpl by lifecycleAware { PermissionRequesterImpl() }
    private val viewModel by lifecycleAware {
        val context = requireActivity().applicationContext

        val presenter = AccelerationPresenterImpl(context)

        val outer = AccelerationOuterImpl(
            navigator = navigator,
            presenter = presenter,
            permissionRequester = permissionRequester
        )

        val useCases = AccelerationDomainFactory.createAccelerationUseCases(
            outer = outer,
            permissions = AccelerationPermissions(context),
            apps = AndroidApps(context),
            ramInfo = RamInfoProvider(context),
            coroutineScope = viewModelScope,
            ads = OlejaAds(context)
        )

        val vm = AccelerationViewModel(
            useCases = useCases
        )

        outer.viewModel = vm
        vm
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.back.setOnClickListener { viewModel.close() }
        binding.accelerate.setOnClickListener { viewModel.accelerate() }
        binding.stopSelected.setOnClickListener { viewModel.uploadBackgroundProcess() }
    }

    override fun onResume() {
        super.onResume()
        navigator.navController = findNavController()
        navigator.inter = OlejaInter(
            activity = requireActivity(),
            onClose = { viewModel.close() }
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