package yin_kio.acceleration.presentation.selectable_acceleration

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import jamycake.lifecycle_aware.lifecycleAware
import kotlinx.coroutines.CoroutineScope
import yin_kio.acceleration.data.AndroidApps
import yin_kio.acceleration.data.OlejaAds
import yin_kio.acceleration.domain.AccelerationDomainFactory
import yin_kio.acceleration.presentation.R
import yin_kio.acceleration.presentation.databinding.FragmentStopSelectedAppsBinding
import yin_kio.acceleration.presentation.inter.OlejaInter

class SelectableAccelerationFragment : Fragment(R.layout.fragment_stop_selected_apps) {

    private val binding: FragmentStopSelectedAppsBinding by viewBinding()
    private val inter: OlejaInter by lifecycleAware { OlejaInter{ navigator.complete() } }
    private val navigator by lifecycleAware { createNavigator() }
    private val viewModel by lifecycleAware { createViewModel(viewModelScope) }











    override fun onResume() {
        super.onResume()
        inter.activity = requireActivity()
        navigator.navController = findNavController()
    }

    override fun onPause() {
        super.onPause()
        inter.activity = null
        navigator.navController = null
    }











    private fun ViewModel.createNavigator() = SelectableAccelerationNavigatorImpl(
        coroutineScope = viewModelScope,
        inter = inter,
        completeDestination = completeDestination(),
        completeArgs = completeArgs()
    )


    private fun completeArgs() : Bundle {
        return Bundle().apply {
            putString("dialog_title", getString(R.string.stop_selected_apps_dialog_title))
            putString("dialog_description", getString(R.string.stop_selected_apps_dialog_description))
        }
    }

    private fun completeDestination() : Int{
        return requireArguments().getInt("completeId")
    }


    private fun createViewModel(coroutineScope: CoroutineScope) : SelectableAccelerationViewModel{
        val context = requireActivity().applicationContext


        val outer = SelectableAccelerationOuterImpl(
            navigator = navigator,
            presenter = SelectableAccelerationPresenter()
        )
        val useCases = AccelerationDomainFactory.createSelectableAccelerationUseCases(
            outer = outer,
            apps = AndroidApps(context),
            coroutineScope = coroutineScope,
            ads = OlejaAds(context),
        )


        val viewModel = SelectableAccelerationViewModel(
            useCases = useCases
        )

        outer.viewModel = viewModel

        return viewModel
    }


}