package com.hedgehog.presentation.ui.second_screen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.hedgehog.presentation.base.BaseFragment
import com.hedgehog.presentation.databinding.FragmentSecondScreenTimeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SecondScreenTimeFragment :
    BaseFragment<FragmentSecondScreenTimeBinding>(FragmentSecondScreenTimeBinding::inflate) {

    private val viewModel: SecondScreenTimeViewModel by viewModels()
    private val args by navArgs<SecondScreenTimeFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAppInfo(args.packageName, args.calendarScreenTime)
    }

}