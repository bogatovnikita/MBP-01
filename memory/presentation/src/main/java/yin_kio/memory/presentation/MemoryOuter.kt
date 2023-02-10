package yin_kio.memory.presentation

import yin_kio.memory.domain.MemoryInfoOut
import yin_kio.memory.domain.OutBoundary

class MemoryOuter(
    private val presenter: MemoryPresenter
) : OutBoundary {

    var viewModel: MutableMemoryViewModel? = null

    override suspend fun outMemoryInfos(ramInfo: MemoryInfoOut, storageInfo: MemoryInfoOut) {
        viewModel?.setRamState(presenter.presentMemoryState(ramInfo))
        viewModel?.setStorageState(presenter.presentMemoryState(storageInfo))
        viewModel?.notify()
    }
}