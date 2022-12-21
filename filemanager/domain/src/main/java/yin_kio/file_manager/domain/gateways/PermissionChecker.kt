package yin_kio.file_manager.domain.gateways

interface PermissionChecker {
    val hasPermission: Boolean
}