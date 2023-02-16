package yin_kio.acceleration.presentation

import io.mockk.coVerify
import io.mockk.spyk
import org.junit.Test
import yin_kio.acceleration.domain.acceleration.ui_out.AccelerationNavigator


class AccelerationOuterTest {

    private val navigator: AccelerationNavigator = spyk()
    private val outer = AccelerationOuterImpl(
        navigator = navigator
    )

}