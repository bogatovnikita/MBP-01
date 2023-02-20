package yin_kio.acceleration.presentation.acceleration.screen.app_item

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import yin_kio.acceleration.presentation.databinding.ListItemAppBinding

class AppItemHolder private constructor(
    private val binding: ListItemAppBinding,
    private val viewModel: AppItemViewModel,
    coroutineScope: CoroutineScope
) : RecyclerView.ViewHolder(binding.root) {


    init {
        coroutineScope.launch {
            viewModel.flow.collect{
                binding.icon.setImageDrawable(it.icon)
                binding.name.text = it.name
            }
        }
    }

    fun bind(packageName: String){
        viewModel.update(packageName)
    }

    companion object{
        fun from(parent: ViewGroup,
                 viewModel: AppItemViewModel,
                 coroutineScope: CoroutineScope
        ) : AppItemHolder{
            val inflater = LayoutInflater.from(parent.context)
            val binding = ListItemAppBinding.inflate(inflater, parent, false)
            return AppItemHolder(
                binding = binding,
                viewModel = viewModel,
                coroutineScope = coroutineScope
            )
        }
    }

}