package com.entertainment.event.ssearch.presentation.ui.notification_manager

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.entertainment.event.ssearch.presentation.R
import com.entertainment.event.ssearch.presentation.databinding.FragmentNotificationSettingsBinding
import com.entertainment.event.ssearch.presentation.ui.adapters.AppRecyclerViewAdapter
import com.entertainment.event.ssearch.presentation.models.NotificationSettingsState
import com.entertainment.event.ssearch.presentation.models.NotificationStateEvent
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NotificationSettingsFragment : Fragment(R.layout.fragment_notification_settings) {

    private val binding: FragmentNotificationSettingsBinding by viewBinding()
    private val viewModel: NotificationSettingsViewModel by viewModels()

    private val adapter: AppRecyclerViewAdapter = AppRecyclerViewAdapter(
        object : AppRecyclerViewAdapter.OnItemAppClickListener {
            override fun switchModeDisturb(packageName: String, isSwitched: Boolean) {
                viewModel.obtainEvent(NotificationStateEvent.SwitchAppModeDisturb(packageName, isSwitched))
            }
        }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initStateObserver()
        initListeners()
    }

    override fun onStart() {
        super.onStart()
        viewModel.obtainEvent(NotificationStateEvent.Update)
    }

    private fun initStateObserver() {
        lifecycle.coroutineScope.launchWhenResumed {
            viewModel.screenState.collect { state ->
                renderState(state)
                if (state.event is NotificationStateEvent.OpenPermissionDialog) {
                    openPermissionDialog()
                    viewModel.obtainEvent(NotificationStateEvent.Default)
                }
            }
        }
    }

    private fun renderState(state: NotificationSettingsState) {
        with(state) {
            adapter.submitList(apps)
            binding.switchModeDisturb.isChecked = modeNotDisturb
            binding.switchLimitAllApplication.isChecked = isAllAppsLimited
        }
    }

    private fun initAdapter() {
        binding.recyclerViewNotification.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewNotification.adapter = adapter
        val itemAnimator = binding.recyclerViewNotification.itemAnimator
        if (itemAnimator is DefaultItemAnimator) {
            itemAnimator.supportsChangeAnimations = false
        }
    }

    private fun openPermissionDialog() {
            findNavController().navigate(R.id.action_to_dialogNotificationPermissionFragment)
    }

    private fun initListeners() {
        binding.btnClearNotifications.setOnClickListener {
            viewModel.obtainEvent(NotificationStateEvent.ClearAllNotification)
        }
//        binding.btnOpenTimetable.setOnClickListener {
//            if (hasPermissionService()) {
//
//            } else {
//                openPermissionDialog()
//            }
//        }
        binding.switchModeDisturb.setOnClickListener {
            viewModel.obtainEvent(NotificationStateEvent.SwitchGeneralDisturbMode(binding.switchModeDisturb.isChecked))
        }
        binding.switchLimitAllApplication.setOnClickListener {
                viewModel.obtainEvent(NotificationStateEvent.LimitApps(binding.switchLimitAllApplication.isChecked))
        }
    }

}