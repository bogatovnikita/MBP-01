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
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.entertainment.event.ssearch.data.background.NotificationService
import com.entertainment.event.ssearch.presentation.R
import com.entertainment.event.ssearch.presentation.databinding.FragmentNotificationSettingsBinding
import com.entertainment.event.ssearch.presentation.ui.adapters.AppRecyclerViewAdapter
import com.entertainment.event.ssearch.presentation.ui.models.NotificationSettingsState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationSettingsFragment : Fragment(R.layout.fragment_notification_settings) {

    private val binding: FragmentNotificationSettingsBinding by viewBinding()
    private val viewModel: NotificationSettingsViewModel by viewModels()

    lateinit var adapter: AppRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getAppWithNotifications(hasPermissionService())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initStateObserver()
//        startActivity(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"))
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
        }

    }

    private fun initAdapter() {
        val adapter = AppRecyclerViewAdapter(
            object : AppRecyclerViewAdapter.OnItemAppClickListener {
                override fun switchModeDisturb(packageName: String, isSwitched: Boolean) {
                    viewModel.switchModeDisturb(packageName, isSwitched)
                }
            }
        )
        binding.recyclerViewNotification.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewNotification.adapter = adapter
    }

    fun startNotificationService() {
        val intent = Intent(requireContext(), NotificationService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            requireContext().startForegroundService(intent)
        } else {
            requireContext().startService(intent)
        }
    }

    fun hasPermissionService() : Boolean {
        val string = Settings.Secure.getString(requireContext().contentResolver, "enabled_notification_listeners") ?: ""
        val listenersClassNames = string.split(":")
        val listenerName = ComponentName(requireContext(), NotificationService::class.java).flattenToString()
        return listenersClassNames.contains(listenerName)
    }
}