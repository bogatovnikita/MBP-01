package yin_kio.acceleration.presentation.selectable_acceleration

import android.app.Activity
import android.os.Bundle
import androidx.navigation.NavController
import com.example.ads.showInter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import yin_kio.acceleration.domain.selectable_acceleration.ui_out.SelectableAccelerationNavigator
import yin_kio.acceleration.presentation.R

class SelectableAccelerationNavigatorImpl(
    private val coroutineScope: CoroutineScope,
    private val activity: Activity,
    private val onInterClosed: (SelectableAccelerationNavigator) -> Unit,
    private val completeDestination: Int,
    private val completeArgs: Bundle,
    private val navController: NavController
) : SelectableAccelerationNavigator {


    override fun close() = onMain {
        navController.navigateUp()
    }

    override fun showStopProgress() = onMain {
        navController.navigate(R.id.toStopProgress)
    }

    override fun showInter() = onMain {
        activity.showInter(onClosed = { onInterClosed(this) })
    }

    override fun complete() = onMain {
        navController.popBackStack(R.id.accelerationFragment, true)
        navController.navigate(completeDestination, completeArgs)
    }

    private fun onMain(action: () -> Unit){
        coroutineScope.launch {
            action()
        }
    }
}