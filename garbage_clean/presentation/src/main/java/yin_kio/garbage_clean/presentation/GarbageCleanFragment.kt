package yin_kio.garbage_clean.presentation

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import by.kirich1409.viewbindingdelegate.viewBinding
import jamycake.lifecycle_aware.lifecycleAware
import kotlinx.coroutines.flow.collect
import yin_kio.garbage_clean.data.FileSystemInfoProviderImpl
import yin_kio.garbage_clean.data.FilesImpl
import yin_kio.garbage_clean.data.OlejaAds
import yin_kio.garbage_clean.data.PermissionsImpl
import yin_kio.garbage_clean.domain.GarbageCleanFactory
import yin_kio.garbage_clean.presentation.databinding.FragmentGarbageCleanBinding

class GarbageCleanFragment : Fragment(R.layout.fragment_garbage_clean) {

    private val binding: FragmentGarbageCleanBinding by viewBinding()
    private val viewModel: ObservableScreenViewModel by lifecycleAware {
        val context = requireActivity().applicationContext
        val presenter = ScreenPresenter(
            context = context
        )
        val useCases = GarbageCleanFactory.createUseCases(
            files = FilesImpl(),
            outBoundary = presenter,
            coroutineScope = viewModelScope,
            fileSystemInfoProvider = FileSystemInfoProviderImpl(context),
            permissions = PermissionsImpl(context),
            ads = OlejaAds(context)
        )
        val vm = ScreenViewModel(useCases)

        presenter.viewModel = vm


        vm
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.state.collect{
                Log.d("!!!", "isInProgress: ${it.isInProgress}")
                Log.d("!!!", "${it.hasPermission}")

            }
        }
    }

}