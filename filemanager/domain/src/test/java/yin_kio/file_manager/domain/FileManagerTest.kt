package yin_kio.file_manager.domain

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.*
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class FileManagerTest{

    private lateinit var state: MutableState
    private lateinit var files: Files


    @BeforeEach
    fun setup(){
        state = MutableState()
        files = mockk()
        coEvery { files.getFiles() } returns listOf(FileInfo()).also { runTest { delay(50) } }
    }

    @Test
    fun `init - has permission - state_hasPermission = true`() = runTest{

        fileManager(true, coroutineScope = this)
        advanceUntilIdle()
        assertEquals(true, state.hasPermission)

    }

    @Test
    fun `init - has not permission - state_hasPermission = false`() = runTest{
        fileManager(false)
        advanceUntilIdle()
        assertEquals(false, state.hasPermission)
    }


    @Test
    fun `init - if has permission call files_getFiles()`() = runTest {
        fileManager(true, coroutineScope = CoroutineScope(Dispatchers.IO))
        coVerify { files.getFiles() }
    }

    @Test
    fun `init = if has not permission does not call files_getFiles()`(){
        fileManager(false)
        coVerify(exactly = 0) { files.getFiles() }
    }

    @Test
    fun `init - if has permission then state contains files and is not in progress`() = runTest{
        assertTrue(state.inProgress)
        fileManager(true, coroutineScope = this)
        advanceUntilIdle()
        assertEquals(listOf(FileInfo()), state.files)
        assertFalse(state.inProgress)
    }

    @Test
    fun `init - if has not permission then state does not contains files and is not in progress`() = runTest{
        assertTrue(state.inProgress)
        fileManager(false, coroutineScope = this)
        advanceUntilIdle()
        assertTrue(state.files.isEmpty())
        assertFalse(state.inProgress)
    }

    @Test
    fun `updateFiles - if has permission then state contains files and is not in progress after progress`() = runTest{
        fileManager(true, coroutineScope = this).updateFiles()
        assertTrue(state.inProgress)
        advanceUntilIdle()
        assertFalse(state.inProgress)
    }



    private fun fileManager(hasPermission: Boolean = true, coroutineScope: CoroutineScope? = null) : FileManager{
        return FileManager(state, permissionChecker(hasPermission), files,
            coroutineScope = coroutineScope ?: CoroutineScope(StandardTestDispatcher())
        )
    }

    private fun permissionChecker(returned: Boolean): PermissionChecker {
        val checker = mockk<PermissionChecker>()
        coEvery { checker.hasPermission } returns returned
        return checker
    }

}