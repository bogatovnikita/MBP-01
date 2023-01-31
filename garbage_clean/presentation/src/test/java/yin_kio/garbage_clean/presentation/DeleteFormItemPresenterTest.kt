package yin_kio.garbage_clean.presentation

import android.content.Context
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import yin_kio.garbage_clean.domain.entities.GarbageType

@RunWith(RobolectricTestRunner::class)
class DeleteFormItemPresenterTest {

    private val context: Context = RuntimeEnvironment.getApplication()
    private val presenter = DeleteFormItemPresenter(context)

    @Test
    fun `test presentIcon`(){
        assertEquals(R.drawable.ic_apk, presenter.presentIcon(GarbageType.Apk))
        assertEquals(R.drawable.ic_temp, presenter.presentIcon(GarbageType.Temp))
        assertEquals(R.drawable.ic_rest, presenter.presentIcon(GarbageType.RestFiles))
        assertEquals(R.drawable.ic_empty_folders, presenter.presentIcon(GarbageType.EmptyFolders))
        assertEquals(R.drawable.ic_thumb, presenter.presentIcon(GarbageType.Thumbnails))
    }

    @Test
    fun `test presentName`(){
        assertEquals(context.getString(R.string.apk_files), presenter.presentName(GarbageType.Apk))
        assertEquals(context.getString(R.string.temp_files), presenter.presentName(GarbageType.Temp))
        assertEquals(context.getString(R.string.rest_files), presenter.presentName(GarbageType.RestFiles))
        assertEquals(context.getString(R.string.miniatures), presenter.presentName(GarbageType.Thumbnails))
        assertEquals(context.getString(R.string.empty_folders), presenter.presentName(GarbageType.EmptyFolders))
    }

}