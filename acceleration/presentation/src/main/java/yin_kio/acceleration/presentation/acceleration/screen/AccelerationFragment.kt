package yin_kio.acceleration.presentation.acceleration.screen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import jamycake.lifecycle_aware.lifecycleAware
import kotlinx.coroutines.flow.collect
import yin_kio.acceleration.presentation.PermissionRequesterImpl
import yin_kio.acceleration.presentation.inter.OlejaInter
import yin_kio.acceleration.presentation.R
import yin_kio.acceleration.presentation.acceleration.AccelerationNavigatorImpl
import yin_kio.acceleration.presentation.databinding.FragmentAccelerationBinding

class AccelerationFragment : Fragment(R.layout.fragment_acceleration) {

    private val binding: FragmentAccelerationBinding by viewBinding()

    private val navigator: AccelerationNavigatorImpl by lifecycleAware { AccelerationNavigatorImpl(
        coroutineScope = viewModelScope,
    ) }
    private val permissionRequester: PermissionRequesterImpl by lifecycleAware { PermissionRequesterImpl() }
    private val viewModel by lifecycleAware { createAccelerationViewModel() }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupObserver()
        setupListeners()
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.flow.collect {
                showRamInfo(it)
            }
        }
    }

    private fun showRamInfo(it: ScreenState) {
        binding.ramInfo.binding.apply {
            progressBar.progress = it.ramInfo.progress
            available.text = it.ramInfo.available
            total.text = it.ramInfo.total
            occupied.text = it.ramInfo.occupied
        }
    }

    private fun setupListeners() {
        binding.apply {
            back.setOnClickListener { viewModel.close() }
            accelerate.setOnClickListener { viewModel.accelerate() }
            stopSelected.setOnClickListener { viewModel.uploadBackgroundProcess() }
        }
    }
















    override fun onResume() {
        super.onResume()
        navigator.navController = findNavController()
        navigator.inter = OlejaInter(
            activity = requireActivity(),
            onClose = { viewModel.close() }
        )
        permissionRequester.activity = requireActivity()

    }

    override fun onPause() {
        super.onPause()
        // Ручное занулиение полей необходимо, так как активити и навконтроллер могут существовать
        // меьше, чем вьюмодель.
        navigator.navController = null
        navigator.inter = null
        permissionRequester.activity = null
    }


    private fun ViewModel.createAccelerationViewModel(): AccelerationViewModel {
        val context = requireActivity().applicationContext

        return AccelerationViewModelFactory(
            context = context,
            navigator = navigator,
            permissionRequester = permissionRequester,
            coroutineScope = viewModelScope
        ).create()
    }


}