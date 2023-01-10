package yin_kio.duplicates.domain.models

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

data class MutableStateHolder(
    override var isInProgress: Boolean = true,
    override var duplicatesList: List<List<ImageInfo>> = emptyList(),
    override var selected: MutableMap<Int, Set<ImageInfo>> = mutableMapOf()
) : State {

    private lateinit var coroutineScope: CoroutineScope

    constructor(coroutineScope: CoroutineScope) : this(){
        this.coroutineScope = coroutineScope
    }

    val state: State get() = this

    private val _stateFlow = MutableSharedFlow<State>()
    override val stateFlow: SharedFlow<State> = _stateFlow.asSharedFlow()


    fun update(){
        coroutineScope.launch { _stateFlow.emit(this@MutableStateHolder.copy()) }
    }


}