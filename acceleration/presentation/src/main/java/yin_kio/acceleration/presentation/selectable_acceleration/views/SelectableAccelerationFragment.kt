package yin_kio.acceleration.presentation.selectable_acceleration.views

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import jamycake.lifecycle_aware.currentBackStackEntry
import yin_kio.acceleration.presentation.R
import yin_kio.acceleration.presentation.databinding.FragmentStopSelectedAppsBinding
import yin_kio.acceleration.presentation.selectable_acceleration.*
import yin_kio.acceleration.presentation.selectable_acceleration.adapter.SelectableAppsAdapter
import yin_kio.acceleration.presentation.selectable_acceleration.view_model.ScreenState
import yin_kio.acceleration.presentation.selectable_acceleration.view_model.SelectableAccelerationViewModel

class SelectableAccelerationFragment : Fragment(R.layout.fragment_stop_selected_apps) {

    private val binding: FragmentStopSelectedAppsBinding by viewBinding()
    private val navigator by lazy { createNavigator(
        completeDestination = requireArguments().getInt("completeId"),
        completeDialogTitle =  getString(R.string.stop_selected_apps_dialog_title),
        completeDialogDescription = getString(R.string.stop_selected_apps_dialog_description),
        onInterClosed = { viewModel.complete(it) }
    ) }
    private val viewModel: SelectableAccelerationViewModel by currentBackStackEntry { createViewModel() }
    private val adapter by lazy { SelectableAppsAdapter(
        onItemUpdate = { app, selectable -> viewModel.updateAppItem(app, selectable) },
        onItemClick = { app, selectable -> viewModel.switchSelectApp(app, selectable) }
    ) }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recycler.adapter = adapter
        setupListeners()
        setupObservers()
    }

    private fun setupListeners() {
        binding.apply {
            back.setOnClickListener { viewModel.close(navigator) }
            checkbox.setOnClickListener { viewModel.switchSelectAllApps() }
            checkboxText.setOnClickListener { viewModel.switchSelectAllApps() }
            stop.setOnClickListener { viewModel.stopSelectedApps(navigator) }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.flow.collect {
                showRecyclerContent(it)
                binding.checkbox.isChecked = it.isAllSelected
                binding.stop.setBackgroundResource(it.buttonBgRes)
                showRecyclerOrProgress(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.commandsFlow.collect{
                adapter.notifyDataSetChanged()
            }
        }
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

}