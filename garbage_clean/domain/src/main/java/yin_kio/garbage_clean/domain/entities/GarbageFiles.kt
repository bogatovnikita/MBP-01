package yin_kio.garbage_clean.domain.entities

class GarbageFiles : MutableMap<GarbageType, MutableList<String>> by mutableMapOf() {

    fun addTo(garbageType: GarbageType, path: String){
        if (this[garbageType] == null){
            this[garbageType] = mutableListOf(path)
        } else {
            this[garbageType]!!.add(path)
        }
    }

}