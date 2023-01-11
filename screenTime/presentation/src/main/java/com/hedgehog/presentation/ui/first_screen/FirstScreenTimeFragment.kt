package com.hedgehog.presentation.ui.first_screen

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.hedgehog.presentation.R
import com.hedgehog.presentation.base.BaseFragment
import com.hedgehog.presentation.databinding.FragmentFirstScreenTimeBinding
import com.hedgehog.presentation.models.AppScreenTimeListItems
import com.hedgehog.presentation.models.CalendarScreenTime
import com.hedgehog.presentation.ui.adapters.ScreenTimeAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class FirstScreenTimeFragment :
    BaseFragment<FragmentFirstScreenTimeBinding>(FragmentFirstScreenTimeBinding::inflate) {

    private val viewModel: FirstScreenTimeViewModel by viewModels()
    private lateinit var adapter: ScreenTimeAdapter
    private var beginTime = 0
    private var endTime = -1
    private var calendar = Calendar.getInstance()
    private var secondCalendar = Calendar.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateScreenTime(Calendar.DATE, beginTime, endTime)
//        startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
        initRecyclerView()
        initObserver()
        initClickListeners()
    }

    private fun initRecyclerView() {
        adapter = ScreenTimeAdapter(
            object : ScreenTimeAdapter.Listener {
                override fun onChooseNote(item: AppScreenTimeListItems) {
                    Toast.makeText(requireContext(), item.name, Toast.LENGTH_SHORT).show()
                }

                override fun onToggle(item: AppScreenTimeListItems) {
                    viewModel.toggleSelection(item)
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
        adapter.submitList(state.appScreenTimeListItems)
        initDate()
        binding.reverseStatistics.isSelected = viewModel.screenState.value.reverseListAppScreenTime
    }

    private fun initClickListeners() {
        binding.selectedMode.setOnClickListener {
            if (viewModel.screenState.value.selectionMode) return@setOnClickListener
            viewModel.selectedMode()
            it.isSelected = viewModel.screenState.value.selectionMode
        }
        binding.reverseStatistics.setOnClickListener {
            viewModel.reverseList()
            it.isSelected = viewModel.screenState.value.reverseListAppScreenTime
        }
        binding.backgroundArrowLeft.setOnClickListener {
            choiceLeftArrow()
        }

        binding.backgroundArrowRight.setOnClickListener {
            if (beginTime == 0 && endTime == -1) return@setOnClickListener
            choiceRightArrow()
        }

        binding.dayButton.setOnClickListener {
            if (viewModel.screenState.value.choiceDay) return@setOnClickListener
            choiceDay()
        }

        binding.weekButton.setOnClickListener {
            if (viewModel.screenState.value.choiceWeek) return@setOnClickListener
            choiceWeek()
        }
    }

    private fun choiceLeftArrow() {
        beginTime += 1
        endTime += 1
        if (viewModel.screenState.value.choiceDay) {
            updateScreenTime(Calendar.DATE, beginTime, endTime)
            calendar.add(Calendar.DATE, -1)
        } else {
            calendar.add(Calendar.WEEK_OF_YEAR, -1)
            secondCalendar.add(Calendar.WEEK_OF_YEAR, -1)
            updateScreenTime(Calendar.WEEK_OF_YEAR, beginTime, endTime)
        }
    }

    private fun choiceRightArrow() {
        beginTime -= 1
        endTime -= 1
        if (viewModel.screenState.value.choiceDay) {
            calendar.add(Calendar.DATE, +1)
            updateScreenTime(Calendar.DATE, beginTime, endTime)
        } else {
            calendar.add(Calendar.WEEK_OF_YEAR, +1)
            secondCalendar.add(Calendar.WEEK_OF_YEAR, +1)
            updateScreenTime(Calendar.WEEK_OF_YEAR, beginTime, endTime)
        }
    }

    private fun choiceWeek() {
        viewModel.choiceWeek()
        binding.weekButton.setBackgroundResource(R.drawable.background_not_transparent_button)
        binding.weekButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        binding.dayButton.setBackgroundResource(R.drawable.background_transparent_button)
        binding.dayButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
        beginTime = 0
        endTime = -1
        calendar = Calendar.getInstance()
        secondCalendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
        secondCalendar.set(Calendar.DAY_OF_WEEK, secondCalendar.firstDayOfWeek)
        secondCalendar.set(Calendar.HOUR_OF_DAY, -1)
        calendar.add(Calendar.WEEK_OF_YEAR, 0)
        secondCalendar.add(Calendar.WEEK_OF_YEAR, +1)
        updateScreenTime(Calendar.WEEK_OF_YEAR, beginTime, endTime)
    }

    private fun choiceDay() {
        viewModel.choiceDay()
        binding.dayButton.setBackgroundResource(R.drawable.background_not_transparent_button)
        binding.dayButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        binding.weekButton.setBackgroundResource(R.drawable.background_transparent_button)
        binding.weekButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
        beginTime = 0
        endTime = -1
        calendar = Calendar.getInstance()
        secondCalendar = Calendar.getInstance()
        updateScreenTime(Calendar.DATE, beginTime, endTime)
    }

    private fun updateScreenTime(dataType: Int, beginTime: Int, endTime: Int) {
        viewModel.getListTimeScreenData(CalendarScreenTime(dataType, beginTime, endTime))
    }

    private fun initDate() {
        if (viewModel.screenState.value.choiceDay) {
            val formatter = SimpleDateFormat("EEEE, dd.MM.yyyy")
            binding.dateTv.text = formatter.format(calendar.time)
        } else {
            val formatter = SimpleDateFormat("EE, dd.MM")
            binding.dateTv.text =
                "${formatter.format(calendar.time)} - ${formatter.format(secondCalendar.time)}"
        }
    }
}
