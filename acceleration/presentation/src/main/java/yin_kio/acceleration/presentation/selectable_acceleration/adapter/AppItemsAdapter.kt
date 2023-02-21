package yin_kio.acceleration.presentation.selectable_acceleration.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import kotlinx.coroutines.CoroutineScope
import yin_kio.acceleration.domain.selectable_acceleration.ui_out.AppsFormState
import yin_kio.acceleration.presentation.AppInfoProvider

class AppItemsAdapter(
    private val coroutineScope: CoroutineScope,
    private val appsFormState: AppsFormState
) : ListAdapter<String, AppItemHolder>(itemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppItemHolder {
        val viewModel = AppItemViewModel(
            appInfoProvider = AppInfoProvider(parent.context),
            coroutineScope= coroutineScope,
            appsFormState = appsFormState,
        )

        return AppItemHolder.from(
            parent = parent,
            viewModel = viewModel,
            coroutineScope = coroutineScope,
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

