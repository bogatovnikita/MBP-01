package yin_kio.duplicates.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import yin_kio.duplicates.domain.models.ImageInfo
import yin_kio.duplicates.presentation.databinding.ListItemGroupBinding

class DuplicatesAdapter(
    private val onImageClick: (groupIndex: Int, path: String) -> Unit,
    private val coroutineScope: CoroutineScope,
    private val stateFlow: Flow<UIState>
) : ListAdapter<List<ImageInfo>, DuplicatesViewHolder>(difCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DuplicatesViewHolder {
        return DuplicatesViewHolder.from(parent, onImageClick,
            coroutineScope, stateFlow
        )
    }

    override fun onBindViewHolder(holder: DuplicatesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}



private fun difCallback() = object : DiffUtil.ItemCallback<List<ImageInfo>>(){
        override fun areItemsTheSame(oldItem: List<ImageInfo>, newItem: List<ImageInfo>): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: List<ImageInfo>,
            newItem: List<ImageInfo>
        ): Boolean {
            return oldItem == newItem
        }
}




class DuplicatesViewHolder private constructor (
    private val binding: ListItemGroupBinding,
    private val onImageClick: (groupIndex: Int, path: String) -> Unit,
    coroutineScope: CoroutineScope,
    stateFlow: Flow<UIState>
) : ViewHolder(binding.root){


    private val adapter by lazy {
        ImageInfoAdapter(
            onItemClick = {onImageClick(absoluteAdapterPosition, it)},
            coroutineScope, stateFlow
        )
    }

    init {
        binding.recycler.adapter = adapter
    }

    fun bind(item: List<ImageInfo>){
        adapter.groupPosition = absoluteAdapterPosition
        adapter.submitList(item)
    }

    companion object{
        fun from(parent: ViewGroup, onImageClick: (groupIndex: Int, path: String) -> Unit,
                 coroutineScope: CoroutineScope,
                 stateFlow: Flow<UIState>
        ) : DuplicatesViewHolder{
            val binding = ListItemGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return DuplicatesViewHolder(binding, onImageClick, coroutineScope, stateFlow)
        }
    }

}