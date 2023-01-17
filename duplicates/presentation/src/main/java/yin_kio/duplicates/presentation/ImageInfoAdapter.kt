package yin_kio.duplicates.presentation


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import yin_kio.duplicates.domain.models.ImageInfo
import yin_kio.duplicates.presentation.databinding.ListItemImageBinding

class ImageInfoAdapter(
    private val onItemClick: (path: String) -> Unit,
    private val coroutineScope: CoroutineScope,
    private val stateFlow: Flow<UIState>
) : ListAdapter<ImageInfo, ImageInfoViewHolder>(diffCallback()) {

    var groupPosition: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageInfoViewHolder {
        return ImageInfoViewHolder.from(parent, onItemClick, coroutineScope, stateFlow, groupPosition)
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
    private val onItemClick: (path: String) -> Unit,
    private val coroutineScope: CoroutineScope,
    private val stateFlow: Flow<UIState>,
    private val groupPosition: Int
) : RecyclerView.ViewHolder(binding.root){


    private var path: String? = null

    init {
        observeItemSelection()
        binding.checkbox.setOnClickListener { clickOnItem() }
        binding.root.setOnClickListener { clickOnItem() }
    }

    private fun observeItemSelection() {
        coroutineScope.launch {
            stateFlow.collect { state ->
                binding.checkbox.isChecked = state.isImageSelected()
            }
        }
    }

    private fun UIState.isImageSelected(
    ): Boolean {
        if (path == null) return false
        return selected[groupPosition]?.contains(ImageInfo(path!!)) ?: false
    }

    private fun clickOnItem() {
        path?.let { onItemClick(it) }
    }

    fun bind(item: ImageInfo){
        path = item.path
        loadImage(item)
    }

    private fun loadImage(item: ImageInfo) {
        Glide.with(binding.root.context)
            .load(item.path)
            .into(binding.image)
    }

    companion object{
        fun from(parent: ViewGroup, onItemClick: (path: String) -> Unit,
                 coroutineScope: CoroutineScope,
                 stateFlow: Flow<UIState>,
                 groupPosition: Int
        ) : ImageInfoViewHolder{
            val binding = ListItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ImageInfoViewHolder(binding, onItemClick, coroutineScope, stateFlow, groupPosition)
        }
    }
}
