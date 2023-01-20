package com.entertainment.event.ssearch.presentation.ui.notification_manager

import android.content.ComponentName
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.entertainment.event.ssearch.data.background.NotificationService
import com.entertainment.event.ssearch.presentation.R
import com.entertainment.event.ssearch.presentation.databinding.FragmentNotificationSettingsBinding
import com.entertainment.event.ssearch.presentation.ui.adapters.AppRecyclerViewAdapter
import com.entertainment.event.ssearch.presentation.ui.models.NotificationSettingsState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationSettingsFragment : Fragment(R.layout.fragment_notification_settings),
    View.OnClickListener {

    private val binding: FragmentNotificationSettingsBinding by viewBinding()
    private val viewModel: NotificationSettingsViewModel by viewModels()

    private val adapter: AppRecyclerViewAdapter = AppRecyclerViewAdapter(
        object : AppRecyclerViewAdapter.OnItemAppClickListener {
            override fun switchModeDisturb(packageName: String, isSwitched: Boolean) {
                viewModel.switchAppModeDisturb(packageName, isSwitched)
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
        startNotificationService()
        viewModel.getAppWithNotifications(hasPermissionService())
    }

    private fun initStateObserver() {
        lifecycle.coroutineScope.launchWhenResumed {
            viewModel.screenState.collect { state ->
                renderState(state)
            }
        }
    }

    private fun renderState(state: NotificationSettingsState) {
        with(state) {
            adapter.submitList(apps)
            binding.switchModeDisturb.isChecked = modeNotDisturb
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

    fun startNotificationService() {
        if (!hasPermissionService()) return
        val intent = Intent(requireContext(), NotificationService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            requireContext().startForegroundService(intent)
        } else {
            requireContext().startService(intent)
        }
    }

    fun hasPermissionService(): Boolean {
        val string = Settings.Secure.getString(
            requireContext().contentResolver,
            "enabled_notification_listeners"
        ) ?: ""
        val listenersClassNames = string.split(":")
        val listenerName =
            ComponentName(requireContext(), NotificationService::class.java).flattenToString()
        return listenersClassNames.contains(listenerName)
    }

    private fun openPermissionDialog() {
        if (!hasPermissionService())
            findNavController().navigate(R.id.action_to_dialogNotificationPermissionFragment)
    }

    private fun initListeners() {
        binding.btnClearNotifications.setOnClickListener(this)
        binding.btnOpenTimetable.setOnClickListener(this)
        binding.switchModeDisturb.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_clear_notifications, R.id.btn_open_timetable -> openPermissionDialog()
            R.id.switch_mode_disturb-> viewModel.switchGeneralDisturbMode(binding.switchModeDisturb.isChecked)
        }
    }

}