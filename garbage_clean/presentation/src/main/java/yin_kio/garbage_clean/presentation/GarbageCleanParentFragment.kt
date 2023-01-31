package yin_kio.garbage_clean.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.NavHostFragment
import jamycake.lifecycle_aware.lifecycleAware
import yin_kio.garbage_clean.presentation.view_model.ObservableScreenViewModel
import yin_kio.garbage_clean.presentation.view_model.ScreenViewModelFactory

class GarbageCleanParentFragment : Fragment(R.layout.fragment_parent) {

    private val viewModel: ObservableScreenViewModel by lifecycleAware { screenViewModel() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val navController = (childFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment).navController

    }


    private fun ViewModel.screenViewModel() = ScreenViewModelFactory().create(
        applicationContext = requireActivity().applicationContext,
        androidViewModel = this
    )
}