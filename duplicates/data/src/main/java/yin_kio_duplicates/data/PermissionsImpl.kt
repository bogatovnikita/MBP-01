package yin_kio_duplicates.data

import android.content.Context
import com.example.permissions.hasStoragePermissions
import yin_kio.duplicates.domain.gateways.Permissions

class PermissionsImpl(
    private val context: Context
) : Permissions {

    override val hasStoragePermissions: Boolean
        get() = context.hasStoragePermissions()
}