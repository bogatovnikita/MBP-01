package com.battery_saving.presentation.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import androidx.work.*
import com.battery_saving.presentation.base.BaseFragment
import com.battery_saving.presentation.room.BatteryChargeDatabase
import com.battery_saving.presentation.room.entities.BatteryChargeStatisticsEntity
import com.battery_saving.presentation.worker.BatteryWorker
import com.hedgehog.battery_saving.presentation.R
import com.hedgehog.battery_saving.presentation.databinding.FragmentBatterySavingBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class BatterySavingFragment :
    BaseFragment<FragmentBatterySavingBinding>(FragmentBatterySavingBinding::inflate) {

    private val viewModel: BatterySavingViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
    }

    private fun initObserver() {
        lifecycleScope.launchWhenResumed {
            viewModel.state.collect { state ->
                withContext(Dispatchers.Main) { renderState(state) }
            }
        }
    }

    private fun renderState(state: BatterySavingState) {
        binding.percentChargeLevel.text =
            resources.getString(R.string.D_percent, state.batteryChargePercent)
    }

    private fun getWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .build()

        val myWorkRequest = PeriodicWorkRequest.Builder(
            BatteryWorker::class.java, 16,
            TimeUnit.MINUTES
        ).setConstraints(constraints)
            .addTag("my_id")
            .build()
        WorkManager.getInstance(requireContext())
            .enqueueUniquePeriodicWork("my_id", ExistingPeriodicWorkPolicy.KEEP, myWorkRequest)
    }

    private fun batteryConsumption() {
        val database = Room.databaseBuilder(
            requireContext().applicationContext,
            BatteryChargeDatabase::class.java,
            "database"
        ).build()
        var sizeDatabaseList: List<BatteryChargeStatisticsEntity> = emptyList()
        lifecycleScope.launch(Dispatchers.IO) {
            sizeDatabaseList = database.getBatteryChargeStatisticsDao().getDatabase()
        }
//        when (sizeDatabaseList.size) {
//
//        }
//        if (sizeDatabaseList.size < 0) {
//            binding.consumptionForHourPercent.text = resources.getString(R.string.calculate)
//        }
    }

}