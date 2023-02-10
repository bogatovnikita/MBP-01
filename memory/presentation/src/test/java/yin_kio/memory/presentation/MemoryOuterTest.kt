package yin_kio.memory.presentation

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import yin_kio.memory.domain.MemoryInfoOut


@RunWith(RobolectricTestRunner::class)
class MemoryOuterTest {
    private val ramInfoOut = MemoryInfoOut()
    private val storageInfoOut = MemoryInfoOut()

    private val ramState = MemoryState()
    private val storageState = MemoryState()


    private val viewModel: MutableMemoryViewModel = spyk()

    @Test
    fun testOutMemoryInfos() = runBlocking {
        val outer = MemoryOuter(presenter())
        outer.viewModel = viewModel

        outer.outMemoryInfos(ramInfoOut, storageInfoOut)

        coVerify {
            viewModel.setRamState(ramState)
            viewModel.setStorageState(storageState)
            viewModel.notify()
        }
    }

    private fun presenter(): MemoryPresenter {
        val presenter: MemoryPresenter = mockk()
        coEvery { presenter.presentMemoryState(ramInfoOut) } returns ramState
        coEvery { presenter.presentMemoryState(storageInfoOut) } returns storageState
        return presenter
    }
}