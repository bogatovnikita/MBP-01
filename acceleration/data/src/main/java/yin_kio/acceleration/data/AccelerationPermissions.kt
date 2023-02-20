package yin_kio.acceleration.data

import android.Manifest
import android.content.Context
import com.example.permissions.hasSpecial
import yin_kio.acceleration.domain.acceleration.gateways.Permissions

class AccelerationPermissions(
    private val context: Context
) : Permissions {

    override val hasPermission: Boolean
        get() = context.hasSpecial(Manifest.permission.PACKAGE_USAGE_STATS)
}