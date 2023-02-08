package com.hedgehog.presentation.custom_view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.hedgehog.presentation.R
import kotlin.properties.Delegates

class AppStatisticsGraph(
    context: Context,
    attributesSet: AttributeSet?,
    defStyleAttr: Int,
    defStyleRes: Int
) : View(context, attributesSet, defStyleAttr, defStyleRes) {
    constructor(context: Context, attributesSet: AttributeSet?, defStyleAttr: Int) : this(
        context, attributesSet, defStyleAttr, R.style.AppStatisticsGraphStyle
    )

    constructor(context: Context, attributesSet: AttributeSet?) : this(
        context, attributesSet, 0
    )

    constructor(context: Context) : this(context, null)

    var appStaticsField: List<Int> = emptyList()
        set(value) {
            field = value
            invalidate()
        }

    private val fieldRect = RectF(0f, 0f, 0f, 0f)
    private var widthCellSize = 0f
    private var heightCellSize = 0f

    private var marginWidthSmallCell = 0f
    private var widthSmallCellSize = 0f

    private var backgroundScreenColor by Delegates.notNull<Int>()
    private var lineColor by Delegates.notNull<Int>()

    private lateinit var backgroundColorPaint: Paint
    private lateinit var brokenLinePaint: Paint
    private lateinit var straightLinePaint: Paint
    private lateinit var textPaint: Paint
    private lateinit var progressPaint: Paint

    init {
        initAttributes(attributesSet, defStyleAttr, defStyleRes)
        initPaints()
    }

    private fun initAttributes(attributesSet: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        val typedArray = context.obtainStyledAttributes(
            attributesSet,
            R.styleable.AppStatisticsGraph,
            defStyleAttr,
            defStyleRes
        )
        backgroundScreenColor =
            typedArray.getColor(
                R.styleable.AppStatisticsGraph_background_color,
                resources.getColor(R.color.dark_blue)
            )

        lineColor = typedArray.getColor(
            R.styleable.AppStatisticsGraph_brush_color,
            resources.getColor(R.color.grey_medium_dark)
        )

        typedArray.recycle()
    }

    private fun initPaints() {
        backgroundColorPaint =
            Paint(Paint.ANTI_ALIAS_FLAG).apply { color = backgroundScreenColor }

        brokenLinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = lineColor
            style = Paint.Style.STROKE
            pathEffect = DashPathEffect(floatArrayOf(10f, 10f, 10f, 10f), 0f)
        }

        straightLinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = lineColor
            style = Paint.Style.STROKE
        }

        textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            style = Paint.Style.FILL
            textSize =
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10f, resources.displayMetrics)
        }

        progressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
//            shader = LinearGradient(
//                100f,
//                100f,
//                0f,
//                0f,
//                Color.YELLOW,
//                Color.GREEN,
//                Shader.TileMode.CLAMP
//            )
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        fieldRect.left = (width * 0.05).toFloat()
        fieldRect.right = (width * 0.84).toFloat()
        fieldRect.top = (height * 0.1).toFloat()
        fieldRect.bottom = (height * 0.8).toFloat()

        widthCellSize = (width - (fieldRect.left + (width - fieldRect.right))) / 8
        heightCellSize = (height - (fieldRect.top + (height - fieldRect.bottom))) / 2

        marginWidthSmallCell = widthCellSize * 0.1f
        widthSmallCellSize = widthCellSize / 3 - marginWidthSmallCell
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBackground(canvas)
        drawHorizontalLineAndText(canvas)
        drawVerticalLineAndText(canvas)
        drawProgress(canvas)
    }

    private fun drawBackground(canvas: Canvas) {
        canvas.drawRoundRect(
            0f,
            0f,
            width.toFloat(),
            height.toFloat(),
            40f,
            40f,
            backgroundColorPaint
        )
    }

    private fun drawHorizontalLineAndText(canvas: Canvas) {
        val listTime = listOf(
            resources.getString(R.string.min_60),
            resources.getString(R.string.min_30),
            resources.getString(R.string.min_0)
        )
        for (i in 0..2) {
            val y = fieldRect.top + heightCellSize * i
            canvas.drawLine(fieldRect.left, y, fieldRect.right, y, straightLinePaint)
            canvas.drawText(
                listTime[i],
                fieldRect.right + 25,
                fieldRect.top + (heightCellSize * i) + 10,
                textPaint
            )
        }
    }

    private fun drawVerticalLineAndText(canvas: Canvas) {
        val listTime = listOf("00", "03", "06", "09", "12", "15", "18", "21", "00")
        for (i in 0..8) {
            val x = fieldRect.left + widthCellSize * i
            canvas.drawLine(x, fieldRect.top - 10f, x, fieldRect.bottom, brokenLinePaint)
            canvas.drawText(
                listTime[i],
                fieldRect.left + (widthCellSize * i) - 10f,
                fieldRect.bottom + 40,
                textPaint
            )
        }
    }

    private fun drawProgress(canvas: Canvas) {

    }
}