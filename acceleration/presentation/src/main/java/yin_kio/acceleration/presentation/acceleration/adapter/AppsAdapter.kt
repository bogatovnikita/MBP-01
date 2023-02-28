package yin_kio.acceleration.presentation.acceleration.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import yin_kio.acceleration.domain.selectable_acceleration.entities.App

class AppsAdapter : ListAdapter<App, AppItemHolder>(itemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppItemHolder {
        return AppItemHolder.from(parent)
    }

    override fun onBindViewHolder(holder: AppItemHolder, position: Int) {
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

