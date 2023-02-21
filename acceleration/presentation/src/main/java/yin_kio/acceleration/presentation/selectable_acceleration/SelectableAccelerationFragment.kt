package yin_kio.acceleration.presentation.selectable_acceleration

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
import kotlinx.coroutines.CoroutineScope
import yin_kio.acceleration.data.AndroidApps
import yin_kio.acceleration.data.OlejaAds
import yin_kio.acceleration.domain.AccelerationDomainFactory
import yin_kio.acceleration.presentation.R
import yin_kio.acceleration.presentation.databinding.FragmentStopSelectedAppsBinding
import yin_kio.acceleration.presentation.inter.OlejaInter
import yin_kio.acceleration.presentation.selectable_acceleration.adapter.AppItemsAdapter

class SelectableAccelerationFragment : Fragment(R.layout.fragment_stop_selected_apps) {

    private val binding: FragmentStopSelectedAppsBinding by viewBinding()
    private val inter: OlejaInter by lifecycleAware { OlejaInter{ navigator.complete() } }
    private val navigator by lifecycleAware { createNavigator() }
    private val viewModel by lifecycleAware { createViewModel(viewModelScope) }
    private val adapter by lazy { AppItemsAdapter(
            coroutineScope = viewLifecycleOwner.lifecycleScope,
            appsFormState = viewModel
        )
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recycler.adapter = adapter

        binding.checkbox.setOnClickListener { viewModel.switchSelectAllApps() }
        binding.checkboxText.setOnClickListener{ viewModel.switchSelectAllApps() }

        setupObserver()
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.flow.collect {
                binding.recycler.updateAll()
                showRecyclerContent(it)
                showIsAllSelected(it)
                showButtonBg(it)
                showRecyclerOrProgress(it)
            }
        }
    }

    private fun showButtonBg(it: ScreenState) {
        binding.stop.setBackgroundResource(it.buttonBgRes)
    }

    private fun showIsAllSelected(it: ScreenState) {
        binding.checkbox.isChecked = it.isAllSelected
    }

    private fun showRecyclerContent(it: ScreenState) {
        if (it.isListVisible) {
            adapter.submitList(it.apps)
        }
    }

    private fun showRecyclerOrProgress(it: ScreenState) {
        binding.progressPlate.isVisible = it.isProgressVisible
        binding.recycler.isVisible = it.isListVisible
    }


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