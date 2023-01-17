package yin_kio.duplicates.domain.use_cases

import yin_kio.duplicates.domain.gateways.Files
import yin_kio.duplicates.domain.models.ImageInfo

internal class DuplicateRemoverImpl(
    private val files: Files
) : DuplicateRemover {

    override suspend fun invoke(duplicates: List<Collection<ImageInfo>>) {
        duplicates.forEach{
            copyFirstAndDeleteRest(it)
        }
    }

    private suspend fun copyFirstAndDeleteRest(list: Collection<ImageInfo>) {
        val first = list.first()
        files.copy(first.path, files.folderForUnited())

        list.forEach {
            files.delete(it.path)
        }
    }
}