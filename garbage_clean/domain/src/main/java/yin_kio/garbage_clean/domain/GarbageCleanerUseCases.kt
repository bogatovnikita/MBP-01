package yin_kio.garbage_clean.domain

import yin_kio.garbage_clean.domain.entities.*
import yin_kio.garbage_clean.domain.gateways.Files

class GarbageCleanerUseCases(
    private val deleteForm: DeleteForm,
    private val files: Files,
    garbageFiles: GarbageFiles,
    private val deleteRequest: DeleteRequest,
) {

    private val interpreter = DeleteRequestInterpreter(garbageFiles)

    fun selectAll(){
        deleteForm.selectAll()
    }
    fun switchSelection(garbageType: GarbageType){
        deleteForm.switchSelection(garbageType)
    }
    fun startDeleteIfCan(){
        if (deleteRequest.isNotEmpty()) {
            files.delete(interpreter.interpret(deleteRequest))
        }
    }

}