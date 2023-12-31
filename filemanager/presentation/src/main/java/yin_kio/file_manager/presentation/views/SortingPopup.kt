package yin_kio.file_manager.presentation.views

import android.widget.PopupWindow
import androidx.fragment.app.Fragment
import yin_kio.file_manager.domain.models.SortingMode
import yin_kio.file_manager.presentation.databinding.PopupSortingBinding
import yin_kio.file_manager.presentation.models.ScreenState

fun Fragment.sortingPopup(state: ScreenState,
                          onItemCLick: (SortingMode) -> Unit,
                          onDismiss: () -> Unit
    ) : PopupWindow {
        return PopupWindow(requireContext()).apply {
            val popupBinding = PopupSortingBinding.inflate(layoutInflater).apply {
                val menuItems = mapOf(
                    SortingMode.FromBigToSmall to fromBigToSmall,
                    SortingMode.FromSmallToBig to fromSmallToBig,
                    SortingMode.FromNewToOld to fromNewToOld,
                    SortingMode.FromOldToNew to fromOldToNew,
                )

                menuItems.forEach { pair -> pair.value.setOnClickListener {
                        onItemCLick(pair.key)
                        dismiss()
                    }
                }
                menuItems[state.sortingMode]?.setTextColor(state.sortingIconColor)

            }

            setBackgroundDrawable(null)
            contentView = popupBinding.root
            setOnDismissListener { onDismiss() }
            isOutsideTouchable = true
        }
    }