package yin_kio.file_manager.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.permissions.requestStoragePermissions
import com.example.permissions.runtimePermissionsLauncher
import kotlinx.coroutines.launch
import yin_kio.file_manager.presentation.databinding.FragmentPermissionBinding

class PermissionFragment(
) : Fragment(R.layout.fragment_permission) {

    private val binding: FragmentPermissionBinding by viewBinding()
    private val viewModel by lazy { parentViewModel() }

    private val permissionsLauncher by runtimePermissionsLauncher {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewLifecycleOwner.lifecycleScope.launch{
            viewModel.flow.collect{
                if (it.hasPermission){
                    findNavController().navigateUp()
                }
            }
        }

        binding.givePermission.setOnClickListener {
            requestStoragePermissions(permissionsLauncher)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.obtainIntention(Intention.UpdateFiles)
    }



}