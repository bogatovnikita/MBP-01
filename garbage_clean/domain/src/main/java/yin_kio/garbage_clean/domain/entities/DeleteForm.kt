package yin_kio.garbage_clean.domain.entities

class DeleteForm : MutableSet<FormItem> by mutableSetOf(){

    val canFree: Long get() = sumOf { it.size }

    private var _isAllSelected = false
    val isAllSelected get() = _isAllSelected

    internal val deleteRequest = DeleteRequest()

    fun switchSelection(garbageType: GarbageType){
        if (deleteRequest.contains(garbageType)){
            deleteRequest.remove(garbageType)
        } else {
            deleteRequest.add(garbageType)
        }

        _isAllSelected = deleteRequest.size == size
    }

    fun switchSelectAll(){
        _isAllSelected = !isAllSelected

        if (_isAllSelected){
            deleteRequest.addAll(GarbageType.values())
        } else {
            deleteRequest.clear()
        }
    }

}