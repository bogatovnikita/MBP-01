package yin_kio.garbage_clean.domain.gateways

interface Files {

    suspend fun delete(paths: List<String>)
    suspend fun getAll() : List<String>

}