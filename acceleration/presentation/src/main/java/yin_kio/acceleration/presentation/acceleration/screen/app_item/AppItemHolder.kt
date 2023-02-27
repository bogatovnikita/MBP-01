package yin_kio.acceleration.presentation.acceleration.screen.app_item

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import yin_kio.acceleration.domain.selectable_acceleration.entities.App
import yin_kio.acceleration.presentation.databinding.ListItemAppBinding

class AppItemHolder private constructor(
    private val binding: ListItemAppBinding,
) : RecyclerView.ViewHolder(binding.root) {



    fun bind(app: App){
        binding.icon.setImageDrawable(app.icon as Drawable?)
        binding.name.text = app.name
    }

    companion object{
        fun from(parent: ViewGroup) : AppItemHolder{
            val inflater = LayoutInflater.from(parent.context)
            val binding = ListItemAppBinding.inflate(inflater, parent, false)
            return AppItemHolder(
                binding = binding
            )
        }
    }

}