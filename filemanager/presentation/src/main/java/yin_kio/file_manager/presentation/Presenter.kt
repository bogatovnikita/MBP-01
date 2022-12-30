package yin_kio.file_manager.presentation

import android.content.Context
import androidx.core.content.ContextCompat
import yin_kio.file_manager.domain.models.*
import yin_kio.file_manager.presentation.models.FileItem
import yin_kio.file_manager.presentation.models.IconShowingMode

class Presenter(
    private val context: Context
) {

    fun presentFileMode(fileRequest: FileRequest) : FileRequest{
        return fileRequest
    }

    fun presentIsAllSelected(isAllSelected: Boolean) : Boolean = isAllSelected


    fun presentSortingMode(sortingMode: SortingMode) : Int {
        return when(sortingMode){
            SortingMode.Disabled -> color(general.R.color.dark_blue)
            else -> color(general.R.color.green)
        }
    }

    private fun color(id: Int) : Int {
        return ContextCompat.getColor(context, id)
    }

    fun presentMainButton(isEnabled: Boolean) : Int{
        return if (isEnabled) general.R.drawable.bg_main_button_enabled else
            general.R.drawable.bg_main_button_disabled
    }

    fun presentFile(fileInfo: FileInfo) : FileItem{
        return FileItem(
            name = fileInfo.name,
            description = "",
            isSelected = fileInfo.isSelected,
            path = fileInfo.path
        )
    }


    fun presentIconVisibility(fileGroup: FileGroup) : IconShowingMode{
        return when(fileGroup){
            FileGroup.Unknown,
            FileGroup.Documents,
            FileGroup.Audio -> IconShowingMode.Icon
            FileGroup.Images,
            FileGroup.Video -> IconShowingMode.Image
        }
    }

    fun presentListShowingModeIcon(listShowingMode: ListShowingMode) : Int{
        return when(listShowingMode){
            ListShowingMode.List -> R.drawable.ic_showing_mode_grid
            ListShowingMode.Grid -> R.drawable.ic_showing_mode_list
        }
    }

    fun presentProgressAlpha(inProgress: Boolean) : Float{
        return if (inProgress) 0.5f else 1f
    }

    fun presentAskDeleteTitle(fileRequest: FileRequest) : String{
        return when(fileRequest){
            FileRequest.AllFiles -> context.getString(R.string.ask_delete_title_files)
            FileRequest.Images -> context.getString(R.string.ask_delete_title_images)
            FileRequest.Video -> context.getString(R.string.ask_delete_title_video)
            FileRequest.Documents -> context.getString(R.string.ask_delete_title_documents)
            FileRequest.Audio -> context.getString(R.string.ask_delete_title_audio)
        }
    }

    fun presentDeleteProgressTitle(fileRequest: FileRequest) : String{
        return when(fileRequest){
            FileRequest.AllFiles -> context.getString(R.string.delete_progress_title_files)
            FileRequest.Images -> context.getString(R.string.delete_progress_title_images)
            FileRequest.Video -> context.getString(R.string.delete_progress_title_video)
            FileRequest.Documents -> context.getString(R.string.delete_progress_title_documents)
            FileRequest.Audio -> context.getString(R.string.delete_progress_title_audio)
        }
    }

}