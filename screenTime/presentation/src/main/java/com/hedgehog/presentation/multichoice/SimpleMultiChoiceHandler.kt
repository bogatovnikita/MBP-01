package com.hedgehog.presentation.multichoice

import com.hedgehog.presentation.models.AppScreenTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SimpleMultiChoiceHandler : MultiChoiceHandler<AppScreenTime>,
    MultiChoiceState<AppScreenTime> {

    private val checkedIds = HashSet<String>()
    private var items: List<AppScreenTime> = emptyList()
    private val stateFlow = MutableStateFlow(OnChanged())

    override fun setItemsFlow(
        coroutineScope: CoroutineScope,
        itemsFlow: Flow<List<AppScreenTime>>
    ) {
        coroutineScope.launch {
            itemsFlow.collectLatest { list ->
                items = list
                removeDeleted(list)
                notifyUpdates()
            }
        }
    }

    override fun listen(): Flow<MultiChoiceState<AppScreenTime>> {
        return stateFlow.map { this }
    }

    override fun toggle(item: AppScreenTime) {
        if (isChecked(item)) {
            clear(item)
        } else {
            check(item)
        }
    }

    override fun selectAll() {
        checkedIds.addAll(items.map { it.name })
        notifyUpdates()
    }

    override fun clearAll() {
        checkedIds.clear()
        notifyUpdates()
    }

    override fun check(item: AppScreenTime) {
        if (!exists(item)) return
        checkedIds.add(item.name)
        notifyUpdates()
    }

    override fun clear(item: AppScreenTime) {
        if (!exists(item)) return
        checkedIds.remove(item.name)
        notifyUpdates()
    }

    override fun isChecked(item: AppScreenTime): Boolean {
        return checkedIds.contains(item.name)
    }

    private fun removeDeleted(app: List<AppScreenTime>) {
        val existingIds = HashSet(app.map { it.name })
        val iterator = checkedIds.iterator()
        while (iterator.hasNext()) {
            val id = iterator.next()
            if (!existingIds.contains(id)) {
                iterator.remove()
            }
        }
    }

    private fun notifyUpdates() {
        stateFlow.value = OnChanged()
    }

    private fun exists(item: AppScreenTime): Boolean {
        return items.any { it.name == item.name }
    }

    override val totalCheckedCount: Int
        get() = checkedIds.size

    private class OnChanged
}