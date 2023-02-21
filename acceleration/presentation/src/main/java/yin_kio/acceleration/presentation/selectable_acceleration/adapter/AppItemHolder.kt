package yin_kio.acceleration.presentation.selectable_acceleration.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import yin_kio.acceleration.presentation.databinding.ListItemAppSelectableBinding

class AppItemHolder private constructor(
    private val binding: ListItemAppSelectableBinding,
    val viewModel: AppItemViewModel,
    coroutineScope: CoroutineScope
) : ViewHolder(binding.root) {


    init {
        coroutineScope.launch {
            viewModel.flow.collect{
                binding.icon.setImageDrawable(it.icon)
                binding.name.text = it.name
                binding.checkbox.isChecked = it.isSelected
            }
        }
    }

    fun bind(packageName: String){
        viewModel.update(packageName)
    }


    companion object{
        fun from(
            parent: ViewGroup,
            viewModel: AppItemViewModel,
            coroutineScope: CoroutineScope
        ) : AppItemHolder{
            val inflater = LayoutInflater.from(parent.context)
            val binding = ListItemAppSelectableBinding.inflate(inflater, parent, false)
            return AppItemHolder(
                binding = binding,
                viewModel = viewModel,
                coroutineScope
            )
        }

    }
}