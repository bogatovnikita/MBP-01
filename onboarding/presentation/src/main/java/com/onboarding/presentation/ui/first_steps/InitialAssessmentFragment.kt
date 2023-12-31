package com.onboarding.presentation.ui.first_steps

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.onboarding.presentation.base.BaseFragment
import com.onboarding.presentation.databinding.FragmentInitialAssessmentBinding

class InitialAssessmentFragment :
    BaseFragment<FragmentInitialAssessmentBinding>(FragmentInitialAssessmentBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.resumeBtn.setOnClickListener {
            findNavController().navigate(
                InitialAssessmentFragmentDirections.toOptimizationFragment(
                    FROM_INITIAL_ASSESSMENT_TO_ABOUT_DEVICE
                )
            )
        }
    }

    companion object {
        const val FROM_INITIAL_ASSESSMENT_TO_ABOUT_DEVICE = 1
    }
}