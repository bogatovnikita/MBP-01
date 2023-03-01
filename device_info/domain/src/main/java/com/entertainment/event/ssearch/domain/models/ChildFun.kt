package com.entertainment.event.ssearch.domain.models

import com.entertainment.event.ssearch.domain.utility.CHILD

data class ChildFun(
    override val id: Int,
    override var type: Int = CHILD,
    override val name: String,
    val body: String,
) : DeviceFunction