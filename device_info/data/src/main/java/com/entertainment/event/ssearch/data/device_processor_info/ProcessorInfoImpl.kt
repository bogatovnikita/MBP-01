package com.entertainment.event.ssearch.data.device_processor_info

import android.app.Application
import android.content.SharedPreferences
import android.os.Build
import com.entertainment.event.ssearch.data.util.Util
import com.entertainment.event.ssearch.domain.device_info.ProcessorInfo
import com.entertainment.event.ssearch.domain.models.ChildFun
import com.entertainment.event.ssearch.domain.models.DeviceFunctionGroup
import com.entertainment.event.ssearch.domain.models.ParentFun
import com.entertainment.event.ssearch.domain.utility.GL_RENDERER
import general.R
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.RandomAccessFile
import java.util.*
import java.util.regex.Pattern
import javax.inject.Inject

class ProcessorInfoImpl @Inject constructor(
    private val context: Application,
    private val prefs: SharedPreferences
) : Util(context), ProcessorInfo {

    private val model: String
        get() {
            val error = getString(R.string.error)
            return getCPUInfo()["Hardware"] ?: error
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

    private val glRenderer: String
        get() {
            val error = getString(R.string.error)
            return prefs.getString(GL_RENDERER, error) ?: error
        }

    private val instructionSet: String
        get() {
            return System.getProperty("os.arch") as String
        }

    private val id: String
        get() {
            return Build.ID
        }

    private val host: String
        get() {
            return Build.HOST
        }

    private val bootloader: String
        get() {
            return Build.BOOTLOADER
        }

    private fun getCurrentFreq(coreNumber: Int): Long {
        val currentFreqPath = "${CPU_INFO_DIR}cpu$coreNumber/cpufreq/scaling_cur_freq"
        return RandomAccessFile(currentFreqPath, "r").use { it.readLine().toLong() / 1000 }
    }

    override fun getProcessorInfo(): DeviceFunctionGroup {
        val firstPart = listOf(
            ChildFun(name = getString(R.string.model), body = model, id = 500)
        )
        val thirdPart = listOf(
            ChildFun(name = getString(R.string.gpu), body = glRenderer, id = 501),
            ChildFun(name = getString(R.string.instructionSet), body = instructionSet, id = 502),
            ChildFun(name = getString(R.string.id), body = id, id = 503),
            ChildFun(name = getString(R.string.host), body = host, id = 504),
            ChildFun(name = getString(R.string.bootloader), body = bootloader, id = 505),
        )

        return DeviceFunctionGroup(
            parentFun = ParentFun(name = getString(R.string.processor), id = 50),
            listFun = firstPart + listFrequencie + thirdPart
        )
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

    private fun getCPUInfo(): Map<String, String> {
        val output: MutableMap<String, String> = HashMap()
        try {

            val br = BufferedReader(FileReader("/proc/cpuinfo"))
            var str: String
            while (br.readLine().also { str = it } != null) {
                val data = str.split(":".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()
                if (data.size > 1) {
                    val key = data[0].trim { it <= ' ' }.replace(" ", "_")
                    output[key] = data[1].trim { it <= ' ' }
                }
            }
            br.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return output
    }

    companion object {
        private const val CPU_INFO_DIR = "/sys/devices/system/cpu/"
    }
}