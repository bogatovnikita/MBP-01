package com.battery_saving.presentation.ui.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hedgehog.battery_saving.presentation.databinding.ItemBatterySavingScanBinding

class BatteryScanAdapter(private val actions: List<String>) :
    RecyclerView.Adapter<BatteryScanAdapter.BatteryScanVH>() {

    private val items = arrayListOf<String>()

    init {
        items.addAll(actions)
    }

    class BatteryScanVH(private val binding: ItemBatterySavingScanBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setText(text: String, position: Int) {
            val alpha = 1.0.toFloat() - (position.toFloat()) * 0.5.toFloat()
            binding.tvAction.apply {
                this.alpha = alpha
                this.text = text
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BatteryScanVH {
        val binding =
            ItemBatterySavingScanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BatteryScanVH(binding)
    }

    override fun onBindViewHolder(holder: BatteryScanVH, position: Int) {
        holder.setText(items[position], position)
    }

    override fun getItemCount() = items.size

    fun removeFirstItem() {
        items.removeFirst()
        notifyItemRemoved(0)
        notifyItemRangeChanged(0, 4)
    }

}
