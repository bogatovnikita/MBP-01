package yin_kio.duplicates.domain

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import yin_kio.duplicates.domain.gateways.Ads
import yin_kio.duplicates.domain.gateways.Files
import yin_kio.duplicates.domain.gateways.Permissions
import yin_kio.duplicates.domain.models.Destination
import yin_kio.duplicates.domain.models.ImageInfo
import yin_kio.duplicates.domain.models.MutableStateHolder
import yin_kio.duplicates.domain.use_cases.DuplicateRemover
import yin_kio.duplicates.domain.use_cases.UniteUseCase
import yin_kio.duplicates.domain.use_cases.UniteUseCaseImpl


@OptIn(ExperimentalCoroutinesApi::class)
internal class UniteUseCaseTest{

    private lateinit var state: MutableStateHolder
    private val dispatcher = StandardTestDispatcher()
    private val coroutineScope = CoroutineScope(dispatcher)
    private lateinit var files: Files
    private lateinit var permissions: Permissions
    private lateinit var useCase: UniteUseCase
    private lateinit var duplicateRemover: DuplicateRemover
    private val ads: Ads = mockk()

    @BeforeEach
    fun setup() = runTest(dispatcher){
        files = mockk()
        permissions = mockk()
        duplicateRemover = mockk()
        state = MutableStateHolder(coroutineScope, dispatcher)

        coEvery { ads.preloadAd() } returns Unit
        every { permissions.hasStoragePermissions } returns true
        useCase = createUniteUseCase()
        waitCoroutines()
    }

    @Test
    fun `unite if has group Selected`() = runTest(dispatcher){
        state.selected = mutableMapOf(0 to mutableSetOf(ImageInfo(FIRST_FILE), ImageInfo(SECOND_FILE)))

        val selectedCollection = state.selected.map { it.value }
        coEvery { duplicateRemover.invoke(selectedCollection) } returns Unit

        useCase.unite()

        assertUniteNavigation()


        coVerify { ads.preloadAd() }
        coVerify { duplicateRemover.invoke(selectedCollection) }
    }


    @Test
    fun `unite if has not selected`() = runTest(dispatcher) {
        coEvery { duplicateRemover.invoke(state.duplicatesLists.map { it.imageInfos }) } returns Unit

        useCase.unite()

        assertUniteNavigation()


        coVerify { ads.preloadAd() }
        coVerify(inverse = true) { duplicateRemover.invoke(state.duplicatesLists.map { it.imageInfos }) }
    }

    @Test
    fun `unite if has item selected`() = runTest(dispatcher){
        state.selected = mutableMapOf(0 to mutableSetOf(ImageInfo(FIRST_FILE)))

        val selectedCollection = state.selected.map { it.value }
        coEvery { duplicateRemover.invoke(selectedCollection) } returns Unit

        useCase.unite()

        assertUniteNavigation()

        coVerify { ads.preloadAd() }
        coVerify(inverse = true) { duplicateRemover.invoke(selectedCollection) }
    }

    private suspend fun createUniteUseCase(stateHolder: MutableStateHolder = state): UniteUseCaseImpl {
        coEvery { files.getImages() } returns listOf(ImageInfo(FIRST_FILE), ImageInfo(SECOND_FILE))
            .also { delay(50) }

        return UniteUseCaseImpl(
            state = stateHolder,
            ads = ads,
            coroutineScope = coroutineScope,
            coroutineContext = dispatcher,
            duplicatesRemover = duplicateRemover,
        )
    }

    private fun TestScope.assertUniteNavigation() {
        assertEquals(Destination.UniteProgress, state.destination)
        waitCoroutines()
        assertEquals(Destination.Inter, state.destination)
    }

    private fun TestScope.waitCoroutines(){
        advanceUntilIdle()
    }

    companion object{
        private const val FIRST_FILE = "a"
        private const val SECOND_FILE = "b"
    }
}