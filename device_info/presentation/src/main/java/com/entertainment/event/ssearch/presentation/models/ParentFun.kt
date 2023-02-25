package com.entertainment.event.ssearch.presentation.models

import com.entertainment.event.ssearch.presentation.utility.PARENT

data class ParentFun(
    override val id: Int = Math.random().toInt(),
    override var type: Int = PARENT,
    override val name: String,
    var isExpanded: Boolean = false,
) : ExpandedAndCollapsedItem
