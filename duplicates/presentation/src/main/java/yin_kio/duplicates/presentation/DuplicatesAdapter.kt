package yin_kio.duplicates.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import yin_kio.duplicates.domain.models.DuplicatesList
import yin_kio.duplicates.presentation.databinding.ListItemGroupBinding

class DuplicatesAdapter(
    private val coroutineScope: CoroutineScope,
    private val createGroupViewModel: () -> GroupViewModel
) : ListAdapter<DuplicatesList, DuplicatesViewHolder>(difCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DuplicatesViewHolder {
        return DuplicatesViewHolder.from(
            parent = parent,
            coroutineScope = coroutineScope,
            viewModel = createGroupViewModel(),
        )
    }

    override fun onBindViewHolder(holder: DuplicatesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}



private fun difCallback() = object : DiffUtil.ItemCallback<DuplicatesList>(){
        override fun areItemsTheSame(oldItem: DuplicatesList, newItem: DuplicatesList): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: DuplicatesList,
            newItem: DuplicatesList
        ): Boolean {
            return oldItem == newItem
        }
}




class DuplicatesViewHolder private constructor (
    private val binding: ListItemGroupBinding,
    private val coroutineScope: CoroutineScope,
    private val viewModel: GroupViewModel
) : ViewHolder(binding.root){


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
                binding.checkbox.isChecked = it
            }
        }
    }

    fun bind(item: DuplicatesList){
        viewModel.updateState(item.id)
        adapter.groupPosition = item.id
        adapter.submitList(item.data)
        binding.select.setOnClickListener { viewModel.switchSelection(item.id) }
        binding.checkbox.setOnClickListener { viewModel.switchSelection(item.id) }
    }

    companion object{
        fun from(parent: ViewGroup,
                 coroutineScope: CoroutineScope,
                 viewModel: GroupViewModel
        ) : DuplicatesViewHolder{
            val binding = ListItemGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return DuplicatesViewHolder(
                binding = binding,
                coroutineScope = coroutineScope,
                viewModel = viewModel,
            )
        }
    }

}