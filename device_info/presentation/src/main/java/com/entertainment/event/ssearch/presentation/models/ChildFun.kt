package com.entertainment.event.ssearch.presentation.models

import com.entertainment.event.ssearch.presentation.utility.CHILD

data class ChildFun(
    override val id: Int = Math.random().toInt(),
    override var type: Int = CHILD,
    override val name: String,
    val body: String,
) : ExpandedAndCollapsedItem