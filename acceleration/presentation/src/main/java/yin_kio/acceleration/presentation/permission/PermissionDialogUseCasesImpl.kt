package yin_kio.acceleration.presentation.permission

import androidx.navigation.NavController
import yin_kio.acceleration.domain.PermissionDialogUseCases

class PermissionDialogUseCasesImpl(
    private val permissionRequester: PermissionRequester
) : PermissionDialogUseCases {

    var navController: NavController? = null

    override fun close() {
        navController?.navigateUp()
    }

    override fun givePermission() {
        permissionRequester.requestPackageUsageStats()
    }
}