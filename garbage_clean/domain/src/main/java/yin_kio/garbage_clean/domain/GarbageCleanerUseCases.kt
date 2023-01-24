package yin_kio.garbage_clean.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import yin_kio.garbage_clean.domain.entities.*
import yin_kio.garbage_clean.domain.gateways.Files
import kotlin.coroutines.CoroutineContext

class GarbageCleanerUseCases(
    private val deleteForm: DeleteForm,
    private val files: Files,
    garbageFiles: GarbageFiles,
    private val deleteRequest: DeleteRequest,
    private val coroutineScope: CoroutineScope,
    private val dispatcher: CoroutineContext
) {

    private val interpreter = DeleteRequestInterpreter(garbageFiles)

    fun selectAll() = async {
        deleteForm.selectAll()
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