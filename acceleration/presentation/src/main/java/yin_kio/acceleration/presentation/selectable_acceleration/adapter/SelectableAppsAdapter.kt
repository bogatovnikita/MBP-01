package yin_kio.acceleration.presentation.selectable_acceleration.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import yin_kio.acceleration.domain.selectable_acceleration.entities.App
import yin_kio.acceleration.domain.selectable_acceleration.ui_out.SelectableItem

class SelectableAppsAdapter(
    private val onItemUpdate: (App, SelectableItem) -> Unit,
    private val onItemClick: (App, SelectableItem) -> Unit
) : ListAdapter<App, SelectableAppHolder>(itemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectableAppHolder {
        return SelectableAppHolder.from(
            parent = parent,
            onItemClick = onItemClick,
            onItemUpdate = onItemUpdate
        )
    }

    override fun onBindViewHolder(holder: SelectableAppHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object{

        private fun itemCallback() = object : DiffUtil.ItemCallback<App>() {
            override fun areItemsTheSame(oldItem: App, newItem: App): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: App, newItem: App): Boolean {
                return oldItem == newItem
            }
        }

    }

}

