package yin_kio.main_menu.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import yin_kio.main_menu.presentation.databinding.FragmentMainMenuBinding

class MainMenuFragment : Fragment(R.layout.fragment_main_menu) {

    private val binding: FragmentMainMenuBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        parentFragmentManager.fragmentFactory = MainMenuFragmentFactory()

        binding.apply {
            fileManager.setOnClickListener{ openFileManager() }
            duplicates.setOnClickListener{ openDuplicates() }
            garbageClean.setOnClickListener{ openGarbageClean() }
        }
    }

    private fun openFileManager() {
        findNavController().navigate(R.id.action_mainMenuFragment_to_fileManagerParentFragment)
    }

    private fun openDuplicates(){
        findNavController().navigate(R.id.action_mainMenuFragment_to_duplicatesParentFragment,
            Bundle().apply {
                putInt("completeId", R.id.action_duplicatesParentFragment_to_advicesFragment)
            }
        )
    }

    private fun openGarbageClean() {
        findNavController().navigate(R.id.action_mainMenuFragment_to_garbageCleanParentFragment,
            Bundle().apply {
                putInt("completeId", R.id.action_garbageCleanParentFragment_to_advicesFragment)
            }
        )
    }

}