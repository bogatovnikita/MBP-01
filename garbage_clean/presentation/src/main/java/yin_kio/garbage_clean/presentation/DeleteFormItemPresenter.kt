package yin_kio.garbage_clean.presentation

import android.content.Context
import yin_kio.garbage_clean.domain.entities.GarbageType
import yin_kio.garbage_clean.domain.out.DeleteFormOutItem

class DeleteFormItemPresenter(
    private val context: Context
) {

    fun presentIcon(garbageType: GarbageType) : Int{
        return when(garbageType){
            GarbageType.Apk -> R.drawable.ic_apk
            GarbageType.Temp -> R.drawable.ic_temp
            GarbageType.RestFiles -> R.drawable.ic_rest
            GarbageType.EmptyFolders -> R.drawable.ic_empty_folders
            GarbageType.Thumbnails -> R.drawable.ic_thumb
        }
    }

    fun presentName(garbageType: GarbageType) : String{
        return when(garbageType){
            GarbageType.Apk -> context.getString(R.string.apk_files)
            GarbageType.Temp -> context.getString(R.string.temp_files)
            GarbageType.RestFiles -> context.getString(R.string.rest_files)
            GarbageType.EmptyFolders -> context.getString(R.string.empty_folders)
            GarbageType.Thumbnails -> context.getString(R.string.miniatures)
        }
    }


}