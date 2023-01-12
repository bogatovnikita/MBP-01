package yin_kio.duplicates.domain

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import yin_kio.duplicates.domain.models.MutableStateHolder

class StateHolderTest {

    @Test
    fun setters() = runBlocking{
        val stateHolder = MutableStateHolder(this)
        stateHolder.isInProgress = false
        stateHolder.duplicatesList = emptyList()
        stateHolder.selected = mutableMapOf()
        assertEquals(
            MutableStateHolder(
            isInProgress = false,
            duplicatesList = emptyList(),
            selected = mutableMapOf()
        ), stateHolder.state)
    }

    @Test
    fun update() = runBlocking{
        val holder = MutableStateHolder(this)
        holder.isInProgress = false
        holder.update()
        val state = holder.stateFlow.first()
        assertEquals(MutableStateHolder(isInProgress = false), state)
    }

}