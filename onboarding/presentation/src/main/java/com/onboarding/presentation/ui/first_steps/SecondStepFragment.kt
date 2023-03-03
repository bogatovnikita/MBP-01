package com.onboarding.presentation.ui.first_steps

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.onboarding.presentation.R
import com.onboarding.presentation.base.BaseFragment
import com.onboarding.presentation.databinding.FragmentSecondStepBinding

class SecondStepFragment :
    BaseFragment<FragmentSecondStepBinding>(FragmentSecondStepBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.resumeBtn.setOnClickListener { findNavController().navigate(R.id.action_secondStepFragment_to_rateFragment) }
    }
}