package yin_kio.file_manager.domain

import io.mockk.coEvery
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
    fun `switchFileMode(Images) - state contains mode images`(){
        fileManager().switchFileMode(FileMode.Images)
        assertEquals(state.fileMode, FileMode.Images)
    }

    @Test
    fun `switchFileMode(AllFiles) - state contains mode AllFiles`(){
        fileManager().switchFileMode(FileMode.AllFiles)
        assertEquals(state.fileMode, FileMode.AllFiles)
    }

    @Test
    fun `switchFileMode(Video) - state contains mode Video`(){
        fileManager().switchFileMode(FileMode.Video)
        assertEquals(state.fileMode, FileMode.Video)
    }

    @Test
    fun `switchFileMode(Documents) - state contains mode Documents`(){
        fileManager().switchFileMode(FileMode.Documents)
        assertEquals(state.fileMode, FileMode.Documents)
    }

    @Test
    fun `switchFileMode(Audio) - state contains mode Audio`(){
        fileManager().switchFileMode(FileMode.Audio)
        assertEquals(state.fileMode, FileMode.Audio)
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