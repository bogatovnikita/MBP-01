package com.onboarding.presentation.ui.first_steps

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.onboarding.presentation.base.BaseFragment
import com.onboarding.presentation.databinding.FragmentRequiredOptimizationBinding

class RequiredOptimizationFragment :
    BaseFragment<FragmentRequiredOptimizationBinding>(FragmentRequiredOptimizationBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.resumeBtn.setOnClickListener {
            findNavController().navigate(
                InitialAssessmentFragmentDirections.toOptimizationFragment(
                    FROM_REQUIRED_OPTIMIZATION_TO_FULL_READY
                )
            )
        }
    }

    companion object {
        const val FROM_REQUIRED_OPTIMIZATION_TO_FULL_READY = 2
    }
}