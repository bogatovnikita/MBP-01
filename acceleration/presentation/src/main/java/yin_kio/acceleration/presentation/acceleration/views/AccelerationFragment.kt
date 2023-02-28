package yin_kio.acceleration.presentation.acceleration.views

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import jamycake.lifecycle_aware.lifecycleAware
import yin_kio.acceleration.domain.acceleration.ui_out.AppsState
import yin_kio.acceleration.presentation.permission.PermissionRequesterImpl
import yin_kio.acceleration.presentation.R
import yin_kio.acceleration.presentation.acceleration.AccelerationNavigatorImpl
import yin_kio.acceleration.presentation.acceleration.view_model.AccelerationViewModel
import yin_kio.acceleration.presentation.acceleration.view_model.AccelerationViewModelFactory
import yin_kio.acceleration.presentation.acceleration.view_model.ScreenState
import yin_kio.acceleration.presentation.acceleration.adapter.AppsAdapter
import yin_kio.acceleration.presentation.databinding.FragmentAccelerationBinding
import yin_kio.acceleration.presentation.inter.OlejaInter

class AccelerationFragment : Fragment(R.layout.fragment_acceleration) {

    private val binding: FragmentAccelerationBinding by viewBinding()

    private val navigator: AccelerationNavigatorImpl by lifecycleAware { createNavigator() }

    private val permissionRequester: PermissionRequesterImpl by lifecycleAware { PermissionRequesterImpl() }
    private val viewModel by lifecycleAware { createAccelerationViewModel() }
    private val adapter by lazy { AppsAdapter() }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recycler.adapter = adapter

        setupObserver()
        setupListeners()
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.flow.collect {
                showRamInfo(it)
                showAppsLoaderOrPermission(it)
                passAppsToRecycler(it.appsState)
            }
        }
    }

    private fun passAppsToRecycler(
        appsState: AppsState
    ) {
        if (appsState is AppsState.AppsList) {
            adapter.submitList(appsState.apps)
        }
    }

    private fun showAppsLoaderOrPermission(it: ScreenState) {
        binding.recycler.isVisible = it.appsState is AppsState.AppsList
        binding.listPermission.isVisible = it.appsState is AppsState.Permission
        binding.listLoader.isVisible = it.appsState is AppsState.Progress
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
            allow.setOnClickListener { viewModel.givePermission() }
        }
    }
















    override fun onResume() {
        super.onResume()
        navigator.navController = findNavController()
        navigator.inter = OlejaInter(
            activity = requireActivity(),
            onClose = { viewModel.complete() }
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
        val context = requireActivity().application

        return AccelerationViewModelFactory(
            context = context,
            navigator = navigator,
            permissionRequester = permissionRequester,
            coroutineScope = viewModelScope
        ).create()
    }



    private fun ViewModel.createNavigator() = AccelerationNavigatorImpl(
        coroutineScope = viewModelScope,
        completeId = requireArguments(),
        completeArgs = Bundle().apply {
            putString("dialog_title", getString(R.string.your_phone_is_fast))
            putString("dialog_description", getString(R.string.your_phone_is_fast_description))
        }
    )



}