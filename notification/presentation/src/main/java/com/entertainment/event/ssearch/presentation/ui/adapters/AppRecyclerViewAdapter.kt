package com.entertainment.event.ssearch.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.entertainment.event.ssearch.presentation.databinding.ItemAppBinding
import com.entertainment.event.ssearch.presentation.ui.mappers.AppMapper
import com.entertainment.event.ssearch.presentation.ui.models.AppItem

class AppRecyclerViewAdapter(
    private val listener: OnItemAppClickListener
) : ListAdapter<AppItem, AppRecyclerViewAdapter.AppViewHolder>(AppItemDiffUtilCallback()) {

    class AppViewHolder(
        private val bindind: ItemAppBinding,
        private val listener: OnItemAppClickListener,
    ) : RecyclerView.ViewHolder(bindind.root) {

        fun bind(app: AppItem) {
            with(bindind) {
                ivIconApp.setImageDrawable(app.icon)
                tvAppName.text = app.name
                switchDoNotDisturb.isChecked = app.isSwitched
                if (app.countNotifications == AppMapper.EMPTY_LIST) {
                    tvCountNotifications.visibility = View.GONE
                } else {
                    tvCountNotifications.text = app.countNotifications
                }
                switchDoNotDisturb.setOnClickListener {
                    listener.switchModeDisturb(app.packageName, app.isSwitched)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val binding =
            ItemAppBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AppViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    interface OnItemAppClickListener {
        fun switchModeDisturb(packageName: String, isSwitched: Boolean)
    }

    class AppItemDiffUtilCallback() : DiffUtil.ItemCallback<AppItem>() {
        override fun areItemsTheSame(oldItem: AppItem, newItem: AppItem) =
            oldItem.packageName == newItem.packageName

        override fun areContentsTheSame(oldItem: AppItem, newItem: AppItem) = oldItem == newItem
    }

}