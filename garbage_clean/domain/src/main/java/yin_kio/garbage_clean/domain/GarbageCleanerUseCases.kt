package yin_kio.garbage_clean.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import yin_kio.garbage_clean.domain.entities.DeleteRequest
import yin_kio.garbage_clean.domain.entities.GarbageFiles
import yin_kio.garbage_clean.domain.entities.GarbageType
import yin_kio.garbage_clean.domain.gateways.Files
import yin_kio.garbage_clean.domain.out.DeleteFormMapper
import yin_kio.garbage_clean.domain.out.OutBoundary

class GarbageCleanerUseCases(
    private val garbageFiles: GarbageFiles,
    private val mapper: DeleteFormMapper,
    private val files: Files,
    private val deleteRequest: DeleteRequest,
    private val outBoundary: OutBoundary,
    private val coroutineScope: CoroutineScope,
    private val updateUseCase: UpdateUseCase
) {

    private val interpreter = DeleteRequestInterpreter(garbageFiles)

    fun switchSelectAll() = async {
        garbageFiles.deleteForm.switchSelectAll()
        val deleteFormOut = mapper.createDeleteFormOut(garbageFiles.deleteForm)
        outBoundary.outDeleteForm(deleteFormOut)
    }
    fun switchSelection(garbageType: GarbageType) = async {
        garbageFiles.deleteForm.switchSelection(garbageType)
    }
    fun deleteIfCan() = async{
        if (deleteRequest.isNotEmpty()) {
            outBoundary.outDeleteProgress(true)
            files.delete(interpreter.interpret(deleteRequest))
            outBoundary.outDeleteProgress(false)
        }
    }

    fun update() = updateUseCase.update()

    private fun async(action: suspend () -> Unit){
        coroutineScope.launch { action() }
    }

}