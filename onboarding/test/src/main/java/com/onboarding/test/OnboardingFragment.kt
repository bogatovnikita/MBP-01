package com.onboarding.test

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class OnboardingFragment : Fragment(R.layout.fragmant_onboarding) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findNavController().navigate(R.id.action_onboardingFragment_to_onboarding_graph)
    }
}