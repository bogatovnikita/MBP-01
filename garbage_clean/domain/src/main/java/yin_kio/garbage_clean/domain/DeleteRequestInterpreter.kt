package yin_kio.garbage_clean.domain

import yin_kio.garbage_clean.domain.entities.DeleteRequest
import yin_kio.garbage_clean.domain.entities.GarbageFiles

class DeleteRequestInterpreter(
    private val garbageFiles: GarbageFiles
) {

    fun interpret(deleteRequest: DeleteRequest) : List<String>{
        val res = mutableListOf<String>()
        deleteRequest.forEach {
            garbageFiles[it]?.let { res.addAll(it) }
        }
        return res
    }

}