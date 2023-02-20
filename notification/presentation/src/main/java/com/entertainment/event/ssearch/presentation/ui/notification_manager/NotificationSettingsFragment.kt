package com.entertainment.event.ssearch.presentation.ui.notification_manager

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.entertainment.event.ssearch.presentation.R
import com.entertainment.event.ssearch.presentation.databinding.FragmentNotificationSettingsBinding
import com.entertainment.event.ssearch.presentation.extensions.toTime
import com.entertainment.event.ssearch.presentation.models.NotificationSettingsState
import com.entertainment.event.ssearch.presentation.models.NotificationStateEvent
import com.entertainment.event.ssearch.presentation.ui.adapters.AppRecyclerViewAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NotificationSettingsFragment : Fragment(R.layout.fragment_notification_settings) {

    private val binding: FragmentNotificationSettingsBinding by viewBinding()
    private val viewModel: NotificationSettingsViewModel by activityViewModels()

    private val startActivityForNotificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if(viewModel.screenState.value.event == NotificationStateEvent.OpenNotificationPermissionDialog) {
                navigate(NotificationStateEvent.OpenDialogClearing)
            } else {
                viewModel.obtainEvent(NotificationStateEvent.Default)
            }
        }

    private val adapter: AppRecyclerViewAdapter = AppRecyclerViewAdapter(
        object : AppRecyclerViewAdapter.OnItemAppClickListener {
            override fun switchModeDisturb(packageName: String, isSwitched: Boolean) {
                viewModel.obtainEvent(
                    NotificationStateEvent.SwitchAppModeDisturb(
                        packageName,
                        isSwitched
                    )
                )
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
                navigate(state.event)
                showAds(state.event)
                requestPermForNotification(state.event)
            }
        }
    }

    private fun renderState(state: NotificationSettingsState) {
        with(state) {
            adapter.submitList(apps)
            binding.switchModeDisturb.isChecked = modeDND
            binding.switchLimitAllApplication.isChecked = isAllAppsLimited
            renderTimetableState(state)
        }
    }

    private fun renderTimetableState(state: NotificationSettingsState) {
        with(state) {
            binding.tvAddTimetable.isVisible = !isNeedShowTimetableInfo
            binding.groupTimetable.isVisible = isNeedShowTimetableInfo
            binding.tvTimeDisturb.text = "${timeStart.toTime()}-${timeEnd.toTime()}"
            binding.tvModeDisturbOn.isVisible = isNeedShowTimetableInfo && isAutoModeEnable
            binding.tvModeDisturbOff.isVisible = isNeedShowTimetableInfo && !isAutoModeEnable
            binding.tvDayOfWeek.text = if (selectedDays.isNotEmpty()) {
                val days =
                    selectedDays.map { "${getString(it)}, " }.reduce { acc, day -> acc + day }
                        .removeSuffix(", ")
                getString(R.string.notification_manager_every_week, days)
            } else {
                getString(R.string.notification_manager_only_today)
            }
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

    private fun navigate(event: NotificationStateEvent) {
        val navId = when (event) {
            is NotificationStateEvent.OpenServicePermissionDialog -> R.id.action_to_dialogNotificationPermissionFragment
            is NotificationStateEvent.OpenDialogClearing -> R.id.action_to_dialogClearingFragment
            is NotificationStateEvent.OpenDialogCompleteClean -> R.id.action_to_dialogCompleteClearFragment
            is NotificationStateEvent.OpenMissedNotification -> R.id.action_to_missedNotificationsFragment
            is NotificationStateEvent.OpenTimeTable -> R.id.action_to_DNDSettingsFragment
            else -> NOT_NAVIGATE
        }
        if (navId == NOT_NAVIGATE || findNavController().currentDestination?.id == navId) return
        findNavController().navigate(navId)
        viewModel.obtainEvent(NotificationStateEvent.Default)
    }

    private fun requestPermForNotification(state: NotificationStateEvent) {
        if (state is NotificationStateEvent.OpenNotificationPermissionDialog || state is NotificationStateEvent.LimitApps)  {
            if (Build.VERSION.SDK_INT >= 33) {
                startActivityForNotificationPermission.launch(arrayOf(Manifest.permission.POST_NOTIFICATIONS))
            } else {
                if (state is NotificationStateEvent.LimitApps) return
                viewModel.obtainEvent(NotificationStateEvent.OpenDialogClearing)
            }
        }
    }

    private fun showAds(event: NotificationStateEvent) {
        if (event == NotificationStateEvent.CloseDialogClearing) {
            viewModel.obtainEvent(NotificationStateEvent.OpenDialogCompleteClean)
        }
    }

    private fun initListeners() {
        binding.btnClearNotifications.setOnClickListener {
            viewModel.obtainEvent(NotificationStateEvent.ClearAllNotification)
        }
        binding.btnMissedNotifications.setOnClickListener {
            viewModel.obtainEvent(NotificationStateEvent.OpenMissedNotification)
        }
        binding.btnOpenTimetable.setOnClickListener {
            viewModel.obtainEvent(NotificationStateEvent.OpenTimeTable)
        }
        binding.switchModeDisturb.setOnClickListener {
            viewModel.obtainEvent(NotificationStateEvent.SwitchGeneralDisturbMode(binding.switchModeDisturb.isChecked))
        }
        binding.switchLimitAllApplication.setOnClickListener {
            viewModel.obtainEvent(NotificationStateEvent.LimitApps(binding.switchLimitAllApplication.isChecked))
        }
        binding.btnGoBack.setOnClickListener { findNavController().popBackStack() }
    }

    companion object {
        const val NOT_NAVIGATE = 11111
    }

}