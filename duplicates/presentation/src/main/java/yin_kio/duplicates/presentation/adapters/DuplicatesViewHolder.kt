package yin_kio.duplicates.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import yin_kio.duplicates.domain.models.DuplicatesList
import yin_kio.duplicates.presentation.databinding.ListItemGroupBinding
import yin_kio.duplicates.presentation.view_models.ImagesGroupViewModel

class DuplicatesViewHolder private constructor (
    private val binding: ListItemGroupBinding,
    private val coroutineScope: CoroutineScope,
    private val viewModel: ImagesGroupViewModel
) : RecyclerView.ViewHolder(binding.root){


    private val adapter by lazy {
        ImageInfoAdapter(
            coroutineScope = coroutineScope,
            createItemViewModel = { viewModel.createItemViewModel() }
        )
    }

    init {
        binding.recycler.adapter = adapter

        coroutineScope.launch {
            viewModel.state.collect{
                binding.checkbox.isChecked = it.isSelected
                binding.restrictionMessage.isInvisible = !it.isShowRestrictionMessage
            }
        }
    }

    fun bind(item: DuplicatesList){
        viewModel.updateState(item.id)
        adapter.groupPosition = item.id
        adapter.submitList(item.imageInfos)
        binding.select.setOnClickListener { viewModel.switchSelection(item.id) }
        binding.checkbox.setOnClickListener { viewModel.switchSelection(item.id) }
    }


    companion object{
        fun from(parent: ViewGroup,
                 coroutineScope: CoroutineScope,
                 viewModel: ImagesGroupViewModel
        ) : DuplicatesViewHolder {
            val binding = ListItemGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return DuplicatesViewHolder(
                binding = binding,
                coroutineScope = coroutineScope,
                viewModel = viewModel,
            )
        }
    }

}