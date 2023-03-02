package com.onboarding.presentation.ui.optimization

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.ads.preloadAd
import com.example.ads.showInter
import com.onboarding.presentation.R
import com.onboarding.presentation.base.BaseFragment
import com.onboarding.presentation.databinding.FragmentOptimizationBinding
import com.onboarding.presentation.utils.DescriptionOptimization
import com.onboarding.presentation.utils.NavigatorOnboarding
import com.onboarding.presentation.utils.PreferencesProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class OptimizationFragment :
    BaseFragment<FragmentOptimizationBinding>(FragmentOptimizationBinding::inflate) {

    private val args by navArgs<OptimizationFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.preloadAd()
        initDescription()
        startScan()
    }

    private fun initDescription() {
        binding.description.text =
            getString(DescriptionOptimization.getDescriptionForOptimization(args.typeNextStep))
    }

    private fun startScan() {
        lifecycleScope.launch {
            val animator = ObjectAnimator.ofInt(binding.progress, "progress", 0, 100)
            animator.interpolator = DecelerateInterpolator()
            animator.duration = 8000
            animator.start()
            delay(8000)
            this@OptimizationFragment.showInter { navigateTo() }
        }
    }

    private fun navigateTo() {
        when {
            PreferencesProvider(requireContext()).checkFirstLaunch() -> {
                findNavController().navigate(NavigatorOnboarding.getNavigationId(args.typeNextStep))
            }

        }
    }

}