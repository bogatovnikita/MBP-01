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
        FileInfo(time = 2, size = 2, path = "path")
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
        callAfterLoading { switchSortingMode(SortingMode.FromOldToNew) }
        val sorted = listOf(3L,2L,1L)
        assertTrue(sorted.contentEquals(state.files.map { it.time }))
    }

    @Test
    fun `switchSortingMode(FromNewToOld) - files are sorted from new to old`() = runTest {
        callAfterLoading {  switchSortingMode(SortingMode.FromNewToOld) }
        val sorted = listOf(1L,2L,3L)
        assertTrue(sorted.contentEquals(state.files.map { it.time }))
    }

    @Test
    fun `switchSortingMode(FromBigToSmall) - files are sorted from big to small`() = runTest{
        callAfterLoading { switchSortingMode(SortingMode.FromBigToSmall) }
        val sorted = listOf(3L,2L,1L)
        assertTrue(sorted.contentEquals(state.files.map { it.size }))
    }

    @Test
    fun `switchSortingMode(FromSmallToBig) - files are sorted from small to big`() = runTest{
        callAfterLoading { switchSortingMode(SortingMode.FromSmallToBig) }
        val sorted = listOf(1L,2L,3L)
        assertTrue(sorted.contentEquals(state.files.map { it.size }))
    }

    @Test
    fun `switchSelectAll - all files are selected and isAllSelected is true`() = runTest {
        callAfterLoading{
            switchSelectAll()
        }
        assertTrue(state.isAllSelected)
        assertEquals(fileInfos().onEach { it.isSelected = true }, state.selectedFiles)
        state.files.forEach{ assertTrue(it.isSelected) }
    }

    @Test
    fun `switchSelectAll - state_selectedFiles not contains all files and each file flag isSelected is false`() = runTest {
        callAfterLoading {
            switchSelectAll()
            switchSelectAll()
        }
        assertFalse(state.isAllSelected)
        assertEquals(emptyList<FileInfo>(), state.selectedFiles)
        state.files.forEach{ assertFalse(it.isSelected) }
    }

    @Test
    fun `switchShowingMode - state contains grid showing mode`() = runTest{
        fileManager().switchShowingMode()
        assertEquals(ShowingMode.Grid, state.showingMode)
    }

    @Test
    fun `switchShowingMode twice - state contains list showing mode`() = runTest{
        fileManager().apply {
            switchShowingMode()
            switchShowingMode()
        }
        assertEquals(ShowingMode.List, state.showingMode)
    }

    @Test
    fun `switchSelectFile - state_selectedFiles contains file and file_isSelected is true`() = runTest{
        callAfterLoading { switchSelectFile("path") }
        assertNotNull(state.selectedFiles.find { it.path == "path" })
        assertTrue(state.files.find { it.path == "path" }?.isSelected ?: false)
    }

    @Test
    fun `switchSelectFile twice - state_selectedFiles not contains file and file_isSelected is false`() = runTest{
        callAfterLoading {
            switchSelectFile("path")
            switchSelectFile("path")
        }
        assertNull(state.selectedFiles.find { it.path == "path" })
        assertNotNull(state.files.find { it.path == "path" })
        assertFalse(state.files.find { it.path == "path" }?.isSelected ?: false)
    }

    @Test
    fun `switchSelectFile - if all files are selected then isAllSelected equals true`() = runTest{
        callAfterLoading {
            state.selectedFiles.addAll(state.files.filter { it.path != "path" })
            switchSelectFile("path")
        }
        assertTrue(state.isAllSelected)
        assertEquals(state.files, state.selectedFiles)
    }

    @Test
    fun `switchSelectFile - if have not selected then isAllSelected equals false`() = runTest{
        callAfterLoading {
            switchSelectFile("path")
            switchSelectFile("path")
        }
        assertFalse(state.isAllSelected)
        assertEquals(emptyList<FileInfo>(), state.selectedFiles)
    }

    @Test
    fun `switchSelectFile - if not all files selected then isAllSelected equals false`() = runTest{
        callAfterLoading {
            switchSelectFile("path")
        }
        assertFalse(state.isAllSelected)
        val file = fileInfos().find { it.path == "path" }!!.apply { isSelected = true }
        assertEquals(listOf(file), state.selectedFiles)
    }

    @Test
    fun `switchSelectFile - if there are no selected files then can not delete`() = runTest{
        callAfterLoading {
            switchSelectFile("path")
            switchSelectFile("path")
        }
        assertFalse(state.canDelete)
    }

    @Test
    fun `switchSelectFile - if there are selected files then can delete`() = runTest{
        callAfterLoading {
            switchSelectFile("path")
        }
        assertTrue(state.canDelete)
    }

    @Test
    fun `switchSelectAll - if there are no selected files then can not delete`() = runTest {
        callAfterLoading {
            switchSelectAll()
            switchSelectAll()
        }
        assertFalse(state.canDelete)
    }

    @Test
    fun `switchSelectAll - if there are selected files then can delete`() = runTest {
        callAfterLoading {
            switchSelectAll()
        }
        assertTrue(state.canDelete)
    }

    @Test
    fun `goBack - state_isShouldGoBack equals true`() = runTest{
        fileManager().goBack()
        assertTrue(state.isShouldGoBack)
    }





    private fun TestScope.callAfterLoading(fileManager: FileManager = fileManager(), action: FileManager.() -> Unit){
        advanceUntilIdle()
        fileManager.action()
    }

    private fun List<Long>.contentEquals(other: List<Long>) : Boolean{
        return toLongArray().contentEquals(other.toLongArray())
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