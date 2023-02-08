package yin_kio.main_menu.presentation

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import yin_kio.main_menu.presentation.databinding.FragmentMainMenuBinding

class MainMenuFragment : Fragment(R.layout.fragment_main_menu) {

    private val binding: FragmentMainMenuBinding by viewBinding()


    private val onBackPressedCallback = onBackPressedCallback()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        parentFragmentManager.fragmentFactory = MainMenuFragmentFactory()
        setupListeners()
        onBackPressedCallback.isEnabled = false

        requireActivity().onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    private fun setupListeners() {
        binding.apply {
            fileManager.setOnClickListener { openFileManager() }
            duplicates.setOnClickListener { openDuplicates() }
            garbageClean.setOnClickListener { openGarbageClean() }
        }
    }

    private fun openFileManager() {
        onBackPressedCallback.isEnabled = true
        findNavController().navigate(R.id.toFileManager)
    }

    private fun openDuplicates(){
        onBackPressedCallback.isEnabled = true
        findNavController().navigate(R.id.toDuplicates, completeDestination())
    }

    private fun openGarbageClean() {
        onBackPressedCallback.isEnabled = true
        findNavController().navigate(R.id.toGarbageClean, completeDestination())
    }

    private fun completeDestination() : Bundle {
        return Bundle().apply {
            putInt(ArgNames.COMPLETE_DESTINATION_ID, R.id.toAdvices)
        }
    }


    private fun onBackPressedCallback() = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
            findNavController().navigateUp()
        }
    }

}