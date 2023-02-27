package yin_kio.acceleration.presentation.selectable_acceleration.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import yin_kio.acceleration.domain.selectable_acceleration.entities.App
import yin_kio.acceleration.domain.selectable_acceleration.ui_out.SelectableItem
import yin_kio.acceleration.presentation.databinding.ListItemAppSelectableBinding

class SelectableAppHolder private constructor(
    private val binding: ListItemAppSelectableBinding,
    private val onItemUpdate: (App, SelectableItem) -> Unit,
    private val onItemClick: (App, SelectableItem) -> Unit
) : ViewHolder(binding.root) {


    fun bind(app: App){
        val selectableItem = selectableItem()
        binding.apply {
            icon.setImageDrawable(app.icon as Drawable?)
            name.text = app.name

            root.setOnClickListener { onItemClick(app, selectableItem) }
            checkbox.setOnClickListener { onItemClick(app, selectableItem) }
        }

        onItemUpdate(app, selectableItem)
    }

    private fun selectableItem() = object : SelectableItem {
        override fun setSelected(isSelected: Boolean) {
            binding.checkbox.isChecked = isSelected
        }
    }


    companion object{
        fun from(
            parent: ViewGroup,
            onItemUpdate: (App, SelectableItem) -> Unit,
            onItemClick: (App, SelectableItem) -> Unit
        ) : SelectableAppHolder{
            val inflater = LayoutInflater.from(parent.context)
            val binding = ListItemAppSelectableBinding.inflate(inflater, parent, false)
            return SelectableAppHolder(
                binding = binding,
                onItemUpdate = onItemUpdate,
                onItemClick = onItemClick
            )
        }

    }
}