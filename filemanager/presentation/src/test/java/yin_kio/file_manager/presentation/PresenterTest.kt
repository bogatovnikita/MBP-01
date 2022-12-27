package yin_kio.file_manager.presentation

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
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
    fun `presentShowingMode list get LinearLayoutManager`(){
        assertTrue(presenter.presentShowingMode(ListShowingMode.List) is LinearLayoutManager)
    }

    @Test
    fun `presentShowingMode grid get GridLayoutManager with 2 span count`(){
        val layoutManager = presenter.presentShowingMode(ListShowingMode.Grid)
        assertTrue((layoutManager is GridLayoutManager))
        layoutManager as GridLayoutManager
        assertEquals(2, layoutManager.spanCount)
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

}