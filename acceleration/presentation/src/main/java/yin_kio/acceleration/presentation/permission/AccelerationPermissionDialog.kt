package yin_kio.acceleration.presentation.permission

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import jamycake.lifecycle_aware.lifecycleAware
import yin_kio.package_usage_dialog.databinding.DialogPackageUsageBinding

class AccelerationPermissionDialog : DialogFragment(yin_kio.package_usage_dialog.R.layout.dialog_package_usage) {

    private val binding: DialogPackageUsageBinding by viewBinding()

    // Здесь идёт прямая зависимость от use cases, так как не требуется сохранения состояния
    private val useCases by lifecycleAware {
        PermissionDialogUseCasesImpl(
            permissionRequester = PermissionRequesterImpl()
        )
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.close.setOnClickListener { useCases.close() }
        binding.allow.setOnClickListener { useCases.givePermission() }
    }

    override fun onResume() {
        super.onResume()
        useCases.navController = findNavController()
    }

    override fun onPause() {
        super.onPause()
        useCases.navController = null
    }

}