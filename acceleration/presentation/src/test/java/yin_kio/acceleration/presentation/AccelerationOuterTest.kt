package yin_kio.acceleration.presentation

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.spyk
import org.junit.Test
import yin_kio.acceleration.domain.acceleration.ui_out.AccelerationNavigator
import yin_kio.acceleration.domain.acceleration.ui_out.RamInfoOut
import yin_kio.acceleration.presentation.acceleration.AccelerationOuterImpl
import yin_kio.acceleration.presentation.acceleration.AccelerationPresenter
import yin_kio.acceleration.presentation.acceleration.AccelerationViewModel
import yin_kio.acceleration.presentation.acceleration.RamInfo


class AccelerationOuterTest {

    private val navigator: AccelerationNavigator = spyk()
    private val viewModel: AccelerationViewModel = spyk()
    private val presenter: AccelerationPresenter = spyk()
    private val outer = AccelerationOuterImpl(
        navigator = navigator,
        viewModel = viewModel,
        presenter = presenter
    )

    @Test
    fun testShowRamInfo(){
        val ramInfo = RamInfo()
        val ramInfoOut = RamInfoOut()

        coEvery { presenter.presentRamInfoOut(ramInfoOut) } returns ramInfo

        outer.showRamInfo(ramInfoOut)

        coVerify { viewModel.setRamInfo(ramInfo) }
    }

}