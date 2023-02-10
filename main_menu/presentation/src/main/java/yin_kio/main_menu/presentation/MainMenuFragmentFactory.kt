package yin_kio.main_menu.presentation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import yin_kio.file_manager.di.FileManagerFragmentFactory
import yin_kio_duplicates.di.DuplicatesFragmentFactory

class MainMenuFragmentFactory : FragmentFactory() {

    private val factories = arrayOf(
        FileManagerFragmentFactory(),
        DuplicatesFragmentFactory()
    )

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return recursively(classLoader, className)
    }

    private fun recursively(classLoader: ClassLoader, className: String) : Fragment{
        factories.forEach {
            val fragment = try {
                it.instantiate(classLoader, className)
            } catch (ex: Exception){
                null
            }
            if (fragment != null) return fragment
        }
        throw Exception("fragment not found: $className")
    }
}