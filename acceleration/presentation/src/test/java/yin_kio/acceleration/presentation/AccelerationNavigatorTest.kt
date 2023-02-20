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
import yin_kio.acceleration.domain.acceleration.ui_out.AccelerationNavigator
import yin_kio.acceleration.presentation.acceleration.AccelerationNavigatorImpl
import yin_kio.acceleration.presentation.inter.Inter

@RunWith(RobolectricTestRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class AccelerationNavigatorTest {


    private val inter: Inter = spyk()
    private val navController: NavController = mockk()
    private lateinit var navigator: AccelerationNavigatorImpl



    private fun setupTest(testBlock: TestScope.() -> Unit) = runTest{
        navigator = AccelerationNavigatorImpl(
            coroutineScope = this,
            inter = inter,
            completeDestination = 0,
            completeArgs = Bundle()
        )
        navigator.navController = navController

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
    fun testShowPermission() = setupTest{
        verifyNavigate(
            navAction = { showPermission() },
            destination = R.id.toAccelerationPermission
        )
    }

    @Test
    fun testShowAccelerationProgress() = setupTest{
        verifyNavigate(
            navAction = { showAccelerateProgress() },
            destination = R.id.toAccelerationDialog
        )
    }

    @Test
    fun testShowSelectableAcceleration() = setupTest {
        verifyNavigate(
            navAction = { showSelectableAcceleration() },
            destination = R.id.toSelectableAcceleration
        )
    }

    @Test
    fun testShowInter() = setupTest {
        navigator.showInter()
        advanceUntilIdle()

        coVerify { inter.show() }
    }

    private fun TestScope.verifyNavigate(
        navAction: AccelerationNavigator.() -> Unit,
        destination: Int
    ) {
        coEvery { navController.navigate(destination)  } returns Unit

        navigator.navAction()
        advanceUntilIdle()

        coVerify { navController.navigate(destination) }
    }
}