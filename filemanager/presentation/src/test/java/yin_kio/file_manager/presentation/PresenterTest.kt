package yin_kio.file_manager.presentation

import android.content.Context
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import yin_kio.file_manager.domain.models.*
import yin_kio.file_manager.presentation.models.IconShowingMode
import java.util.*

@RunWith(RobolectricTestRunner::class)
class PresenterTest {

    private val presenter: Presenter
    private val context: Context

    init {
        context = RuntimeEnvironment.getApplication()
        presenter = Presenter(context)
    }

    @Test
    fun `presentFileMode - get file mode`(){
        assertEquals(FileRequest.AllFiles, presenter.presentFileMode(FileRequest.AllFiles))
    }

    @Test
    fun `presentIsAllSelected - get Boolean`(){
        assertEquals(true, presenter.presentIsAllSelected(true))
    }


    @Test
    fun `presentSortingMode disabled get dark blue color`(){
        val expected = context.getColor(general.R.color.dark_blue)
        assertEquals(expected, presenter.presentSortingMode(SortingMode.Disabled))
    }

    @Test
    fun `presentSortingMode no disabled get green color`(){
        val expected = context.getColor(general.R.color.green)
        assertEquals(expected, presenter.presentSortingMode(SortingMode.FromSmallToBig))
    }

    @Test
    fun `presentMainButton enabled get bg_main_button_enabled`(){
        val expected = general.R.drawable.bg_main_button_enabled
        assertEquals(expected, presenter.presentMainButton(true))
    }

    @Test
    fun `presentMainButton disabled get bg_main_button_disabled`(){
        val expected = general.R.drawable.bg_main_button_disabled
        assertEquals(expected, presenter.presentMainButton(false))
    }

    @Test
    fun `presentFile get FileItem`(){
        val fileInfo = FileInfo()
        val fileItem = presenter.presentFile(fileInfo)
        assertEquals(fileItem.name, fileInfo.name)
        assertEquals(fileItem.isSelected, fileInfo.isSelected)
        assertEquals(fileItem.path, fileInfo.path)
    }

    @Test
    fun `presentIconShowingMode - icon for audio or document or unknown`(){
        val unknown = presenter.presentIconVisibility(FileGroup.Unknown)
        val audio = presenter.presentIconVisibility(FileGroup.Audio)
        val document = presenter.presentIconVisibility(FileGroup.Documents)

        assertEquals(IconShowingMode.Icon, unknown)
        assertEquals(IconShowingMode.Icon, audio)
        assertEquals(IconShowingMode.Icon, document)
    }

    @Test
    fun `presentIconShowingMode - image for video or image`(){
        val video = presenter.presentIconVisibility(FileGroup.Video)
        val images = presenter.presentIconVisibility(FileGroup.Images)

        assertEquals(IconShowingMode.Image, video)
        assertEquals(IconShowingMode.Image, images)
    }

    @Test
    fun `presentListShowingModeIcon for grid get ic_showing_mode_list`(){
        val actual = presenter.presentListShowingModeIcon(ListShowingMode.Grid)
        assertEquals(R.drawable.ic_showing_mode_list, actual)
    }

    @Test
    fun `presentListShowingModeIcon for list ic_showing_mode_grid`(){
        val actual = presenter.presentListShowingModeIcon(ListShowingMode.List)
        assertEquals(R.drawable.ic_showing_mode_grid, actual)
    }


}