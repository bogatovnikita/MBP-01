package com.hedgehog.presentation.ui.first_screen

import android.os.Bundle
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getListTimeScreenData(CalendarScreenTime(Calendar.DAY_OF_YEAR, 1))
        initRecyclerView()
        initObserver()
    }

    private fun initRecyclerView() {
        adapter = ScreenTimeAdapter(object : ScreenTimeAdapter.Listener {
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
        adapter.submitList(state.listDataScreenTime)
    }
}