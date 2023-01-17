package yin_kio.duplicates.presentation


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import yin_kio.duplicates.domain.models.ImageInfo
import yin_kio.duplicates.presentation.databinding.ListItemImageBinding

class ImageInfoAdapter : ListAdapter<ImageInfo, ImageInfoViewHolder>(diffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageInfoViewHolder {
        return ImageInfoViewHolder.from(parent)
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
    private val binding: ListItemImageBinding
) : RecyclerView.ViewHolder(binding.root){


    fun bind(item: ImageInfo){
        Glide.with(binding.root.context)
            .load(item.path)
            .into(binding.image)
    }

    companion object{
        fun from(parent: ViewGroup) : ImageInfoViewHolder{
            val binding = ListItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ImageInfoViewHolder(binding)
        }
    }
}
