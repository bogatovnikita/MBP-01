package com.hedgehog.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hedgehog.presentation.databinding.ItemScreenTimeBinding
import com.hedgehog.presentation.models.AppScreenTime

class ScreenTimeAdapter(private val listener: Listener) :
    ListAdapter<AppScreenTime, ScreenTimeAdapter.ScreenTimeViewHolder>(ItemCallback),
    View.OnClickListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScreenTimeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemScreenTimeBinding.inflate(inflater, parent, false)
        binding.root.setOnClickListener(this)
        return ScreenTimeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScreenTimeViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            root.tag = item
            iconIv.setImageResource(item.icon)
            titleTv.text = item.name
            descriptionTv.text = item.time
        }
    }

    override fun onClick(view: View) {
        val item = view.tag as AppScreenTime
        listener.onChooseNote(item)
    }

    object ItemCallback : DiffUtil.ItemCallback<AppScreenTime>() {
        override fun areItemsTheSame(oldItem: AppScreenTime, newItem: AppScreenTime) =
            oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: AppScreenTime, newItem: AppScreenTime) =
            oldItem == newItem
    }

    interface Listener {
        fun onChooseNote(item: AppScreenTime)
    }

    class ScreenTimeViewHolder(val binding: ItemScreenTimeBinding) :
        RecyclerView.ViewHolder(binding.root)
}