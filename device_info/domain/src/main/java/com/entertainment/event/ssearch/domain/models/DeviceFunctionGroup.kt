package com.entertainment.event.ssearch.domain.models

data class DeviceFunctionGroup(
    val parentFun: ParentFun,
    val listFun: List<ChildFun>
)