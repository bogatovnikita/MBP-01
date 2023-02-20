package yin_kio.acceleration.presentation.acceleration.screen

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import yin_kio.acceleration.data.AccelerationPermissions
import yin_kio.acceleration.data.AndroidApps
import yin_kio.acceleration.data.OlejaAds
import yin_kio.acceleration.data.RamInfoProvider
import yin_kio.acceleration.domain.AccelerationDomainFactory
import yin_kio.acceleration.domain.acceleration.ui_out.AccelerationNavigator
import yin_kio.acceleration.presentation.PermissionRequester

class AccelerationViewModelFactory(
    private val context: Context,
    private val navigator: AccelerationNavigator,
    private val permissionRequester: PermissionRequester,
    private val coroutineScope: CoroutineScope
) {

    fun create() : AccelerationViewModel {

        val presenter = AccelerationPresenterImpl(context)

        val outer = AccelerationOuterImpl(
            navigator = navigator,
            presenter = presenter,
            permissionRequester = permissionRequester
        )

        val useCases = AccelerationDomainFactory.createAccelerationUseCases(
            outer = outer,
            permissions = AccelerationPermissions(context),
            apps = AndroidApps(context),
            ramInfo = RamInfoProvider(context),
            coroutineScope = coroutineScope,
            ads = OlejaAds(context)
        )

        val vm = AccelerationViewModel(
            useCases = useCases
        )

        outer.viewModel = vm
        return vm
    }

}