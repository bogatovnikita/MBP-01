package com.hedgehog.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hedgehog.presentation.databinding.ItemScreenTimeBinding
import com.hedgehog.presentation.models.AppScreenTimeListItems

class ScreenTimeAdapter(private val listener: Listener) :
    ListAdapter<AppScreenTimeListItems, ScreenTimeAdapter.ScreenTimeViewHolder>(ItemCallback),
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
            checkbox.tag = item
            iconIv.setImageDrawable(item.icon)
            titleTv.text = item.name
            descriptionTv.text = item.time
            if (item.isSelectedMode) {
                iconRightArrow.visibility = View.GONE
                checkbox.visibility = View.VISIBLE
                checkbox.isChecked = item.isChecked
            }
        }
    }

    override fun onClick(view: View) {
        val item = view.tag as AppScreenTimeListItems
        listener.onChooseNote(item)
    }

    object ItemCallback : DiffUtil.ItemCallback<AppScreenTimeListItems>() {
        override fun areItemsTheSame(
            oldItem: AppScreenTimeListItems,
            newItem: AppScreenTimeListItems
        ) = oldItem.name == newItem.name

        override fun areContentsTheSame(
            oldItem: AppScreenTimeListItems,
            newItem: AppScreenTimeListItems
        ) = oldItem == newItem
    }

    interface Listener {
        fun onChooseNote(item: AppScreenTimeListItems)
        fun onToggle(item: AppScreenTimeListItems)
    }

    class ScreenTimeViewHolder(val binding: ItemScreenTimeBinding) :
        RecyclerView.ViewHolder(binding.root)
}