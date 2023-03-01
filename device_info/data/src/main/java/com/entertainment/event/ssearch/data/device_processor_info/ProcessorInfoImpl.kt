package com.entertainment.event.ssearch.data.device_processor_info

import android.app.Application
import com.entertainment.event.ssearch.data.util.Util
import com.entertainment.event.ssearch.domain.device_info.ProcessorInfo
import com.entertainment.event.ssearch.domain.models.ChildFun
import com.entertainment.event.ssearch.domain.models.DeviceFunctionGroup
import com.entertainment.event.ssearch.domain.models.ParentFun
import general.R
import java.io.*
import java.util.*
import java.util.regex.Pattern
import javax.inject.Inject

class ProcessorInfoImpl @Inject constructor(
    private val context: Application
) : Util(context), ProcessorInfo {

    private val instructionSet: String
        get() {
            return System.getProperty("os.arch") as String
        }

    private val numOfCores: Int
        get() {
            return Objects.requireNonNull(File("/sys/devices/system/cpu/").listFiles { params ->
                Pattern.matches(
                    "cpu[0-9]",
                    params.name
                )
            }).size
        }

    private val listFrequencie: List<ChildFun>
        get() {
            val list = mutableListOf<ChildFun>()
            if (numOfCores == 0) {
                return emptyList()
            } else {
                for (number in 0 until numOfCores) {
                    list.add(
                        ChildFun(
                            name = context.getString(R.string.core, number + 1),
                            body = "${getCurrentFreq(number)} MHz",
                            id = 150 + number
                        )
                    )
                }
            }
            return list
        }

    private fun getCurrentFreq(coreNumber: Int): Long {
        val currentFreqPath = "${CPU_INFO_DIR}cpu$coreNumber/cpufreq/scaling_cur_freq"
        return RandomAccessFile(currentFreqPath, "r").use { it.readLine().toLong() / 1000 }
    }

    override fun getProcessorInfo(): DeviceFunctionGroup = DeviceFunctionGroup(
        parentFun = ParentFun(name = getString(R.string.processor), id = 50),
        listFun = listFrequencie
    )

    companion object {
        private const val CPU_INFO_DIR = "/sys/devices/system/cpu/"
    }
}