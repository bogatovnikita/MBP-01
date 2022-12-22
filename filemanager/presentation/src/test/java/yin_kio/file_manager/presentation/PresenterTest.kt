package yin_kio.file_manager.presentation

import android.content.Context
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.BeforeClass
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import yin_kio.file_manager.domain.models.SortingMode

@RunWith(RobolectricTestRunner::class)
class PresenterTest {

    private val presenter: Presenter
    private val context: Context

    init {
        context = RuntimeEnvironment.getApplication()
        presenter = Presenter(context)
    }

    @Test
    fun `present FromBigToSmall get string from big to small`(){
        val actual = presenter.present(SortingMode.FromBigToSmall)
        assertEquals(context.getString(R.string.from_big_to_small), actual)
    }

    @Test
    fun `present FromSmallToBig get string fram small to big`(){
        val actual = presenter.present(SortingMode.FromSmallToBig)
        assertEquals(context.getString(R.string.from_small_to_big), actual)
    }

    @Test
    fun `present FromOldToNew get string from old to new`(){
        val actual = presenter.present(SortingMode.FromOldToNew)
        assertEquals(context.getString(R.string.from_old_to_new), actual)
    }

    @Test
    fun `present FromNewToOld get string from new to old`(){
        val actual = presenter.present(SortingMode.FromNewToOld)
        assertEquals(context.getString(R.string.from_new_to_old), actual)
    }

//    @Test
//    fun `prsent FromOldToNew get string from_old_to_new`(){
//
//    }

}