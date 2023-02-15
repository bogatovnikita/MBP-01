package com.hedgehog.presentation.ui.second_screen

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.hedgehog.presentation.R
import com.hedgehog.presentation.base.BaseFragment
import com.hedgehog.presentation.databinding.FragmentSecondScreenTimeBinding
import com.hedgehog.presentation.models.CalendarScreenTime
import com.hedgehog.presentation.models.Period
import com.hedgehog.presentation.models.SwitchButtonState
import com.hedgehog.presentation.ui.first_screen.FirstScreenTimeFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class SecondScreenTimeFragment :
    BaseFragment<FragmentSecondScreenTimeBinding>(FragmentSecondScreenTimeBinding::inflate) {

    private val viewModel: SecondScreenTimeViewModel by viewModels()
    private val args by navArgs<SecondScreenTimeFragmentArgs>()
    private lateinit var sharedPrefs: SharedPreferences
    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val result = requireContext().packageManager.getInstalledApplications(0)
                .find { info -> info.packageName == args.packageName } == null
            if (result) findNavController().navigateUp()
        }

    override fun onStart() {
        super.onStart()
        val result = requireContext().packageManager.getInstalledApplications(0)
            .find { info -> info.packageName == args.packageName } == null
        if (result) findNavController().navigateUp()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAppInfo(args.packageName, args.calendarScreenTime)
        initSharedPrefs()
        checkArgForRenderScreen()
        initObserver()
        initClickListener()
    }

    private fun initSharedPrefs() {
        sharedPrefs = requireContext().getSharedPreferences(FIRST_LAUNCH, 0)
    }

    private fun checkArgForRenderScreen() {
        viewModel.beginTime = args.calendarScreenTime.beginTime
        viewModel.endTime = args.calendarScreenTime.endTime
        if (args.calendarScreenTime.dataType == Calendar.DATE) {
            viewModel.choiceDay()
            showAppsByPeriod(Period.Day)

            viewModel.calendar = Calendar.getInstance()
            viewModel.secondCalendar = Calendar.getInstance()
            viewModel.calendar.add(Calendar.DATE, -args.calendarScreenTime.beginTime)

        } else {
            viewModel.choiceWeek()
            showAppsByPeriod(Period.Week)

            binding.groupArrows.visibility = View.GONE

            viewModel.calendar = Calendar.getInstance()
            viewModel.secondCalendar = Calendar.getInstance()

            viewModel.calendar.add(Calendar.DATE, -FirstScreenTimeFragment.LIMIT_STATISTICS_FOR_DAY)
        }
    }

    private fun initObserver() {
        lifecycleScope.launchWhenResumed {
            viewModel.screenState.collect { state ->
                withContext(Dispatchers.Main) { renderState(state) }
            }
        }
    }

    private fun initClickListener() {
        binding.iconIv.setOnClickListener {
            if (sharedPrefs.getBoolean(FIRST_LAUNCH, true)) {
                showTutorial()
            } else {
                binding.tutorialGroup.visibility = View.GONE
                try {
                    val intentLaunch =
                        requireContext().packageManager.getLaunchIntentForPackage(args.packageName)
                    startActivity(intentLaunch)
                } catch (e: Exception) {
                    Toast.makeText(
                        requireActivity(),
                        R.string.this_is_a_system_application_you_will_not_be_able_to_switch_to_it,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        binding.infoAppButton.setOnClickListener {
            if (sharedPrefs.getBoolean(FIRST_LAUNCH, true)) {
                showTutorial()
            } else {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", args.packageName, null)
                intent.data = uri
                startActivity(intent)
            }
        }
        binding.backgroundBackArrow.setOnClickListener {
            if (sharedPrefs.getBoolean(FIRST_LAUNCH, true)) {
                showTutorial()
            } else {
                findNavController().navigateUp()
            }
        }
        binding.dayButton.setOnClickListener {
            if (sharedPrefs.getBoolean(FIRST_LAUNCH, true)) {
                showTutorial()
            } else {
                if (viewModel.screenState.value.choiceDay) return@setOnClickListener
                choiceDay()
            }
        }
        binding.weekButton.setOnClickListener {
            if (sharedPrefs.getBoolean(FIRST_LAUNCH, true)) {
                showTutorial()
            } else {
                if (viewModel.screenState.value.choiceWeek) return@setOnClickListener
                choiceWeek()
            }
        }
        binding.backgroundArrowLeft.setOnClickListener {
            if (sharedPrefs.getBoolean(FIRST_LAUNCH, true)) {
                showTutorial()
            } else {
                choiceLeftArrow()
            }
        }
        binding.backgroundArrowRight.setOnClickListener {
            if (sharedPrefs.getBoolean(FIRST_LAUNCH, true)) {
                showTutorial()
            } else {
                if (viewModel.beginTime == 0 && viewModel.endTime == -1) return@setOnClickListener
                choiceRightArrow()
            }
        }
        binding.stopApp.setOnClickListener {
            if (sharedPrefs.getBoolean(FIRST_LAUNCH, true)) {
                showTutorial()
            } else {
                if (viewModel.screenState.value.appInfo.isSystemApp) {
                    Toast.makeText(requireContext(), R.string.stop_system_app, Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                } else {
                    clickStopAllButton()
                }
            }
        }
        binding.deleteApp.setOnClickListener {
            if (sharedPrefs.getBoolean(FIRST_LAUNCH, true)) {
                showTutorial()
            } else {
                if (viewModel.screenState.value.appInfo.isSystemApp) {
                    Toast.makeText(requireContext(), R.string.delete_system_app, Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                } else {
                    clickDeleteAppButton()
                }
            }
        }

        binding.tutorialTransparentBackground.setOnClickListener {
            binding.tutorialGroup.visibility = View.GONE
        }
    }

    private fun showTutorial() {
        lifecycleScope.launch {
            delay(1000)
            binding.tutorialGroup.visibility = View.VISIBLE
            sharedPrefs.edit().putBoolean(FIRST_LAUNCH, false).apply()
        }
    }

    private fun clickStopAllButton() {
        val activityManager: ActivityManager =
            requireContext().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.killBackgroundProcesses(args.packageName)
        findNavController().navigateUp()
        Toast.makeText(requireActivity(), R.string.kill_background_process_text, Toast.LENGTH_SHORT)
            .show()
    }

    private fun clickDeleteAppButton() {
        val intent = Intent(Intent.ACTION_DELETE)
        intent.data = Uri.parse("package:" + args.packageName)
        resultLauncher.launch(intent)
    }

    private fun choiceLeftArrow() {
        if (viewModel.screenState.value.choiceDay && viewModel.beginTime == FirstScreenTimeFragment.LIMIT_STATISTICS_FOR_DAY) return
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

    private fun choiceDay() {
        viewModel.choiceDay()

        binding.groupArrows.visibility = View.VISIBLE

        showAppsByPeriod(Period.Day)

        viewModel.beginTime = 0
        viewModel.endTime = -1
        viewModel.calendar = Calendar.getInstance()
        viewModel.secondCalendar = Calendar.getInstance()
        updateScreenTime(Calendar.DATE, viewModel.beginTime, viewModel.endTime)
    }

    private fun choiceWeek() {
        viewModel.choiceWeek()

        binding.groupArrows.visibility = View.GONE

        showAppsByPeriod(Period.Week)

        viewModel.beginTime = 0
        viewModel.endTime = -1
        viewModel.calendar = Calendar.getInstance()
        viewModel.secondCalendar = Calendar.getInstance()

        viewModel.calendar.add(Calendar.DATE, -FirstScreenTimeFragment.LIMIT_STATISTICS_FOR_DAY)

        updateScreenTime(Calendar.WEEK_OF_YEAR, viewModel.beginTime, viewModel.endTime)
    }

    private fun renderState(state: SecondScreenTimeState) {
        initDate()
        if (state.isLoading) {
            binding.groupLoadingData.isVisible = state.isLoading
            binding.loader.isVisible = !state.isLoading
//            binding.customViewGraph.progressesHeights = state.appInfo.listTime
            initScreen(state)
            if (state.appInfo.listTime.isEmpty()) {
                stateListIsEmpty()
            } else {
                stateListIsNotEmpty()
            }
        }
    }

    private fun stateListIsNotEmpty() {
        binding.groupNotStatistics.visibility = View.GONE
        binding.groupLoadingData.visibility = View.VISIBLE
    }

    private fun stateListIsEmpty() {
        initDate()
        binding.groupNotStatistics.visibility = View.VISIBLE
        binding.groupLoadingData.visibility = View.GONE
        initIconAndName()
    }

    private fun initIconAndName() {
        val icon = try {
            requireContext().packageManager.getApplicationIcon(args.packageName)
        } catch (e: Exception) {
            null
        }
        val name = try {
            requireContext().packageManager.getApplicationInfo(args.packageName, 0)
                .loadLabel(requireContext().packageManager)
        } catch (e: Exception) {
            args.packageName
        }
        binding.iconIv.setImageDrawable(icon)
        binding.nameAppTv.text = name
    }

    private fun updateScreenTime(dataType: Int, beginTime: Int, endTime: Int) {
        viewModel.getAppInfo(args.packageName, CalendarScreenTime(dataType, beginTime, endTime))
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

    private fun initScreen(state: SecondScreenTimeState) {
        with(state.appInfo) {
            binding.nameAppTv.text = this.nameApp
            binding.iconIv.setImageDrawable(this.icon)
            binding.timeLastLaunchTv.text = this.lastLaunch
            binding.timeTotalUsageTimeTv.text = this.totalTimeUsage
            binding.timeDataTv.text = this.data
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

    companion object {
        private const val FIRST_LAUNCH = "FIRST_LAUNCH"
    }
}