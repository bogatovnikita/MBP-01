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
import androidx.recyclerview.widget.LinearLayoutManager
import com.hedgehog.presentation.R
import com.hedgehog.presentation.base.BaseFragment
import com.hedgehog.presentation.databinding.FragmentFirstScreenTimeBinding
import com.hedgehog.presentation.models.AppScreenTime
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
    private lateinit var activityManager: ActivityManager

    override fun onStart() {
        super.onStart()
        checkPermission()
    }

    private fun checkPermission() {
        if (checkPackageUsageStatePermission()) {
            updateScreenTime(Calendar.DATE, viewModel.beginTime, viewModel.endTime)
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
                    showToast(item.name.toInt())
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
            if (state.listIsEmpty) {
                stateListIsEmpty()
            } else {
                stateListIsNotEmpty()
            }
        } else {
            binding.loader.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.GONE
        }
    }

    private fun stateListIsNotEmpty() {
        binding.selectedMode.isEnabled = true
        binding.selectedMode.isClickable = true

        binding.reverseStatistics.isEnabled = true
        binding.reverseStatistics.isClickable = true

        binding.groupNotStatistics.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
    }

    private fun stateListIsEmpty() {
        initDate()
        binding.reverseStatistics.isClickable = false
        binding.reverseStatistics.isEnabled = false

        binding.selectedMode.isClickable = false
        binding.selectedMode.isEnabled = false

        binding.groupNotStatistics.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
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
            choiceLeftArrow()
        }
        binding.backgroundArrowRight.setOnClickListener {
            if (viewModel.beginTime == 0 && viewModel.endTime == -1) return@setOnClickListener
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

    private fun clickDeleteAppButton() {
        val checkList = viewModel.screenState.value.listDataScreenTime.filter { it.isChecked }
        if (checkList.isNotEmpty()) {
            viewModel.screenState.value.listDataScreenTime.filter {
                !it.isItSystemApp && it.isChecked
            }.forEach { appScreenTime ->
                val intent = Intent(Intent.ACTION_DELETE)
                intent.data = Uri.parse("package:" + appScreenTime.packageName)
                startActivityForResult(intent, 10)
            }
        } else {
            showToast(R.string.select_the_app_to_delete)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 10) {
            if (viewModel.screenState.value.choiceDay) {
                updateScreenTime(Calendar.DATE, viewModel.beginTime, viewModel.endTime)
            } else {
                updateScreenTime(Calendar.WEEK_OF_YEAR, viewModel.beginTime, viewModel.endTime)
            }
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

    private fun clickCheckbox() {
        if (viewModel.screenState.value.totalCheckedCount == viewModel.screenState.value.listDataScreenTime.size - viewModel.screenState.value.systemCheckedCount) {
            viewModel.cleanToggleCheckBox()
        } else {
            viewModel.selectAll()
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
        if (viewModel.screenState.value.choiceDay && viewModel.beginTime == LIMIT_STATICS_FOR_DAY) return
        if (viewModel.screenState.value.choiceWeek && viewModel.beginTime == LIMIT_STATICS_FOR_WEEK) return
        viewModel.beginTime += 1
        viewModel.endTime += 1
        if (viewModel.screenState.value.choiceDay) {
            updateScreenTime(Calendar.DATE, viewModel.beginTime, viewModel.endTime)
            viewModel.calendar.add(Calendar.DATE, -1)
        } else {
            viewModel.calendar.add(Calendar.WEEK_OF_YEAR, -1)
            viewModel.secondCalendar.add(Calendar.WEEK_OF_YEAR, -1)
            updateScreenTime(Calendar.WEEK_OF_YEAR, viewModel.beginTime, viewModel.endTime)
        }
    }

    private fun choiceRightArrow() {
        viewModel.beginTime -= 1
        viewModel.endTime -= 1
        if (viewModel.screenState.value.choiceDay) {
            viewModel.calendar.add(Calendar.DATE, +1)
            updateScreenTime(Calendar.DATE, viewModel.beginTime, viewModel.endTime)
        } else {
            viewModel.calendar.add(Calendar.WEEK_OF_YEAR, +1)
            viewModel.secondCalendar.add(Calendar.WEEK_OF_YEAR, +1)
            updateScreenTime(Calendar.WEEK_OF_YEAR, viewModel.beginTime, viewModel.endTime)
        }
    }

    private fun choiceWeek() {
        viewModel.choiceWeek()
        binding.weekButton.setBackgroundResource(R.drawable.background_not_transparent_button)
        binding.weekButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))

        binding.dayButton.setBackgroundResource(R.drawable.background_transparent_button)
        binding.dayButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))

        viewModel.beginTime = 0
        viewModel.endTime = -1
        viewModel.calendar = Calendar.getInstance()
        viewModel.secondCalendar = Calendar.getInstance()
        viewModel.calendar.set(Calendar.DAY_OF_WEEK, viewModel.calendar.firstDayOfWeek)
        viewModel.secondCalendar.set(Calendar.DAY_OF_WEEK, viewModel.secondCalendar.firstDayOfWeek)
        viewModel.secondCalendar.set(Calendar.HOUR_OF_DAY, -1)
        viewModel.calendar.add(Calendar.WEEK_OF_YEAR, 0)
        viewModel.secondCalendar.add(Calendar.WEEK_OF_YEAR, +1)
        updateScreenTime(Calendar.WEEK_OF_YEAR, viewModel.beginTime, viewModel.endTime)
        if (viewModel.screenState.value.selectionMode) {
            selectedMode(binding.selectedMode)
        }
    }

    private fun choiceDay() {
        viewModel.choiceDay()
        binding.dayButton.setBackgroundResource(R.drawable.background_not_transparent_button)
        binding.dayButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        binding.weekButton.setBackgroundResource(R.drawable.background_transparent_button)
        binding.weekButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
        viewModel.beginTime = 0
        viewModel.endTime = -1
        viewModel.calendar = Calendar.getInstance()
        viewModel.secondCalendar = Calendar.getInstance()
        updateScreenTime(Calendar.DATE, viewModel.beginTime, viewModel.endTime)
        if (viewModel.screenState.value.selectionMode) {
            selectedMode(binding.selectedMode)
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
        const val LIMIT_STATICS_FOR_DAY = 30
        const val LIMIT_STATICS_FOR_WEEK = 3
    }
}