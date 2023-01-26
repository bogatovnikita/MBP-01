package yin_kio.garbage_clean.domain.gateways

interface Files {

    fun delete(paths: List<String>)
    fun getAll() : List<String>

}