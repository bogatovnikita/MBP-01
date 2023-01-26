package com.hedgehog.screentimetest

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class ScreenTimeTestFragment : Fragment(R.layout.fragment_screen_time_test) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findNavController().navigate(R.id.action_screenTimeTestFragment_to_screen_time_graph)
    }
}