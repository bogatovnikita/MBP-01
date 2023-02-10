package yin_kio.storage_info_view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.withStyledAttributes
import com.example.storage_info_view.R
import com.example.storage_info_view.databinding.LayoutStorageInfoBinding

class StorageInfoView : FrameLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs){
        initAttrs(context, attrs)
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    val binding by lazy { LayoutStorageInfoBinding.inflate(
        LayoutInflater.from(context), this, true)
    }

    // TODO enable draw shadow


    private fun initAttrs(context: Context, attrs: AttributeSet?){
        context.withStyledAttributes(
            attrs,
            R.styleable.StorageInfoView,
            0
        ) {
            val title = getString(R.styleable.StorageInfoView_title)
            val totalStorageTitle = getString(R.styleable.StorageInfoView_total_storage_title)

            binding.header.text = title
            binding.totalSpaceTitle.text = totalStorageTitle
        }

    }

}