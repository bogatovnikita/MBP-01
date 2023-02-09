package yin_kio.memory.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MemoryUseCases(
    private val outBoundary: OutBoundary,
    private val ramInfo: RamInfo,
    private val storageInfo: StorageInfo,
    private val coroutineScope: CoroutineScope,
    private val dispatcher: CoroutineContext
) {

    fun update(){
        coroutineScope.launch(dispatcher) {
            outBoundary.outMemoryInfos(ramInfo.provide(), storageInfo.provide())
        }
    }

}