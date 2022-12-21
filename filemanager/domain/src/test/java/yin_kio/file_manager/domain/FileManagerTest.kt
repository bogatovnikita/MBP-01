package yin_kio.file_manager.domain

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.*
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
            coEvery { files.getFiles(value) } returns fileInfos().also { runTest { delay(50) } }
        }
    }


    @Test
    fun `updateFiles - if has not permission then state does not contains files and is not in progress`() = runTest{
        assertTrue(state.inProgress)
        fileManager(false).updateFiles()
        assertTrue(state.inProgress)
        advanceUntilIdle()
        assertTrue(state.files.isEmpty())
        assertFalse(state.inProgress)
    }

    @Test
    fun `updateFiles - if has permission then state contains files and is not in progress after progress`() = runTest{
        fileManager(true).updateFiles()
        assertTrue(state.inProgress)
        advanceUntilIdle()
        assertFalse(state.inProgress)
        assertEquals(fileInfos(), state.files)
    }

    private fun fileInfos() = listOf(
        FileInfo(time = 1, size = 1),
        FileInfo(time = 3, size = 3),
        FileInfo(time = 2, size = 2)
    )

    @Test
    fun `switchFileMode - all state values is correct`() = runTest{
        assertFileModeSwitching(fileManager())
    }



    @Test
    fun `updateFiles - call getFiles with correct FileMode`() = runTest{
        assertGetFilesCalls(fileManager())
    }

    @Test
    fun `switchSortingMode - state contains selected sortingMode`() = runTest{
        fileManager().assertSortingModeSwitching()
    }

    @Test
    fun `switchSortingMode(FromOldToNew) - files are sorted from old to new`() = runTest{
        switchSortingMode(SortingMode.FromOldToNew)
        val sorted = listOf(3L,2L,1L)
        assertTrue(sorted.contentEquals(state.files.map { it.time }))
    }

    @Test
    fun `switchSortingMode(FromNewToOld) - files are sorted from new to old`() = runTest {
        switchSortingMode(SortingMode.FromNewToOld)
        val sorted = listOf(1L,2L,3L)
        assertTrue(sorted.contentEquals(state.files.map { it.time }))
    }

    @Test
    fun `switchSortingMode(FromBigToSmall) - files are sorted from big to small`() = runTest{
        switchSortingMode(SortingMode.FromBigToSmall)
        val sorted = listOf(3L,2L,1L)
        assertTrue(sorted.contentEquals(state.files.map { it.size }))
    }

    @Test
    fun `switchSortingMode(FromSmallToBig) - files are sorted from small to big`() = runTest{
        switchSortingMode(SortingMode.FromSmallToBig)
        val sorted = listOf(1L,2L,3L)
        assertTrue(sorted.contentEquals(state.files.map { it.size }))
    }








    private fun List<Long>.contentEquals(other: List<Long>) : Boolean{
        return toLongArray().contentEquals(other.toLongArray())
    }


    private fun TestScope.switchSortingMode(sortingMode: SortingMode){
        fileManager().apply {
            advanceUntilIdle()
            switchSortingMode(sortingMode)
        }
    }











    private fun FileManager.assertSortingModeSwitching() {
        SortingMode.values().forEach {
            switchSortingMode(it)
            assertEquals(it, state.sortingMode)
        }
    }


    private fun assertFileModeSwitching(fileManager: FileManager){
        FileMode.values().forEach {
            fileManager.switchFileMode(it)
            assertEquals(state.fileMode, it)
        }
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



    private fun TestScope.fileManager(hasPermission: Boolean = true) : FileManager{
        return FileManager(state, permissionChecker(hasPermission), files,
            coroutineScope = this
        )
    }

    private fun permissionChecker(returned: Boolean): PermissionChecker {
        val checker = mockk<PermissionChecker>()
        coEvery { checker.hasPermission } returns returned
        return checker
    }

}