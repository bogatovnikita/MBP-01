package yin_kio.file_manager.presentation

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.bumptech.glide.Glide
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

    fun presentShowingMode(showingMode: ListShowingMode) : LayoutManager{
        return when(showingMode){
            ListShowingMode.List -> LinearLayoutManager(context)
            ListShowingMode.Grid -> GridLayoutManager(context, 2)
        }
    }

    fun presentSortingMode(sortingMode: SortingMode) : Int {
        return when(sortingMode){
            SortingMode.Disabled -> context.getColor(general.R.color.dark_blue)
            else -> context.getColor(general.R.color.green)
        }
    }

    fun presentMainButton(isEnabled: Boolean) : Int{
        return if (isEnabled) general.R.drawable.bg_main_button_enabled else
            general.R.drawable.bg_main_button_disabled
    }

    fun presentFile(fileInfo: FileInfo) : FileItem{
        return FileItem(
            name = fileInfo.name,
            description = fileInfo.description,
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

}