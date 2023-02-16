package yin_kio.acceleration.presentation

import androidx.navigation.NavController
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import yin_kio.acceleration.domain.acceleration.ui_out.AccelerationNavigator

@RunWith(RobolectricTestRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class AccelerationNavigatorTest {

    private val context = RuntimeEnvironment.getApplication()
    private val navController: NavController = mockk()
    private lateinit var navigator: NavigatorImpl



    private fun setupTest(testBlock: TestScope.() -> Unit) = runTest{
        navigator = NavigatorImpl(this)
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