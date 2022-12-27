package yin_kio.file_manager.data

import android.os.Environment
import java.io.File

class AndroidFolders : Folders {
    override val root: File
        get() = Environment.getExternalStorageDirectory()
}