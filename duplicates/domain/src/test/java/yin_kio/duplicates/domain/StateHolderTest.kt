package yin_kio.duplicates.domain

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import yin_kio.duplicates.domain.models.ImageInfo
import yin_kio.duplicates.domain.models.MutableStateHolder

class StateHolderTest {

    private lateinit var stateHolder: MutableStateHolder

    @BeforeEach
    fun setup(){
        stateHolder = MutableStateHolder()
    }

    @Test
    fun setters() {
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
        val stateHolder = MutableStateHolder( coroutineScope =  this, coroutineContext = coroutineContext)

        stateHolder.isInProgress = false
        stateHolder.update()
        val state = stateHolder.stateFlow.first()
        assertEquals(MutableStateHolder(isInProgress = false), state)
    }

    @Test
    fun isItemSelected() {
        stateHolder.selected[0] = mutableSetOf(ImageInfo(FILE_PATH_1))
        assertTrue(stateHolder.isItemSelected(0 , FILE_PATH_1))

        stateHolder.selected.remove(0)
        assertFalse(stateHolder.isItemSelected(0 , FILE_PATH_1))
    }

    @Test
    fun isGroupSelected() {
        stateHolder.duplicatesList = listOf(listOf(ImageInfo(FILE_PATH_1), ImageInfo(FILE_PATH_2)))

        stateHolder.selected[0] = mutableSetOf(ImageInfo(FILE_PATH_1))
        assertFalse(stateHolder.isGroupSelected(0))

        stateHolder.selected[0] = mutableSetOf(ImageInfo(FILE_PATH_1), ImageInfo(FILE_PATH_2))
        assertTrue(stateHolder.isGroupSelected(0))

        stateHolder.selected.remove(0)
        assertFalse(stateHolder.isGroupSelected(0))
    }

    companion object{
        private const val FILE_PATH_1 = "a"
        private const val FILE_PATH_2 = "b"
    }

}