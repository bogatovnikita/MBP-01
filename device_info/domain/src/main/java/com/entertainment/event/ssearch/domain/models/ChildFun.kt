package com.entertainment.event.ssearch.domain.models

import com.entertainment.event.ssearch.domain.utility.CHILD

data class ChildFun(
    override val id: Int = Math.random().toInt(),
    override var type: Int = CHILD,
    override val name: Int,
    val body: String,
) : DeviceFunction