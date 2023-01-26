package com.entertainment.event.ssearch.presentation.ui.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.entertainment.event.ssearch.presentation.R
import com.entertainment.event.ssearch.presentation.databinding.ItemNotificationBinding
import com.entertainment.event.ssearch.presentation.models.NotificationUi
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

typealias OnNotificationClick = (NotificationUi) -> Unit

class NotificationRecyclerViewAdapter(
    private val listener: OnNotificationClick,
) :
    ListAdapter<NotificationUi, NotificationRecyclerViewAdapter.NotificationViewHolder>(
        NotificationDiffUtilCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding =
            ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = getItem(position)
        holder.bind(notification)
    }

    class NotificationViewHolder(
        private val binding: ItemNotificationBinding,
        private val listener: OnNotificationClick,
    ) :
        RecyclerView.ViewHolder(binding.root) {

        private val timeNow = Calendar.getInstance().timeInMillis

        fun bind(state: NotificationUi) {
            with(state) {
                setIcon(icon)
                binding.tvName.text = setTitle(title, name)
                binding.tvBody.text = body
                binding.tvTime.text = setTime(time)
                binding.root.setOnClickListener { listener(state) }
            }
        }

        private fun setTime(time: Long): String {
            return when (timeNow - time) {
                in 0..TimeUnit.MINUTES.toMillis(1) -> binding.root.context.getString(R.string.notification_manager_now)
                in 0..TimeUnit.HOURS.toMillis(1) -> binding.root.context.getString(
                    R.string.notification_manager_min_ago,
                    (((timeNow - time) / TimeUnit.MINUTES.toMillis(1)).toString())
                )
                in 0..TimeUnit.DAYS.toMillis(1) -> convertLongToTime(time, "HH:mm")
                else -> convertLongToTime(time, "dd.MM.yyyy")
            }
        }

        private fun convertLongToTime(time: Long, timeFormat: String): String {
            val date = Date(time)
            val format = SimpleDateFormat(timeFormat)
            return format.format(date)
        }

        private fun setIcon(uri: String) {
            Glide.with(binding.ivIcon)
                .load(Uri.parse(uri))
                .placeholder(R.drawable.ic_default_app)
                .error(R.drawable.ic_default_app)
                .into(binding.ivIcon)
        }

        private fun setTitle(title: String, name: String): String =
            if (title == "null") name else title

    }


    class NotificationDiffUtilCallback : DiffUtil.ItemCallback<NotificationUi>() {
        override fun areItemsTheSame(oldItem: NotificationUi, newItem: NotificationUi) =
            oldItem.time == newItem.time

        override fun areContentsTheSame(oldItem: NotificationUi, newItem: NotificationUi) =
            oldItem == newItem
    }
}