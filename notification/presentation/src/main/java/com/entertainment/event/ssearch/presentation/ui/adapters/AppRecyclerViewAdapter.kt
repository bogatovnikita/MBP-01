package com.entertainment.event.ssearch.presentation.ui.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.entertainment.event.ssearch.presentation.R
import com.entertainment.event.ssearch.presentation.databinding.ItemAppBinding
import com.entertainment.event.ssearch.presentation.models.AppUi

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
                tvCountNotifications.isVisible = setVisibleNotificationCount(app.countNotifications)
                if (app.countNotifications != 0) {
                    tvCountNotifications.text = setCountNotification(app.countNotifications)
                }
                switchDoNotDisturb.setOnClickListener {
                    switchDoNotDisturb.isChecked = switchModeOrIgnore(app, switchDoNotDisturb.isChecked)
                }
            }
        }

        private fun setVisibleNotificationCount(count: Int): Boolean = count != 0

        private fun switchModeOrIgnore(app: AppUi, isChecked: Boolean): Boolean {
            listener.switchModeDisturb(app.packageName, isChecked)
            return if (!app.hasPermission) false else isChecked
        }

        private fun setIcon(uri: String) {
            Glide.with(binding.ivIconApp)
                .load(Uri.parse(uri))
                .placeholder(R.drawable.ic_default_app)
                .error(R.drawable.ic_default_app)
                .into(binding.ivIconApp)
        }

        private fun setCountNotification(count: Int) = binding.root.context.getString(
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