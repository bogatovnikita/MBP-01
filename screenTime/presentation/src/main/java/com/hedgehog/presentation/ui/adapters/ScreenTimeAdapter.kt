package com.hedgehog.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hedgehog.presentation.R
import com.hedgehog.presentation.databinding.ItemScreenTimeBinding
import com.hedgehog.presentation.models.AppScreenTime

class ScreenTimeAdapter(private val listener: Listener, private val isSelectedMode: Boolean) :
    ListAdapter<AppScreenTime, ScreenTimeAdapter.ScreenTimeViewHolder>(ItemCallback),
    View.OnClickListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScreenTimeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemScreenTimeBinding.inflate(inflater, parent, false)
        binding.root.setOnClickListener(this)
        binding.checkbox.setOnClickListener(this)
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
            if (isSelectedMode) {
                iconRightArrow.visibility = View.GONE
                checkbox.visibility = View.VISIBLE
                if (item.isItSystemApp) {
                    checkbox.buttonDrawable = ContextCompat.getDrawable(
                        holder.binding.root.context,
                        R.drawable.ic_check_box_system_app
                    )
                }
            }
            checkbox.isChecked = item.isChecked
        }
    }

    override fun onClick(view: View) {
        val item = view.tag as AppScreenTime
        when (view.id) {
            R.id.checkbox -> {
                listener.onToggle(item)
            }
            else -> listener.onChooseNote(item)
        }
    }

    object ItemCallback : DiffUtil.ItemCallback<AppScreenTime>() {
        override fun areItemsTheSame(
            oldItem: AppScreenTime,
            newItem: AppScreenTime
        ) = oldItem.packageName == newItem.packageName

        override fun areContentsTheSame(
            oldItem: AppScreenTime,
            newItem: AppScreenTime
        ) = oldItem == newItem
    }

    interface Listener {
        fun onChooseNote(item: AppScreenTime)
        fun onToggle(item: AppScreenTime)
    }

    class ScreenTimeViewHolder(val binding: ItemScreenTimeBinding) :
        RecyclerView.ViewHolder(binding.root)
}