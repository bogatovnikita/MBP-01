package yin_kio.acceleration.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import jamycake.lifecycle_aware.lifecycleAware
import yin_kio.acceleration.presentation.databinding.DialogAcceleratePermissionBinding

class AccelerationPermissionDialog : DialogFragment(R.layout.dialog_accelerate_permission) {

    private val binding: DialogAcceleratePermissionBinding by viewBinding()

    // Здесь идёт прямая зависимость от use cases, так как не требуется сохранения состояния
    private val useCases by lifecycleAware {
        PermissionDialogUseCasesImpl()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.close.setOnClickListener { useCases.close() }
        binding.allow.setOnClickListener { useCases.givePermission() }
    }

    override fun onResume() {
        super.onResume()
        useCases.activity = requireActivity()
        useCases.navController = findNavController()
    }

    override fun onPause() {
        super.onPause()
        useCases.activity = null
        useCases.navController = null
    }

}