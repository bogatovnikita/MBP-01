package yin_kio.duplicates.domain.use_cases

import yin_kio.duplicates.domain.models.Destination

interface DuplicateUseCase {
    fun updateFiles()
    fun switchGroupSelection(index: Int)
    fun switchItemSelection(groupIndex: Int, path: String)
    fun navigate(destination: Destination)
    fun unite()
    fun closeInter()
    fun continueUniting()
    fun completeUniting()

}