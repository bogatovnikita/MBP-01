package yin_kio.acceleration.presentation

import android.app.Activity
import androidx.navigation.NavController
import com.example.permissions.requestPackageUsageStats
import yin_kio.acceleration.domain.PermissionDialogUseCases

class PermissionDialogUseCasesImpl : PermissionDialogUseCases {

    var activity: Activity? = null
    var navController: NavController? = null

    override fun close() {
        navController?.navigateUp()
    }

    override fun givePermission() {
        activity?.requestPackageUsageStats()
    }
}