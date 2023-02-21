package yin_kio.acceleration.presentation.selectable_acceleration.adapter

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import yin_kio.acceleration.domain.selectable_acceleration.ui_out.AppsFormState
import yin_kio.acceleration.presentation.AppInfoProvider

class AppItemViewModel(
    private val appInfoProvider: AppInfoProvider,
    private val coroutineScope: CoroutineScope,
    private val appsFormState: AppsFormState
) {


    private val _flow = MutableStateFlow(AppItem())
    val flow = _flow.asStateFlow()

    fun update(packageName: String){
        coroutineScope.launch(Dispatchers.IO) {
            _flow.value = _flow.value.copy(
                icon = appInfoProvider.getIcon(packageName),
                name = appInfoProvider.getName(packageName),
                isSelected = appsFormState.isAppSelected(packageName)
            )
        }
    }

}