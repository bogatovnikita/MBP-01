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
import yin_kio.file_manager.domain.gateways.Ads
import yin_kio.file_manager.domain.gateways.Files
import yin_kio.file_manager.domain.gateways.PermissionChecker
import yin_kio.file_manager.domain.models.*

@OptIn(ExperimentalCoroutinesApi::class)
internal class FileManagerTest{

    private lateinit var stateHolder: MutableStateHolder
    private val state get() = stateHolder.value
    private lateinit var files: Files
    private lateinit var ads: Ads


    @BeforeEach
    fun setup(){
        stateHolder = MutableStateHolder()
        files = mockk()
        ads = mockk()
        for (value in FileRequest.values()) {
            coEvery { files.getFiles(value) } returns fileInfos().also { runTest { delay(50) } }
        }
        coEvery { files.delete(listOf("path")) } returns Unit
        coEvery { files.delete(listOf()) } returns Unit
        coEvery { ads.preload() } returns Unit
    }


    @Test
    fun `updateFiles - if has not permission then state does not contains files and is not in progress`() = runTest{
        assertTrue(state.inProgress)
        fileManager(false).updateFiles()
        assertTrue(state.inProgress)
        wait()
        assertTrue(state.files.isEmpty())
        assertFalse(state.inProgress)
    }

    @Test
    fun `updateFiles - if has permission then state contains files and is not in progress after progress`() = runTest{
        fileManager(true).updateFiles()
        assertTrue(state.inProgress)
        wait()
        assertFalse(state.inProgress)
        assertEquals(fileInfos(), state.files)
    }

    @Test
    fun `updateFiles after selectAll - isAllSelected is false`() = runTest{
        callAfterLoading {
            switchSelectAll()
            updateFiles()
            wait()
        }
        assertFalse(state.isAllSelected)
        assertTrue(state.selectedFiles.isEmpty())
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
    fun `switchSortingMode test`() = runTest{
        callAfterLoading {
            assertSortingModeSwitching()


            assertSorted(SortingMode.FromOldToNew, listOf(1L,2L,3L)) { it.time }
            assertSorted(SortingMode.FromNewToOld, listOf(3L,2L,1L)) { it.time }
            assertSorted(SortingMode.FromSmallToBig, listOf(1L,2L,3L)) { it.size }
            assertSorted(SortingMode.FromBigToSmall, listOf(3L,2L,1L)) { it.size }

        }
    }

    private fun FileManager.assertSorted(sortingMode: SortingMode, expected: List<Long>, sortBy: (FileInfo) -> Long){
        switchSortingMode(sortingMode)
        val actual = state.files.map(sortBy)
        assertTrue(expected.contentEquals(actual), "expected: $expected, actual: $actual")
    }

    private fun FileManagerImpl.assertSortingModeSwitching() {
        SortingMode.values().forEach {
            switchSortingMode(it)
            assertEquals(it, state.sortingMode)
        }
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
        assertEquals(ListShowingMode.Grid, state.listShowingMode)
    }

    @Test
    fun `switchShowingMode twice - state contains list showing mode`() = runTest{
        fileManager().apply {
            switchShowingMode()
            switchShowingMode()
        }
        assertEquals(ListShowingMode.List, state.listShowingMode)
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

    @Test
    fun `test delete`() = runTest{
        callAfterLoading {
            switchSelectFile("path")
            delete()
        }
        assertEquals(DeleteState.Progress, state.deleteState)
        assertFalse(state.isShowInter)
        coVerify { ads.preload() }
        wait()
        coVerify { files.delete(listOf("path")) }
        assertEquals(DeleteState.Done, state.deleteState)
        assertTrue(state.isShowInter)
    }

    @Test
    fun `askDelete - delete state is Ask`() = runTest{
        fileManager().askDelete()
        assertEquals(DeleteState.Ask, state.deleteState)
    }

    @Test
    fun `cancelDelete - delete state is Wait if not in progress`() = runTest {
        callAfterLoading() {
            askDelete()
            cancelDelete()

            assertEquals(DeleteState.Wait, state.deleteState)

            delete()
            cancelDelete()

            assertEquals(DeleteState.Progress, state.deleteState)
        }
    }



    @Test
    fun `hideInter - isShowInter is false`() = runTest {
        fileManager().hideInter()
        assertFalse(state.isShowInter)
    }

    @Test
    fun `completeDelete - delete state is Wait and files are updated`() = runTest{
        val oldFiles = state.files
        fileManager().completeDelete()
        wait()
        assertEquals(DeleteState.Wait, state.deleteState)
        assertTrue(oldFiles !== state.files)
    }

    @Test
    fun `showSortingModeSelector - isShowSortingModePopup is true`() = runTest{
        fileManager().showSortingModeSelector()
        assertTrue(state.isShowSortingModeSelector)
    }

    @Test
    fun `switchSortingMode - isShowSortingModePopup is false`() = runTest {
        fileManager().apply {
            showSortingModeSelector()
            switchSortingMode(SortingMode.FromNewToOld)
        }
        assertFalse(state.isShowSortingModeSelector)
    }

    @Test
    fun `init - sorting mode is disabled`() = runTest {
        fileManager()
        assertEquals(SortingMode.Disabled, state.sortingMode)
    }

    @Test
    fun `updateFiles - file are sorted according to sorting mode`() = runTest {
        callAfterLoading {
            switchSortingMode(SortingMode.FromSmallToBig)
            updateFiles()
            wait()
        }
        val sizes = listOf(1L, 2L, 3L)
        assertTrue(sizes.contentEquals(state.files.map { it.size }))
    }

    @Test
    fun `updateFiles after select - can not delete and no selected files`() = runTest {
        callAfterLoading {
            switchSelectFile("path")
            updateFiles()
            wait()
        }
        assertFalse(state.canDelete)
        assertTrue(state.selectedFiles.isEmpty())
    }

    private fun TestScope.wait() {
        advanceUntilIdle()
    }

    @Test
    fun `hideSortingModeSelector - isSHowSortingModeSelector is false`() = runTest{
        fileManager().hideSortingModeSelector()
        assertFalse(state.isShowSortingModeSelector)
    }





    private fun TestScope.callAfterLoading(fileManager: FileManagerImpl = fileManager(), action: FileManagerImpl.() -> Unit){
        wait()
        fileManager.action()
    }

    private fun List<Long>.contentEquals(other: List<Long>) : Boolean{
        return toLongArray().contentEquals(other.toLongArray())
    }




    private fun TestScope.assertFileModeSwitching(fileManager: FileManagerImpl){
        FileRequest.values().forEach {
            fileManager.switchFileMode(it)
            wait()
            assertEquals(state.fileRequest, it)
        }
    }

    private fun TestScope.assertGetFilesCalls(fileManager: FileManagerImpl){
        for (value in FileRequest.values()) {
            fileManager.apply {
                switchFileMode(value)
            }
            wait()
            coVerify { files.getFiles(value) }
        }

    }



    private fun TestScope.fileManager(hasPermission: Boolean = true) : FileManagerImpl{
        return FileManagerImpl(
            stateHolder, permissionChecker(hasPermission), files,
            coroutineScope = this,
            coroutineContext = coroutineContext,
            ads = ads
        )
    }

    private fun permissionChecker(returned: Boolean): PermissionChecker {
        val checker = mockk<PermissionChecker>()
        coEvery { checker.hasPermission } returns returned
        return checker
    }

}