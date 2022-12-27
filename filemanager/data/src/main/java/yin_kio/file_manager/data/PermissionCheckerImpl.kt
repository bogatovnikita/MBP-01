package yin_kio.file_manager.data

import android.content.Context
import com.example.permissions.hasStoragePermissions
import yin_kio.file_manager.domain.gateways.PermissionChecker

internal class PermissionCheckerImpl(
    private val context: Context
) : PermissionChecker {

    override val hasPermission: Boolean
        get() = context.hasStoragePermissions()

}