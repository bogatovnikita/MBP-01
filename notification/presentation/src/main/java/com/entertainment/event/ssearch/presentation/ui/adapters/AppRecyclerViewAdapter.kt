package com.entertainment.event.ssearch.presentation.ui.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.entertainment.event.ssearch.presentation.R
import com.entertainment.event.ssearch.presentation.databinding.ItemAppBinding
import com.entertainment.event.ssearch.presentation.ui.models.AppUi

class AppRecyclerViewAdapter(
    private val listener: OnItemAppClickListener
) : ListAdapter<AppUi, AppRecyclerViewAdapter.AppViewHolder>(AppItemDiffUtilCallback()) {

    class AppViewHolder(
        private val binding: ItemAppBinding,
        private val listener: OnItemAppClickListener,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(app: AppUi) {
            with(binding) {
                setIcon(app.icon)
                tvAppName.text = app.name
                switchDoNotDisturb.isChecked = app.isSwitched
                if (app.countNotifications == 0) {
                    tvCountNotifications.visibility = View.GONE
                } else {
                    tvCountNotifications.text = getString(app.countNotifications)
                }
                switchDoNotDisturb.setOnClickListener {
                    listener.switchModeDisturb(app.packageName, switchDoNotDisturb.isChecked)
                }
            }
        }

        private fun setIcon(uri: String) {
            Glide.with(binding.ivIconApp)
                .load(Uri.parse(uri))
                .placeholder(R.drawable.ic_watch)
                .error(R.drawable.ic_moon)
                .into(binding.ivIconApp)

        }

        private fun getString(count: Int) = binding.root.context.getString(
            R.string.notification_maneger_count_notifications,
            count.toString()
        )


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

    class AppItemDiffUtilCallback() : DiffUtil.ItemCallback<AppUi>() {
        override fun areItemsTheSame(oldItem: AppUi, newItem: AppUi) =
            oldItem.packageName == newItem.packageName

        override fun areContentsTheSame(oldItem: AppUi, newItem: AppUi) = oldItem == newItem
    }

}