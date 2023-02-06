package yin_kio.main_menu.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import yin_kio.main_menu.presentation.databinding.FragmentAdvicesBinding

class AdvicesFragment : Fragment(R.layout.fragment_advices) {

    private val binding: FragmentAdvicesBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.advices.setOnClickListener{ findNavController().navigateUp() }
    }

}