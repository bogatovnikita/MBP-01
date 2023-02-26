package com.entertainment.event.ssearch.presentation.ui.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.entertainment.event.ssearch.domain.models.ChildFun
import com.entertainment.event.ssearch.domain.models.DeviceFunction
import com.entertainment.event.ssearch.domain.models.ParentFun
import com.entertainment.event.ssearch.domain.utility.CHILD
import com.entertainment.event.ssearch.domain.utility.PARENT
import com.entertainment.event.ssearch.presentation.R
import com.entertainment.event.ssearch.presentation.databinding.ItemChildDeviceInfoBinding
import com.entertainment.event.ssearch.presentation.databinding.ItemParentDeviceInfoBinding

typealias OnParentFunClick = (parentFun: ParentFun) -> Unit

class FunctionRecycleAdapter(private val onParentFunClick: OnParentFunClick) :
    ListAdapter<DeviceFunction, RecyclerView.ViewHolder>(
        FunctionDiffUtilCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == PARENT) {
            val binding = ItemParentDeviceInfoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            ParentFunVH(binding, onParentFunClick)
        } else {
            val binding = ItemChildDeviceInfoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            ChildFunVH(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (item.type) {
            PARENT -> (holder as ParentFunVH).bind(item as ParentFun)
            CHILD -> (holder as ChildFunVH).bind(item as ChildFun)
        }
    }

    override fun getItemViewType(position: Int): Int = getItem(position).type

    inner class ParentFunVH(
        private val binding: ItemParentDeviceInfoBinding,
        private val onParentFunClick: OnParentFunClick
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(parentFun: ParentFun) {
            with(binding) {
                tvFunGroup.setText(parentFun.name)
                btnOpenInfo.setImageDrawable(getExpandedPrCollapsedDrawable(parentFun.isExpanded))
                parentContainer.setOnClickListener {
                    onParentFunClick(parentFun)
                }
            }
        }

        private fun getExpandedPrCollapsedDrawable(isExpanded: Boolean): Drawable? {
            val id = if (isExpanded) R.drawable.ic_expanded_list else R.drawable.ic_collapsed_list
            return AppCompatResources.getDrawable(binding.root.context, id)
        }

    }

    inner class ChildFunVH(
        private val binding: ItemChildDeviceInfoBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(parentFun: ChildFun) {
            with(binding) {
                tvFunName.setText(parentFun.name)
                tvFunBody.text = parentFun.body
            }
        }

    }

    class FunctionDiffUtilCallback : DiffUtil.ItemCallback<DeviceFunction>() {
        override fun areItemsTheSame(
            oldItem: DeviceFunction,
            newItem: DeviceFunction
        ) = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: DeviceFunction,
            newItem: DeviceFunction
        ) =
            (oldItem.id == newItem.id && oldItem.name == newItem.name && oldItem.type == newItem.type)
    }
}
