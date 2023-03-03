package com.onboarding.presentation.ui.splash

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.onboarding.presentation.base.BaseFragment
import com.onboarding.presentation.databinding.FragmentSplashBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

            }
        })
        lifecycleScope.launch {
//            delay(2000)
            findNavController().navigate(
                SplashFragmentDirections.toOptimizationFragment(
                    FROM_SPLASH_TO_FIRST_OPTIMIZATION
                )
            )
        }
    }

    companion object {
        const val FROM_SPLASH_TO_FIRST_OPTIMIZATION = 0
    }
}