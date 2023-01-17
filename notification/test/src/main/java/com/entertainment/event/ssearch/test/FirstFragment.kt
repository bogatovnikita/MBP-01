package com.entertainment.event.ssearch.test

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.entertainment.event.ssearch.test.databinding.FragmentFirstBinding

class FirstFragment : Fragment(R.layout.fragment_first) {

    val binding: FragmentFirstBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding
        findNavController().navigate(R.id.action_firstFragment_to_notification_graph)
    }
}