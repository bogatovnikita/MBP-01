package yin_kio.duplicates.presentation


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import yin_kio.duplicates.domain.models.ImageInfo
import yin_kio.duplicates.presentation.databinding.ListItemImageBinding

class ImageInfoAdapter(
    private val coroutineScope: CoroutineScope,
    private val createItemViewModel: () -> ItemViewModel,
) : ListAdapter<ImageInfo, ImageInfoViewHolder>(diffCallback()) {

    var groupPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageInfoViewHolder {
        return ImageInfoViewHolder.from(
            parent = parent,
            coroutineScope = coroutineScope,
            groupPosition = { groupPosition },
            viewModel = createItemViewModel(),
        )
    }

    override fun onBindViewHolder(holder: ImageInfoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}


private fun diffCallback()  = object : DiffUtil.ItemCallback<ImageInfo>(){
    override fun areItemsTheSame(oldItem: ImageInfo, newItem: ImageInfo): Boolean {
        return oldItem.path == newItem.path
    }

    override fun areContentsTheSame(oldItem: ImageInfo, newItem: ImageInfo): Boolean {
        return oldItem == newItem
    }
}

class ImageInfoViewHolder private constructor(
    private val binding: ListItemImageBinding,
    private val coroutineScope: CoroutineScope,
    private val groupPosition: () -> Int,
    private val viewModel: ItemViewModel
) : RecyclerView.ViewHolder(binding.root){



    init {
        coroutineScope.launch {
            viewModel.state.collect { binding.checkbox.isChecked = it.isSelected }
        }
    }



    fun bind(item: ImageInfo){
        viewModel.updateState(groupPosition(), item.path)

        binding.checkbox.setOnClickListener { viewModel.switchSelection(groupPosition(), item.path) }
        binding.root.setOnClickListener { viewModel.switchSelection(groupPosition(), item.path) }

        loadImage(item)
    }

    private fun loadImage(item: ImageInfo) {
        Glide.with(binding.root.context)
            .load(item.path)
            .into(binding.image)
    }

    companion object{
        fun from(parent: ViewGroup,
                 coroutineScope: CoroutineScope,
                 groupPosition: () -> Int,
                 viewModel: ItemViewModel
        ) : ImageInfoViewHolder{
            val binding = ListItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ImageInfoViewHolder(
                binding = binding,
                coroutineScope = coroutineScope,
                groupPosition = groupPosition,
                viewModel = viewModel,
            )
        }
    }
}
