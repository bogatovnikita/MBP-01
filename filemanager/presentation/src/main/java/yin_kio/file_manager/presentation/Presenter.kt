package yin_kio.file_manager.presentation

import android.content.Context
import yin_kio.file_manager.domain.models.SortingMode

class Presenter(
    private val context: Context
) {

    fun present(sortingMode: SortingMode) : String{
        return when(sortingMode){
            SortingMode.FromNewToOld -> str(R.string.from_new_to_old)
            SortingMode.FromOldToNew -> str(R.string.from_old_to_new)
            SortingMode.FromBigToSmall -> str(R.string.from_big_to_small)
            SortingMode.FromSmallToBig -> str(R.string.from_small_to_big)
            SortingMode.Disabled -> ""
        }
    }

    private fun str(id: Int) : String{
        return context.getString(id)
    }

}