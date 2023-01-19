package yin_kio.duplicates.presentation.adapters


import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import kotlinx.coroutines.CoroutineScope
import yin_kio.duplicates.domain.models.ImageInfo
import yin_kio.duplicates.presentation.view_models.ItemViewModel

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

    companion object{
        private fun diffCallback()  = object : DiffUtil.ItemCallback<ImageInfo>(){
            override fun areItemsTheSame(oldItem: ImageInfo, newItem: ImageInfo): Boolean {
                return oldItem.path == newItem.path
            }

            override fun areContentsTheSame(oldItem: ImageInfo, newItem: ImageInfo): Boolean {
                return oldItem == newItem
            }
        }
    }
}