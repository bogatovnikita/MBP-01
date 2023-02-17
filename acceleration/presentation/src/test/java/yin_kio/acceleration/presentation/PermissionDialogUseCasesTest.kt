package yin_kio.acceleration.presentation

import androidx.navigation.NavController
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import org.junit.Test

class PermissionDialogUseCasesTest {

    private val navController: NavController = mockk()
    private val permissionRequester: PermissionRequester = spyk()
    private val useCases = PermissionDialogUseCasesImpl(
        permissionRequester = permissionRequester
    )

    init {
        useCases.navController = navController
    }

    @Test
    fun testClose(){
        coEvery { navController.navigateUp() } returns true

        useCases.close()

        coVerify { navController.navigateUp() }
    }

    @Test
    fun testGivePermission(){
        useCases.givePermission()

        coVerify { permissionRequester.requestPackageUsageStats() }
    }

}