package yin_kio.file_manager.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class FileManager(
    private val state: MutableState,
    private val permissionChecker: PermissionChecker,
    private val files: Files,
    private val coroutineScope: CoroutineScope
) {

    init {
        updateFiles()
    }

    fun updateFiles() {
        coroutineScope.launch {
            state.apply {
                hasPermission = permissionChecker.hasPermission
                if (hasPermission) {
                    inProgress = true
                    files = this@FileManager.files.getFiles()
                    inProgress = false
                } else {
                    state.inProgress = false
                }
            }
        }

    }


}