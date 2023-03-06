package com.onboarding.presentation.ui.first_steps

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.onboarding.presentation.R
import com.onboarding.presentation.base.BaseFragment
import com.onboarding.presentation.databinding.FragmentRateBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RateFragment : BaseFragment<FragmentRateBinding>(FragmentRateBinding::inflate) {
    private var isCheck = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickListener()
    }

    private fun initClickListener() {
        binding.ratingBar.setOnRatingBarChangeListener { _, _, _ ->
            isCheck = true
            binding.resumeBtn.isClickable = true
            binding.resumeBtn.setBackgroundResource(R.drawable.background_not_transparent_button_blue)
        }

        binding.resumeBtn.setOnClickListener {
            if (!isCheck) return@setOnClickListener

            visitStore()
            navigateToNext()
        }
    }

    private fun visitStore() {
        val uri: Uri = Uri.parse("market://details?id=${requireActivity().packageName}")
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        goToMarket.addFlags(
            Intent.FLAG_ACTIVITY_NO_HISTORY or
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        )
        try {
            startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=${requireActivity().packageName}")
                )
            )
        }
    }

    private fun navigateToNext() {
        lifecycleScope.launch {
            delay(500)
            findNavController().navigate(R.id.action_rateFragment_to_initialAssessmentFragment)
        }
    }
}