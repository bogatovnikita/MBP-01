package yin_kio.garbage_clean.domain.out

import yin_kio.garbage_clean.domain.entities.DeleteForm

class DeleteFormMapper {

    fun createDeleteFormOut(deleteForm: DeleteForm) : DeleteFormOut {
        return DeleteFormOut(
            isAllSelected = deleteForm.isAllSelected,
            items = deleteForm.map {
                DeleteFormOutItem(
                    garbageType = it.garbageType,
                    size = it.size,
                    isSelected = deleteForm.isAllSelected || deleteForm.deleteRequest.contains(it.garbageType)
                )
            }
        )
    }

}