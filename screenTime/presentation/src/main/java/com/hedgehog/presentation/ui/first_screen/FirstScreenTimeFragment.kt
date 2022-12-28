package com.hedgehog.presentation.ui.first_screen

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.hedgehog.presentation.base.BaseFragment
import com.hedgehog.presentation.databinding.FragmentFirstScreenTimeBinding
import com.hedgehog.presentation.models.AppScreenTime
import com.hedgehog.presentation.models.CalendarScreenTime
import com.hedgehog.presentation.ui.adapters.ScreenTimeAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

@AndroidEntryPoint
class FirstScreenTimeFragment :
    BaseFragment<FragmentFirstScreenTimeBinding>(FragmentFirstScreenTimeBinding::inflate) {

    private val viewModel: FirstScreenTimeViewModel by viewModels()
    private lateinit var adapter: ScreenTimeAdapter
    private var beginTime = 0
    private var endTime = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateScreenTime(Calendar.DATE, beginTime, endTime)
        startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
        initRecyclerView()
        initObserver()
        initClickListeners()
    }

    private fun initRecyclerView() {
        adapter = ScreenTimeAdapter(
            object : ScreenTimeAdapter.Listener {
                override fun onChooseNote(item: AppScreenTime) {
                    Toast.makeText(requireContext(), item.name, Toast.LENGTH_SHORT).show()
                }
            })
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    private fun initObserver() {
        lifecycleScope.launchWhenResumed {
            viewModel.screenState.collect { state ->
                withContext(Dispatchers.Main) { renderState(state) }
            }
        }
    }

    private fun renderState(state: FirstScreenTimeState) {
        if (!state.isLoading) return
        adapter.submitList(state.listDataScreenTime)

    }

    private fun initClickListeners() {
        binding.backgroundArrowLeft.setOnClickListener {
            beginTime += 1
            endTime += 1
            updateScreenTime(Calendar.DATE, beginTime, endTime)
        }

        binding.backgroundArrowRight.setOnClickListener {
            if (beginTime == 0 && endTime == -1) return@setOnClickListener
            beginTime -= 1
            endTime -= 1
            updateScreenTime(Calendar.DATE, beginTime, endTime)
        }

        binding.dayButton.setOnClickListener {
            if (viewModel.screenState.value.choiceDay) return@setOnClickListener
            viewModel.choiceDay()
            beginTime = 0
            endTime = -1
            updateScreenTime(Calendar.DATE, beginTime, endTime)
        }

        binding.weekButton.setOnClickListener {
            if (viewModel.screenState.value.choiceWeek) return@setOnClickListener
            viewModel.choiceWeek()
        }
    }

    private fun updateScreenTime(dataType: Int, beginTime: Int, endTime: Int) {
        viewModel.getListTimeScreenData(CalendarScreenTime(dataType, beginTime, endTime))
    }
}
