package yin_kio.acceleration.presentation

import android.os.Bundle
import androidx.navigation.NavController
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import yin_kio.acceleration.domain.selectable_acceleration.ui_out.SelectableAccelerationNavigator
import yin_kio.acceleration.presentation.selectable_acceleration.SelectableAccelerationNavigatorImpl


@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class SelectableAccelerationNavigatorTest {


    private val navController: NavController = mockk()
    private val completeDestination = 0
    private val completeArgs: Bundle = spyk()
    private lateinit var navigator: SelectableAccelerationNavigatorImpl




    private fun setupTest(testBlock: TestScope.() -> Unit) = runTest{
        navigator = SelectableAccelerationNavigatorImpl(
            coroutineScope = this,
            completeDestination = completeDestination,
            completeArgs = completeArgs,
            activity = spyk(),
            onInterClosed = {},
            navController = navController
        )

        testBlock()
    }

    @Test
    fun testClose() = setupTest{
        coEvery { navController.navigateUp() } returns true

        navigator.close()
        advanceUntilIdle()

        coVerify { navController.navigateUp() }
    }

    @Test
    fun testShowStopProgress() = setupTest{
        verifyNavigate(
            navAction = { showStopProgress() },
            destination = R.id.toStopProgress
        )
    }



    private fun TestScope.verifyNavigate(
        navAction: SelectableAccelerationNavigator.() -> Unit,
        destination: Int
    ) {
        coEvery { navController.navigate(destination)  } returns Unit

        navigator.navAction()
        advanceUntilIdle()

        coVerify { navController.navigate(destination) }
    }
}