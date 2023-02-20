package yin_kio.acceleration.presentation.acceleration.screen.app_item

import android.app.Application
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import kotlinx.coroutines.CoroutineScope

class AppsAdapter(
    private val coroutineScope: CoroutineScope,
    private val application: Application
) : ListAdapter<String, AppItemHolder>(itemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppItemHolder {
        val appInfoProvider = AppInfoProvider(application)
        val viewModel = AppItemViewModel(
            appInfoProvider = appInfoProvider,
            coroutineScope = coroutineScope
        )

        return AppItemHolder.from(
            parent = parent,
            viewModel = viewModel,
            coroutineScope = coroutineScope
        )
    }

    override fun onBindViewHolder(holder: AppItemHolder, position: Int) {
        holder.bind(getItem(position))
    }


    companion object{
        private fun itemCallback() = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        }
    }
}

