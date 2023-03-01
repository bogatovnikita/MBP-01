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
import com.battery_saving.presentation.utils.KillBackgroundProcesses
import com.hedgehog.battery_saving.presentation.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class BatteryScanDialog : DialogFragment(R.layout.dialog_battery_scan) {

    private var callbackCloseDialog: CallbackCloseDialog? = null
    fun addCallbackCloseDialog(callbackCloseDialog: CallbackCloseDialog) {
        this.callbackCloseDialog = callbackCloseDialog
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return noCancelableDialog()
    }

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

        val adapter = BatteryScanAdapter(makeList())
        initRecyclerView(adapter)

        scanBattery(adapter)
    }

    private fun makeList(): List<String> {
        return listOf(
            getString(R.string.reducing_the_load_on_the_battery),
            getString(R.string.checking_the_system),
            getString(R.string.freezing_background_processes)
        )
    }

    private fun initRecyclerView(adapter: BatteryScanAdapter) {
        val recyclerView: RecyclerView = requireView().findViewById(R.id.recycler_view)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }
    }

    private fun scanBattery(adapter: BatteryScanAdapter) {
        KillBackgroundProcesses(requireContext()).killAllProcesses()
        val repeat = makeList().size
        lifecycleScope.launch(Dispatchers.Default) {
            repeat(repeat) {
                delay(TimeUnit.SECONDS.toMillis(8) / repeat)
                withContext(Dispatchers.Main) {
                    adapter.removeFirstItem()
                }
            }
            delay(100)
            callbackCloseDialog?.closeDialog()
            dialog?.dismiss()
        }
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