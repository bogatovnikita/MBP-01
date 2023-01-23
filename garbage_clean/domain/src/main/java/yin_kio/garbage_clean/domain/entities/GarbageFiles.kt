package yin_kio.garbage_clean.domain.entities

class GarbageFiles : MutableMap<GarbageType, List<String>> by mutableMapOf() {
}