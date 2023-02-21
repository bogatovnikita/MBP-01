package yin_kio.acceleration.presentation.selectable_acceleration.adapter

import android.content.Context
import android.util.AttributeSet
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView

class AppItemRecycler : RecyclerView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )



    fun updateItem(packageName: String){
        children.forEach {
            val viewHolder = getChildViewHolder(it) as AppItemHolder
            val viewModel = viewHolder.viewModel
            if (viewModel.flow.value.packageName == packageName){
                viewModel.update(packageName)
            }
        }
    }
}