package yin_kio.garbage_clean.domain.entities

class DeleteForm : MutableSet<FormItem> by mutableSetOf(){

    val canFree: Long get() = sumOf { it.size }

    internal val deleteRequest = DeleteRequest()

    fun switchSelection(garbageType: GarbageType){
        if (deleteRequest.contains(garbageType)){
            deleteRequest.remove(garbageType)
        } else {
            deleteRequest.add(garbageType)
        }
    }

    fun switchSelectAll(){
        if (deleteRequest.containsAll(GarbageType.values().toList())){
            deleteRequest.clear()
        } else {
            deleteRequest.addAll(GarbageType.values())
        }
    }

}