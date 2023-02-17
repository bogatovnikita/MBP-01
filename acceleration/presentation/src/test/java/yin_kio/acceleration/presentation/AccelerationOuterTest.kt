package yin_kio.acceleration.presentation

import io.mockk.spyk
import yin_kio.acceleration.domain.acceleration.ui_out.AccelerationNavigator
import yin_kio.acceleration.presentation.acceleration.AccelerationOuterImpl


class AccelerationOuterTest {

    private val navigator: AccelerationNavigator = spyk()
    private val outer = AccelerationOuterImpl(
        navigator = navigator
    )

}