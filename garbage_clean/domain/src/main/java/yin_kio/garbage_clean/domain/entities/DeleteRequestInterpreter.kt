package yin_kio.garbage_clean.domain.entities

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