package com.entertainment.event.ssearch.presentation.ui.device_info

import android.opengl.GLSurfaceView
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.entertainment.event.ssearch.presentation.R
import com.entertainment.event.ssearch.presentation.databinding.FragmentDeviceInfoBinding
import com.entertainment.event.ssearch.presentation.ui.adapter.FunctionRecycleAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class DeviceInfoFragment : Fragment(R.layout.fragment_device_info) {

    private val binding: FragmentDeviceInfoBinding by viewBinding()
    private val viewModel: DeviceInfoViewModel by viewModels()

    @Inject
    lateinit var glSurfaceView: GLSurfaceView

    private val adapter: FunctionRecycleAdapter =
        FunctionRecycleAdapter { parentItem ->
                viewModel.expandOrCollapseList(parentItem)
            }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initObservable()
        binding.glContainer.addView(glSurfaceView)
    }

    private fun initObservable() {
        lifecycleScope.launchWhenResumed {
            viewModel.screenState.collect { state ->
                adapter.submitList(state.showedDeviceInfo)
            }
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

    override fun onResume() {
        super.onResume()
        viewModel.startObserve()
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopObserve()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.glContainer.removeView(glSurfaceView)
    }
}