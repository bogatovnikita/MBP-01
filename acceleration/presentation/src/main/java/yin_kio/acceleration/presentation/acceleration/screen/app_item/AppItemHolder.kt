package yin_kio.acceleration.presentation.acceleration.screen.app_item

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import yin_kio.acceleration.presentation.databinding.ListItemAppBinding

class AppItemHolder private constructor(
    private val binding: ListItemAppBinding,
) : RecyclerView.ViewHolder(binding.root) {



    fun bind(packageName: String){
    }

    companion object{
        fun from(parent: ViewGroup) : AppItemHolder{
            val inflater = LayoutInflater.from(parent.context)
            val binding = ListItemAppBinding.inflate(inflater, parent, true)
            return AppItemHolder(
                binding = binding
            )
        }
    }

}