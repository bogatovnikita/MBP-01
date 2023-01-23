package yin_kio.garbage_clean.domain

data class FormItem(
    val garbageType: GarbageType,
    val size: Long
){

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FormItem

        if (garbageType != other.garbageType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = garbageType.hashCode()
        return result
    }
}