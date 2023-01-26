package com.hedgehog.presentation.ui.second_screen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.hedgehog.presentation.base.BaseFragment
import com.hedgehog.presentation.databinding.FragmentSecondScreenTimeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat

@AndroidEntryPoint
class SecondScreenTimeFragment :
    BaseFragment<FragmentSecondScreenTimeBinding>(FragmentSecondScreenTimeBinding::inflate) {

    private val viewModel: SecondScreenTimeViewModel by viewModels()
    private val args by navArgs<SecondScreenTimeFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAppInfo(args.packageName, args.calendarScreenTime)
        initObserver()
        initClickListener()
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
            val intentLaunch =
                requireContext().packageManager.getLaunchIntentForPackage(args.packageName)
            startActivity(intentLaunch)
        }
        binding.infoAppButton.setOnClickListener {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", args.packageName, null)
            intent.data = uri
            startActivity(intent)
        }
    }

    private fun renderState(state: SecondScreenTimeState) {
        initDate()
        if (state.isLoading) {
            binding.groupLoadingData.isVisible = state.isLoading
            binding.loader.isVisible = !state.isLoading
            initScreen(state)
        }
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

}