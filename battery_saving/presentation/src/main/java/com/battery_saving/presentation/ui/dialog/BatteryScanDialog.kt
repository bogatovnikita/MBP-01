package com.battery_saving.presentation.ui.dialog

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hedgehog.battery_saving.presentation.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class BatteryScanDialog : DialogFragment(R.layout.dialog_battery_scan) {

    override fun onStart() {
        super.onStart()
        setupLayoutParams()
    }

    private fun setupLayoutParams() {
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listItems = listOf(
            getString(R.string.reducing_the_load_on_the_battery),
            getString(R.string.checking_the_system),
            getString(R.string.freezing_background_processes)
        )
        val adapter = BatteryScanAdapter(listItems)
        val recyclerView: RecyclerView = requireView().findViewById(R.id.recycler_view)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }
        val repeat = listItems.size
        lifecycleScope.launch(Dispatchers.Default) {
            repeat(repeat) {
                delay(TimeUnit.SECONDS.toMillis(8) / repeat)
                withContext(Dispatchers.Main) {
                    adapter.removeFirstItem()
                }
            }
            delay(100)
            dialog?.dismiss()
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
        override fun onBackPressed() {
        }
    }

}