package com.entertainment.event.ssearch.presentation.ui.missed_notifications

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.entertainment.event.ssearch.presentation.R
import com.entertainment.event.ssearch.presentation.databinding.FragmentMissedNotificationsBinding
import com.entertainment.event.ssearch.presentation.models.MissedNotificationEvent
import com.entertainment.event.ssearch.presentation.models.MissedNotificationState
import com.entertainment.event.ssearch.presentation.ui.adapters.NotificationRecyclerViewAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MissedNotificationsFragment : Fragment(R.layout.fragment_missed_notifications) {

    private val binding: FragmentMissedNotificationsBinding by viewBinding()
    private val viewModel: MissedNotificationViewModel by viewModels()

    private val adapter: NotificationRecyclerViewAdapter =
        NotificationRecyclerViewAdapter(
            onClick = { notification ->
                viewModel.obtainEvent(MissedNotificationEvent.OpenAppByPackageName(notification))
            },
            onSwipe = { notification ->
                viewModel.obtainEvent(MissedNotificationEvent.DeleteNotification(notification))
            }
        )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initObserverState()
        initListeners()
    }

    private fun initObserverState() {
        lifecycleScope.launch {
            viewModel.screenState.collect { state ->
                renderState(state)
            }
        }
    }

    private fun renderState(state: MissedNotificationState) {
        with(state) {
            adapter.submitList(notifications)
            binding.groupEmptyNotifications.isVisible = notificationIsEmpty
            binding.groupNotificationsNotEmpty.isVisible = !notificationIsEmpty
        }
    }

    private fun initListeners() {
        with(binding) {
            btnGoBack.setOnClickListener { findNavController().popBackStack() }
            btnSettings.setOnClickListener { findNavController().popBackStack() }
            binding.btnCleanAll.setCanDeleteListener { isCanDelete ->
                viewModel.obtainEvent(MissedNotificationEvent.DeleteAll(isCanDelete))
            }
            binding.root.setOnClickListener {
                binding.btnCleanAll.hideButton() // TODO есть стандартное расширение isVisible
            }
            binding.btnMissedNotifications.setOnClickListener {
                binding.btnCleanAll.hideButton()
            }
            binding.recyclerView.setOnClickListener {
                binding.btnCleanAll.hideButton()
            }
            binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    binding.btnCleanAll.hideButton() // TODO Высокая связность между вьюшками: этот слушатель зависит от кнопки, как и другие.
                    // TODO в качестве посредника лучше использовать вьюмодель, это снизит связность
                }
            })
        }
    }

    private fun initAdapter() {
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = adapter
        val itemAnimator = binding.recyclerView.itemAnimator
        if (itemAnimator is DefaultItemAnimator) {
            itemAnimator.supportsChangeAnimations = false
        }
    }

}