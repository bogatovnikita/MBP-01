package yin_kio.acceleration.presentation.permission

import android.app.Activity
import com.example.permissions.requestPackageUsageStats

class PermissionRequesterImpl : PermissionRequester {

    var activity: Activity? = null

    override fun requestPackageUsageStats() {
        activity?.requestPackageUsageStats()
    }
}