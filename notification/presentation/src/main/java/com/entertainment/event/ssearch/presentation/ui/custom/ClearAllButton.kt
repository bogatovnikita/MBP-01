package com.entertainment.event.ssearch.presentation.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.entertainment.event.ssearch.presentation.databinding.ButtonClearAllBinding

class ClearAllButton @JvmOverloads constructor(
    context: Context,
    private val attr: AttributeSet? = null,
    private val defStyleAttr: Int = 0
) : LinearLayout(context, attr, defStyleAttr) {

    private var isExpanded = false

    private val _binding: ButtonClearAllBinding =
        ButtonClearAllBinding.inflate(LayoutInflater.from(context), this)

    init {
        initListener()
    }

    private fun initListener() {
    }

    private fun setVisibility() {
        _binding.tvClearAll.visibility = if (isExpanded) VISIBLE else GONE
    }

    fun setCanDeleteListener(block: (isCanDelete: Boolean) -> Unit) {
        _binding.root.setOnClickListener {
            block(isExpanded)
            isExpanded = !isExpanded
            setVisibility()
        }
        _binding.imageButton.setOnClickListener {
            block(isExpanded)
            isExpanded = !isExpanded
            setVisibility()
        }
        _binding.tvClearAll.setOnClickListener {
            block(isExpanded)
            isExpanded = !isExpanded
            setVisibility()
        }
    }

    fun hideButton() {
        isExpanded = false
        setVisibility()
    }
}