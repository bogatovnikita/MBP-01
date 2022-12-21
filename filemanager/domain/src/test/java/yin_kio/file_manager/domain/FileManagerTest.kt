package yin_kio.file_manager.domain

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.*
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
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
        for (value in FileMode.values()) {
            coEvery { files.getFiles(value) } returns listOf(FileInfo()).also { runTest { delay(50) } }
        }
    }


    @Test
    fun `updateFiles - if has not permission then state does not contains files and is not in progress`() = runTest{
        assertTrue(state.inProgress)
        fileManager(false, coroutineScope = this).updateFiles()
        assertTrue(state.inProgress)
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
        assertEquals(listOf(FileInfo()), state.files)
    }

    @Test
    fun `switchFileMode - all state values is correct`(){
        assertFileModeSwitching(fileManager())
    }

    private fun assertFileModeSwitching(fileManager: FileManager){
        FileMode.values().forEach {
            fileManager.switchFileMode(it)
            assertEquals(state.fileMode, it)
        }
    }

    @Test
    fun `updateFiles - call getFiles with correct FileMode`() = runTest{
        assertGetFilesCalls(fileManager(coroutineScope = this))
    }

    private fun TestScope.assertGetFilesCalls(fileManager: FileManager){
        for (value in FileMode.values()) {
            fileManager.apply {
                switchFileMode(value)
                updateFiles()
            }
            advanceUntilIdle()
            coVerify { files.getFiles(value) }
        }

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