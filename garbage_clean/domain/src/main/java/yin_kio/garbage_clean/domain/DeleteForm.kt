package yin_kio.garbage_clean.domain

class DeleteForm : MutableSet<FormItem> by mutableSetOf(){

    val canFree: Long get() = sumOf { it.size }

    val deleteRequest = DeleteRequest()

    fun select(garbageType: GarbageType){
        deleteRequest.add(garbageType)
    }

    fun selectAll(){
        deleteRequest.addAll(GarbageType.values())
    }

}