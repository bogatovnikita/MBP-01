package yin_kio.duplicates.presentation.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import kotlinx.coroutines.CoroutineScope
import yin_kio.duplicates.domain.models.DuplicatesList
import yin_kio.duplicates.presentation.view_models.GroupViewModel

class DuplicatesAdapter(
    private val coroutineScope: CoroutineScope,
    private val createGroupViewModel: () -> GroupViewModel
) : ListAdapter<DuplicatesList, DuplicatesViewHolder>(difCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DuplicatesViewHolder {
        return DuplicatesViewHolder.from(
            parent = parent,
            coroutineScope = coroutineScope,
            viewModel = createGroupViewModel(),
        )
    }

    override fun onBindViewHolder(holder: DuplicatesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private fun difCallback() = object : DiffUtil.ItemCallback<DuplicatesList>(){
            override fun areItemsTheSame(oldItem: DuplicatesList, newItem: DuplicatesList): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: DuplicatesList,
                newItem: DuplicatesList
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

}