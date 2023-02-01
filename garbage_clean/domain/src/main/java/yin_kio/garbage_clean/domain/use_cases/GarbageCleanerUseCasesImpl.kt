package yin_kio.garbage_clean.domain.use_cases

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import yin_kio.garbage_clean.domain.entities.GarbageFiles
import yin_kio.garbage_clean.domain.entities.GarbageType
import yin_kio.garbage_clean.domain.gateways.Ads
import yin_kio.garbage_clean.domain.gateways.Files
import yin_kio.garbage_clean.domain.out.DeleteProgressState
import yin_kio.garbage_clean.domain.out.OutBoundary
import yin_kio.garbage_clean.domain.services.DeleteFormMapper
import yin_kio.garbage_clean.domain.services.DeleteRequestInterpreter
import kotlin.coroutines.CoroutineContext

internal class GarbageCleanerUseCasesImpl(
    private val garbageFiles: GarbageFiles,
    private val mapper: DeleteFormMapper,
    private val files: Files,
    private val outBoundary: OutBoundary,
    private val coroutineScope: CoroutineScope,
    private val dispatcher: CoroutineContext,
    private val updateUseCase: UpdateUseCase,
    private val ads: Ads
) : GarbageCleanUseCases {

    private val interpreter = DeleteRequestInterpreter(garbageFiles)

    override fun switchSelectAll() = async {
        garbageFiles.deleteForm.switchSelectAll()
        updateDeleteForm()
    }
    override fun switchSelection(garbageType: GarbageType) = async {
        garbageFiles.deleteForm.switchSelection(garbageType)
        updateDeleteForm()
    }

    private fun updateDeleteForm() {
        val deleteFormOut = mapper.createDeleteFormOut(garbageFiles.deleteForm)
        outBoundary.outDeleteForm(deleteFormOut)
    }

    override fun deleteIfCan() = async{
        val deleteRequest = garbageFiles.deleteForm.deleteRequest
        if (deleteRequest.isNotEmpty()) {
            ads.preloadAd()
            outBoundary.outDeleteProgress(DeleteProgressState.Progress)
            files.delete(interpreter.interpret(deleteRequest))
            outBoundary.outDeleteProgress(DeleteProgressState.Complete)
        }
    }

    override fun update() = updateUseCase.update()

    private fun async(action: suspend () -> Unit){
        coroutineScope.launch(dispatcher) { action() }
    }

}