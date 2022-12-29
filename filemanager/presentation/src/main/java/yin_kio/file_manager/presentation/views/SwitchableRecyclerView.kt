package yin_kio.file_manager.presentation.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recycler_adapter.SingleTypeViewHolder
import com.example.recycler_adapter.recyclerAdapter
import yin_kio.file_manager.domain.models.FileGroup
import yin_kio.file_manager.domain.models.FileInfo
import yin_kio.file_manager.domain.models.ListShowingMode
import yin_kio.file_manager.presentation.R
import yin_kio.file_manager.presentation.databinding.GridItemBinding
import yin_kio.file_manager.presentation.databinding.ListItemBinding
import yin_kio.file_manager.presentation.models.IconShowingMode


class SwitchableRecyclerView : RecyclerView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        setAdapterAndLayoutManager(ListShowingMode.List)
    }

    private var _listShowingMode: ListShowingMode = ListShowingMode.List
    private var _mutableAdapter: ListAdapter<FileInfo, *>? = null
    val mutableAdapter get() = _mutableAdapter

    private var onItemClick: (String) -> Unit = {}

    var onViewHolderCreated: (SingleTypeViewHolder<FileInfo, *>, CheckBox) -> Unit = {_,_->}

    fun setOnItemClickListener(onItemClick: (path: String) -> Unit){
        this.onItemClick = onItemClick
    }

    fun setListShowingMode(listShowingMode: ListShowingMode){
        if (_listShowingMode == listShowingMode) return
        _listShowingMode = listShowingMode
        setAdapterAndLayoutManager(listShowingMode)
    }

    private fun setAdapterAndLayoutManager(listShowingMode: ListShowingMode) {
        setLayoutManager(listShowingMode)
        setAdapter(listShowingMode)
    }

    private fun setLayoutManager(listShowingMode: ListShowingMode){
        layoutManager = when(listShowingMode){
            ListShowingMode.List -> LinearLayoutManager(context)
            ListShowingMode.Grid -> GridLayoutManager(context, 2)
        }
    }

    private fun setAdapter(listShowingMode: ListShowingMode) {
        _mutableAdapter = createAdapter(listShowingMode)
        adapter = _mutableAdapter
    }

    private fun createAdapter(listShowingMode: ListShowingMode) : ListAdapter<FileInfo, *>{
        return when (listShowingMode) {
            ListShowingMode.List -> listAdapter()
            ListShowingMode.Grid -> gridAdapter()
        }
    }

    private fun listAdapter() = recyclerAdapter<FileInfo, ListItemBinding>(
        onViewHolderCreated = { onViewHolderCreated(it, checkBox)},
        onBind = { item, _ ->
            setOnItemClick(root, checkBox) { onItemClick(item.path) }
            setImage(icon, image, item)
            setTexts(item, name, description)
            checkBox.isChecked = item.isSelected
        }
    )

    private fun gridAdapter() = recyclerAdapter<FileInfo, GridItemBinding>(
        onViewHolderCreated = { onViewHolderCreated(it, checkBox) },
        onBind = { item, _ ->
            setOnItemClick(root, checkBox) { onItemClick(item.path) }
            setImage(icon, image, item)
            setTexts(item, name, description)
            checkBox.isChecked = item.isSelected
        }
    )



    private fun setTexts(fileInfo: FileInfo, name: TextView, description: TextView){
        name.text = fileInfo.name
    }

    private fun setImage(icon: ImageView, image: ImageView, fileInfo: FileInfo){
        val iconVisibility = presentIconVisibility(fileInfo.fileGroup)
        icon.isVisible = iconVisibility == IconShowingMode.Icon
        image.isVisible = iconVisibility == IconShowingMode.Image


        if (icon.isVisible) icon.setImageDrawable(iconDrawable(fileInfo.fileGroup))
        if (image.isVisible) {
            Glide.with(context)
                .load(fileInfo.path)
                .into(image)
        }
    }

    private fun iconDrawable(fileGroup: FileGroup) : Drawable{
        return when(fileGroup){
            FileGroup.Unknown,
            FileGroup.Images,
            FileGroup.Video -> ContextCompat.getDrawable(context, R.drawable.ic_unknown)
            FileGroup.Documents -> ContextCompat.getDrawable(context, R.drawable.ic_documents)
            FileGroup.Audio -> ContextCompat.getDrawable(context, R.drawable.ic_audio)
        }!!
    }

    private fun presentIconVisibility(fileGroup: FileGroup) : IconShowingMode {
        return when(fileGroup){
            FileGroup.Unknown,
            FileGroup.Documents,
            FileGroup.Audio -> IconShowingMode.Icon
            FileGroup.Images,
            FileGroup.Video -> IconShowingMode.Image
        }
    }


    private fun setOnItemClick(vararg views: View, onItemClick: (View) -> Unit){
        views.forEach { it.setOnClickListener(onItemClick) }
    }
}