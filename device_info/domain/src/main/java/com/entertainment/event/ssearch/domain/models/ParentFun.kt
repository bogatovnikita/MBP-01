package com.entertainment.event.ssearch.domain.models

import com.entertainment.event.ssearch.domain.utility.PARENT


data class ParentFun(
    override val id: Int = Math.random().toInt(),
    override var type: Int = PARENT,
    override val name: Int,
    var isExpanded: Boolean = false,
) : DeviceFunction
