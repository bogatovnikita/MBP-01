package yin_kio.acceleration.presentation.selectable_acceleration

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.recycler_adapter.recyclerAdapter
import jamycake.lifecycle_aware.lifecycleAware
import yin_kio.acceleration.domain.selectable_acceleration.entities.App
import yin_kio.acceleration.presentation.R
import yin_kio.acceleration.presentation.databinding.DialogStopSelectedAppsBinding
import yin_kio.acceleration.presentation.databinding.ListItmeDialogAppBinding

class SelectableAccelerationDialog : DialogFragment(R.layout.dialog_stop_selected_apps) {

    private val binding: DialogStopSelectedAppsBinding by viewBinding()
    private val viewModel: SelectableAccelerationViewModel by lifecycleAware(
        viewModelStoreOwner = { findNavController().previousBackStackEntry!! }
    )
    private val adapter by lazy { recyclerAdapter<App, ListItmeDialogAppBinding>(
        onBind = {item, _ ->
            icon.setImageDrawable(item.icon as Drawable)
            name.text = item.name
        }
    ) }


    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recycler.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.flow.collect{
                adapter.submitList(it.selectedApps)
            }
        }

    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return noCancelableDialog()
    }

    private fun noCancelableDialog() = object : Dialog(requireContext()) {
        init {
            setCancelable(false)
            setCanceledOnTouchOutside(false)

            window?.setBackgroundDrawable(ColorDrawable(android.R.color.transparent))
        }

        @Deprecated("Deprecated in Java")
        override fun onBackPressed() {}
    }


}