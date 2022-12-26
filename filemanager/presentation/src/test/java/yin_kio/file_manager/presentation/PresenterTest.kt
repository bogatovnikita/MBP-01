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



}