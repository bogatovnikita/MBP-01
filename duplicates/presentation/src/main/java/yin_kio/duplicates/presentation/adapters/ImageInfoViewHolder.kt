package yin_kio.duplicates.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import yin_kio.duplicates.domain.models.ImageInfo
import yin_kio.duplicates.presentation.databinding.ListItemImageBinding
import yin_kio.duplicates.presentation.view_models.ItemViewModel

class ImageInfoViewHolder private constructor(
    private val binding: ListItemImageBinding,
    private val coroutineScope: CoroutineScope,
    private val groupPosition: () -> Int,
    private val viewModel: ItemViewModel
) : RecyclerView.ViewHolder(binding.root){



    init {
        coroutineScope.launch {
            viewModel.state.collect {
                binding.checkbox.isChecked = it.isSelected
                loadImage(it.path)
            }
        }
    }

    private fun loadImage(path: String) {
        Glide.with(binding.root.context)
            .load(path)
            .into(binding.image)
    }


    fun bind(item: ImageInfo){
        viewModel.updateState(groupPosition(), item.path)

        binding.checkbox.setOnClickListener { viewModel.switchSelection(groupPosition(), item.path) }
        binding.root.setOnClickListener { viewModel.switchSelection(groupPosition(), item.path) }
    }



    companion object{
        fun from(parent: ViewGroup,
                 coroutineScope: CoroutineScope,
                 groupPosition: () -> Int,
                 viewModel: ItemViewModel
        ) : ImageInfoViewHolder {
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
