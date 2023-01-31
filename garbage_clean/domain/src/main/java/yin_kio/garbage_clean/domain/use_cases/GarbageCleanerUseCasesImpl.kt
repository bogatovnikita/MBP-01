package yin_kio.garbage_clean.domain.use_cases

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import yin_kio.garbage_clean.domain.services.DeleteFormMapper
import yin_kio.garbage_clean.domain.services.DeleteRequestInterpreter
import yin_kio.garbage_clean.domain.entities.DeleteRequest
import yin_kio.garbage_clean.domain.entities.GarbageFiles
import yin_kio.garbage_clean.domain.entities.GarbageType
import yin_kio.garbage_clean.domain.gateways.Ads
import yin_kio.garbage_clean.domain.gateways.Files
import yin_kio.garbage_clean.domain.out.DeleteProgressState
import yin_kio.garbage_clean.domain.out.OutBoundary

internal class GarbageCleanerUseCasesImpl(
    private val garbageFiles: GarbageFiles,
    private val mapper: DeleteFormMapper,
    private val files: Files,
    private val deleteRequest: DeleteRequest,
    private val outBoundary: OutBoundary,
    private val coroutineScope: CoroutineScope,
    private val updateUseCase: UpdateUseCase,
    private val ads: Ads
) : GarbageCleanUseCases {

    private val interpreter = DeleteRequestInterpreter(garbageFiles)

    override fun switchSelectAll() = async {
        garbageFiles.deleteForm.switchSelectAll()
        val deleteFormOut = mapper.createDeleteFormOut(garbageFiles.deleteForm)
        outBoundary.outDeleteForm(deleteFormOut)
    }
    override fun switchSelection(garbageType: GarbageType) = async {
        garbageFiles.deleteForm.switchSelection(garbageType)
    }
    override fun deleteIfCan() = async{
        if (deleteRequest.isNotEmpty()) {
            ads.preloadAd()
            outBoundary.outDeleteProgress(DeleteProgressState.Progress)
            files.delete(interpreter.interpret(deleteRequest))
            outBoundary.outDeleteProgress(DeleteProgressState.Complete)
        }
    }

    override fun update() = updateUseCase.update()

    private fun async(action: suspend () -> Unit){
        coroutineScope.launch { action() }
    }

}