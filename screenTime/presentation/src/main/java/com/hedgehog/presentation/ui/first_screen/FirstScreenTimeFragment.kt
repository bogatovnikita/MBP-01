package com.hedgehog.presentation.ui.first_screen

import android.Manifest
import android.app.ActivityManager
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Process
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hedgehog.presentation.R
import com.hedgehog.presentation.base.BaseFragment
import com.hedgehog.presentation.databinding.FragmentFirstScreenTimeBinding
import com.hedgehog.presentation.models.AppScreenTime
import com.hedgehog.presentation.models.CalendarScreenTime
import com.hedgehog.presentation.models.Period
import com.hedgehog.presentation.models.SwitchButtonState
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
    private lateinit var activityManager: ActivityManager

    override fun onStart() {
        super.onStart()
        checkPermission()
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.screenState.value.selectionMode) {
            if (viewModel.screenState.value.choiceDay) {
                updateScreenTime(Calendar.DATE, viewModel.beginTime, viewModel.endTime)
            } else {
                updateScreenTime(Calendar.WEEK_OF_YEAR, viewModel.beginTime, viewModel.endTime)
            }
        }
    }

    private fun checkPermission() {
        if (checkPackageUsageStatePermission()) {
            if (viewModel.screenState.value.choiceDay) {
                updateScreenTime(Calendar.DATE, viewModel.beginTime, viewModel.endTime)
                showAppsByPeriod(Period.Day)
            } else {
                updateScreenTime(Calendar.WEEK_OF_YEAR, viewModel.beginTime, viewModel.endTime)
                showAppsByPeriod(Period.Week)
            }
        } else {
            startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initObserver()
        initClickListeners()
    }

    private fun initRecyclerView() {
        getResultFromAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    private fun getResultFromAdapter() {
        adapter = ScreenTimeAdapter(
            object : ScreenTimeAdapter.Listener {
                override fun onChooseNote(item: AppScreenTime) {
                    if (viewModel.screenState.value.selectionMode) return
                    val typeDateFormat = if (viewModel.screenState.value.choiceDay) {
                        Calendar.DATE
                    } else {
                        Calendar.WEEK_OF_YEAR
                    }

                    findNavController().navigate(
                        FirstScreenTimeFragmentDirections.actionFirstScreenTimeFragmentToSecondScreenTimeFragment(
                            item.packageName,
                            CalendarScreenTime(
                                typeDateFormat,
                                viewModel.beginTime,
                                viewModel.endTime
                            )
                        )
                    )
                }

                override fun onToggle(item: AppScreenTime) {
                    if (!item.isItSystemApp) {
                        viewModel.toggleCheckBox(item)
                    } else {
                        showToast(R.string.delete_system_app)
                    }
                    binding.checkbox.isChecked =
                        (viewModel.screenState.value.listDataScreenTime.size - viewModel.screenState.value.systemCheckedCount) == viewModel.screenState.value.totalCheckedCount
                }
            }, viewModel.screenState.value.selectionMode
        )
    }

    private fun initObserver() {
        lifecycleScope.launchWhenResumed {
            viewModel.screenState.collect { state ->
                withContext(Dispatchers.Main) { renderState(state) }
            }
        }
    }

    private fun renderState(state: FirstScreenTimeState) {
        initDate()
        if (state.isLoading) {
            binding.loader.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
            adapter.submitList(state.listDataScreenTime)
            binding.reverseStatistics.isSelected =
                viewModel.screenState.value.reverseListAppScreenTime
            if (state.listDataScreenTime.isEmpty()) {
                stateListIsEmpty()
            } else {
                stateListIsNotEmpty()
            }
        } else {
            binding.loader.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.GONE
            binding.groupNotStatistics.visibility = View.GONE
        }

        if (state.isLoading && state.selectionMode) {
            binding.groupCheckbox.visibility = View.VISIBLE
        } else {
            binding.groupCheckbox.visibility = View.GONE
        }
    }

    private fun stateListIsEmpty() {
        binding.reverseStatistics.isClickable = false
        binding.reverseStatistics.isEnabled = false

        binding.selectedMode.isClickable = false
        binding.selectedMode.isEnabled = false

        binding.groupNotStatistics.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
    }

    private fun stateListIsNotEmpty() {
        binding.selectedMode.isEnabled = true
        binding.selectedMode.isClickable = true

        binding.reverseStatistics.isEnabled = true
        binding.reverseStatistics.isClickable = true

        binding.groupNotStatistics.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
    }

    private fun initClickListeners() {
        binding.checkbox.setOnClickListener {
            clickCheckbox()
        }
        binding.stopApp.setOnClickListener {
            clickStopAllButton()
        }
        binding.deleteApp.setOnClickListener {
            clickDeleteAppButton()
        }
        binding.selectedMode.setOnClickListener {
            selectedMode(it)
        }
        binding.reverseStatistics.setOnClickListener {
            viewModel.reverseList()
            it.isSelected = viewModel.screenState.value.reverseListAppScreenTime
        }
        binding.backgroundArrowLeft.setOnClickListener {
            if (!viewModel.screenState.value.isLoading) return@setOnClickListener
            choiceLeftArrow()
        }
        binding.backgroundArrowRight.setOnClickListener {
            if (viewModel.beginTime == 0 && viewModel.endTime == -1) return@setOnClickListener
            choiceRightArrow()
        }
        binding.dayButton.setOnClickListener {
            if (viewModel.screenState.value.choiceDay) return@setOnClickListener
            if (!viewModel.screenState.value.isLoading) return@setOnClickListener
            choiceDay()
        }
        binding.weekButton.setOnClickListener {
            if (viewModel.screenState.value.choiceWeek) return@setOnClickListener
            if (!viewModel.screenState.value.isLoading) return@setOnClickListener
            choiceWeek()
        }
    }

    private fun clickCheckbox() {
        if (viewModel.screenState.value.totalCheckedCount == viewModel.screenState.value.listDataScreenTime.size - viewModel.screenState.value.systemCheckedCount) {
            viewModel.cleanToggleCheckBox()
        } else {
            viewModel.selectAll()
        }
    }

    private fun clickStopAllButton() {
        val checkList = viewModel.screenState.value.listDataScreenTime.filter { it.isChecked }
        if (checkList.isNotEmpty()) {
            activityManager =
                requireContext().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            viewModel.screenState.value.listDataScreenTime.forEach {
                if (it.isChecked) {
                    activityManager.killBackgroundProcesses(it.packageName)
                }
            }
            binding.selectedMode.performClick()
            showToast(R.string.kill_background_process_text)
        } else {
            showToast(R.string.select_the_app_to_stop)
        }
    }

    private fun clickDeleteAppButton() {
        val checkList = viewModel.screenState.value.listDataScreenTime.filter { it.isChecked }
        if (checkList.isNotEmpty()) {
            viewModel.screenState.value.listDataScreenTime.filter {
                !it.isItSystemApp && it.isChecked
            }.forEach { appScreenTime ->
                val intent = Intent(Intent.ACTION_DELETE)
                intent.data = Uri.parse("package:" + appScreenTime.packageName)
                startActivity(intent)
            }
            viewModel.cleanToggleCheckBox()
        } else {
            showToast(R.string.select_the_app_to_delete)
        }
    }

    private fun selectedMode(it: View) {
        viewModel.cleanToggleCheckBox()
        viewModel.selectedMode()
        it.isSelected = viewModel.screenState.value.selectionMode
        binding.groupCheckbox.isVisible = viewModel.screenState.value.selectionMode
        binding.groupChoiceDate.isVisible = !viewModel.screenState.value.selectionMode
        binding.checkbox.isChecked = false
        initRecyclerView()
    }

    private fun choiceLeftArrow() {
        if (viewModel.screenState.value.choiceDay && viewModel.beginTime == LIMIT_STATISTICS_FOR_DAY) return
        viewModel.beginTime += 1
        viewModel.endTime += 1
        updateScreenTime(Calendar.DATE, viewModel.beginTime, viewModel.endTime)
        viewModel.calendar.add(Calendar.DATE, -1)
    }

    private fun choiceRightArrow() {
        viewModel.beginTime -= 1
        viewModel.endTime -= 1
        viewModel.calendar.add(Calendar.DATE, +1)
        updateScreenTime(Calendar.DATE, viewModel.beginTime, viewModel.endTime)
    }

    private fun choiceDay() {
        viewModel.choiceDay()

        binding.arrowsGroups.visibility = View.VISIBLE

        showAppsByPeriod(Period.Day)

        viewModel.beginTime = 0
        viewModel.endTime = -1
        viewModel.calendar = Calendar.getInstance()
        viewModel.secondCalendar = Calendar.getInstance()
        updateScreenTime(Calendar.DATE, viewModel.beginTime, viewModel.endTime)
        if (viewModel.screenState.value.selectionMode) {
            selectedMode(binding.selectedMode)
        }
    }

    private fun choiceWeek() {
        viewModel.choiceWeek()

        binding.arrowsGroups.visibility = View.GONE

        showAppsByPeriod(Period.Week)

        viewModel.beginTime = 0
        viewModel.endTime = -1
        viewModel.calendar = Calendar.getInstance()
        viewModel.secondCalendar = Calendar.getInstance()

        viewModel.calendar.add(Calendar.DATE, -LIMIT_STATISTICS_FOR_DAY)

        updateScreenTime(Calendar.WEEK_OF_YEAR, viewModel.beginTime, viewModel.endTime)
        if (viewModel.screenState.value.selectionMode) {
            selectedMode(binding.selectedMode)
        }
    }

    private fun showAppsByPeriod(period: Period) {
        val weekState = presentSwitchButton(period == Period.Week)
        val dayState = presentSwitchButton(period == Period.Day)

        binding.apply {
            val periodControl = mapOf(weekButton to weekState, dayButton to dayState)
            periodControl.entries.forEach {
                it.key.setBackgroundResource(it.value.bgRes)
                it.key.setTextColor(it.value.textColor)
            }
        }
    }

    private fun presentSwitchButton(isActive: Boolean): SwitchButtonState {
        return if (isActive) {
            SwitchButtonState(
                textColor = ContextCompat.getColor(requireContext(), R.color.white),
                bgRes = R.drawable.background_not_transparent_button
            )
        } else {
            SwitchButtonState(
                textColor = ContextCompat.getColor(requireContext(), R.color.green),
                bgRes = R.drawable.background_transparent_button
            )
        }
    }

    private fun updateScreenTime(dataType: Int, beginTime: Int, endTime: Int) {
        viewModel.getListTimeScreenData(CalendarScreenTime(dataType, beginTime, endTime))
    }

    private fun initDate() {
        if (viewModel.screenState.value.choiceDay) {
            val formatter = SimpleDateFormat("EEEE, dd.MM.yyyy")
            binding.dateTv.text = formatter.format(viewModel.calendar.time)
        } else {
            val formatter = SimpleDateFormat("EE, dd.MM")
            binding.dateTv.text =
                "${formatter.format(viewModel.calendar.time)} - ${formatter.format(viewModel.secondCalendar.time)}"
        }
    }

    private fun checkPackageUsageStatePermission(): Boolean {
        val appOps = requireContext()
            .getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            Process.myUid(), requireContext().packageName
        )
        return if (mode == AppOpsManager.MODE_DEFAULT) {
            requireContext().checkCallingOrSelfPermission(Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED
        } else {
            mode == AppOpsManager.MODE_ALLOWED
        }
    }

    private fun showToast(text: Int) {
        Toast.makeText(
            requireContext(),
            text,
            Toast.LENGTH_SHORT
        ).show()
    }

    companion object {
        const val LIMIT_STATISTICS_FOR_DAY = 7
    }
}