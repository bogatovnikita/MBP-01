package yin_kio.garbage_clean.domain

import yin_kio.garbage_clean.domain.entities.DeleteForm
import yin_kio.garbage_clean.domain.entities.GarbageType

class GarbageCleanerUseCases(
    private val deleteForm: DeleteForm
) {

    fun selectAll(){
        deleteForm.selectAll()
    }
    fun switchSelection(garbageType: GarbageType){
        deleteForm.switchSelection(garbageType)
    }
    fun startDelete(){

    }

}