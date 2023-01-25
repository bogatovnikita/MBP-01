package yin_kio.garbage_clean.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import yin_kio.garbage_clean.domain.entities.*
import yin_kio.garbage_clean.domain.gateways.Files
import yin_kio.garbage_clean.domain.out.DeleteFormMapper
import yin_kio.garbage_clean.domain.out.OutBoundary
import kotlin.coroutines.CoroutineContext

class GarbageCleanerUseCases(
    private val deleteForm: DeleteForm,
    private val mapper: DeleteFormMapper,
    private val files: Files,
    garbageFiles: GarbageFiles,
    private val deleteRequest: DeleteRequest,
    private val outBoundary: OutBoundary,
    private val coroutineScope: CoroutineScope,
    private val dispatcher: CoroutineContext
) {

    private val interpreter = DeleteRequestInterpreter(garbageFiles)

    fun switchSelectAll() = async {
        deleteForm.switchSelectAll()
        val deleteFormOut = mapper.createDeleteFormOut(deleteForm)
        outBoundary.outDeleteForm(deleteFormOut)
    }
    fun switchSelection(garbageType: GarbageType) = async {
        deleteForm.switchSelection(garbageType)
    }
    fun startDeleteIfCan() = async{
        if (deleteRequest.isNotEmpty()) {
            files.delete(interpreter.interpret(deleteRequest))
        }
    }

    private fun async(action: suspend () -> Unit){
        coroutineScope.launch(dispatcher) { action() }
    }

}