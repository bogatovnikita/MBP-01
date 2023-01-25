package yin_kio.duplicates.domain.use_cases

import yin_kio.duplicates.domain.models.ImageInfo

internal interface DuplicateRemover : suspend (List<Collection<ImageInfo>>) -> Unit