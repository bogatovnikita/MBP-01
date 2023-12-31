package yin_kio.duplicates.domain.models

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

internal data class MutableStateHolder(
    override var canUnite: Boolean = true,
    override var isClosed: Boolean = false,
    override var isInProgress: Boolean = true,
    override var duplicatesLists: List<DuplicatesList> = emptyList(),
    override var selected: MutableMap<Int, MutableSet<ImageInfo>> = mutableMapOf(),
    override var destination: Destination = Destination.List
) : StateHolder {

    internal var uniteWay: UniteWay = UniteWay.Selected

    private lateinit var coroutineScope: CoroutineScope
    private lateinit var coroutineContext: CoroutineContext

    constructor(coroutineScope: CoroutineScope, coroutineContext: CoroutineContext) : this(){
        this.coroutineScope = coroutineScope
        this.coroutineContext = coroutineContext
    }

    val state: StateHolder get() = this

    private val _stateFlow = MutableSharedFlow<StateHolder>()
    override val stateFlow: SharedFlow<StateHolder> = _stateFlow.asSharedFlow()


    override fun isItemSelected(groupIndex: Int, path: String): Boolean {
        return selected[groupIndex]?.contains(ImageInfo(path)) ?: false
    }

    override fun isGroupSelected(groupIndex: Int): Boolean {
        if (duplicatesLists.isEmpty() || groupIndex >= duplicatesLists.size) return false
        return (selected[groupIndex]?.size ?: 0) == duplicatesLists[groupIndex].imageInfos.size
    }

    fun update(){
        coroutineScope.launch(coroutineContext) { _stateFlow.emit(this@MutableStateHolder.copy()) }
    }


}