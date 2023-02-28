package yin_kio.acceleration.presentation.selectable_acceleration

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import yin_kio.acceleration.data.AndroidApps
import yin_kio.acceleration.data.OlejaAds
import yin_kio.acceleration.domain.AccelerationDomainFactory
import yin_kio.acceleration.domain.selectable_acceleration.ui_out.SelectableAccelerationNavigator
import yin_kio.acceleration.presentation.selectable_acceleration.view_model.SelectableAccelerationViewModel

fun Fragment.createViewModel() : SelectableAccelerationViewModel {

    val context = requireActivity().application


    val outer = SelectableAccelerationOuterImpl(
        presenter = SelectableAccelerationPresenter()
    )
    val useCases = AccelerationDomainFactory.createSelectableAccelerationUseCases(
        outer = outer,
        apps = AndroidApps(context),
        coroutineScope = viewLifecycleOwner.lifecycleScope,
        ads = OlejaAds(context),
    )


    val viewModel = SelectableAccelerationViewModel(
        useCases = useCases,
        coroutineScope = viewLifecycleOwner.lifecycleScope
    )

    outer.viewModel = viewModel

    return viewModel
}

fun Fragment.createNavigator(
    completeDestination: Int,
    completeDialogTitle: String,
    completeDialogDescription: String,
    onInterClosed: (SelectableAccelerationNavigator) -> Unit
) : SelectableAccelerationNavigator{
    return SelectableAccelerationNavigatorImpl(
        coroutineScope = viewLifecycleOwner.lifecycleScope,
        activity = requireActivity(),
        completeDestination = completeDestination,
        completeArgs =  Bundle().apply {
            putString("dialog_title", completeDialogTitle)
            putString("dialog_description", completeDialogDescription)
        },
        navController = findNavController(),
        onInterClosed = onInterClosed
    )
}