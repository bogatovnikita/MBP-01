package com.entertainment.event.ssearch.domain.device_info

import com.entertainment.event.ssearch.domain.models.DeviceFunctionGroup

interface ProcessorInfo {

    fun getProcessorInfo(): DeviceFunctionGroup

}