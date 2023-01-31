package yin_kio.garbage_clean.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import jamycake.lifecycle_aware.lifecycleAware
import yin_kio.garbage_clean.presentation.databinding.FragmentGarbageCleanBinding
import yin_kio.garbage_clean.presentation.view_model.ObservableScreenViewModel
import yin_kio.garbage_clean.presentation.view_model.ScreenViewModelFactory

class GarbageCleanFragment : Fragment(R.layout.fragment_garbage_clean) {

    private val binding: FragmentGarbageCleanBinding by viewBinding()
    private val viewModel: ObservableScreenViewModel by lifecycleAware { screenViewModel() }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.state.collect{

            }
        }
    }






    private fun ViewModel.screenViewModel() = ScreenViewModelFactory().create(
        applicationContext = requireActivity().applicationContext,
        androidViewModel = this
    )

}