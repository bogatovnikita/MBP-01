package com.hedgehog.presentation.custom_view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.hedgehog.presentation.R
import java.text.SimpleDateFormat
import java.util.*
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

    var progressesHeights: List<Int> = emptyList()
        set(value) {
            field = value
            invalidate()
        }

    private var typeWeek = false

    private val chartRect = RectF(0f, 0f, 0f, 0f)
    private var cellWidth = 0f
    private var cellHeight = 0f

    private var marginWidthSmallCell = 0f
    private var widthSmallCellSize = 0f

    private var backgroundScreenColor by Delegates.notNull<Int>()
    private var lineColor by Delegates.notNull<Int>()
    private val verticalLinesTextsForHours =
        listOf("00", "03", "06", "09", "12", "15", "18", "21", "00")

    private val verticalLinesTextsForWeek = mutableListOf<String>()

    private val horizontalLinesTextsForHour = listOf(
        resources.getString(R.string.min_60),
        resources.getString(R.string.min_30),
        resources.getString(R.string.min_0)
    )

    private val horizontalLinesTextsForWeek = listOf(
        resources.getString(R.string.min_90),
        resources.getString(R.string.min_60),
        resources.getString(R.string.min_30),
        resources.getString(R.string.min_0)
    )

    private lateinit var backgroundColorPaint: Paint
    private lateinit var brokenLinePaint: Paint
    private lateinit var straightLinePaint: Paint
    private lateinit var textPaint: Paint
    private val progressPaint = progressPaint()

    init {
        initAttributes(attributesSet, defStyleAttr, defStyleRes)
        initPaints()
        checkDays()
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

        typeWeek = typedArray.getBoolean(R.styleable.AppStatisticsGraph_day_type, false)

        typedArray.recycle()
    }

    private fun initPaints() {
        backgroundColorPaint = bgColorPaint()
        brokenLinePaint = brokenLinePaint()
        straightLinePaint = straightLinePaint()
        textPaint = textPaint()
    }

    private fun progressPaint(): Paint {
        return Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            color = Color.CYAN
        }
    }

    private fun textPaint(): Paint {
        return Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            style = Paint.Style.FILL
            textSize = 10.sp
        }
    }

    private fun straightLinePaint(): Paint {
        return Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = lineColor
            style = Paint.Style.STROKE
        }
    }

    private fun brokenLinePaint(): Paint {
        return Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = lineColor
            style = Paint.Style.STROKE
            pathEffect = DashPathEffect(floatArrayOf(10f, 10f, 10f, 10f), 0f)
        }
    }

    private fun bgColorPaint(): Paint {
        return Paint(Paint.ANTI_ALIAS_FLAG).apply { color = backgroundScreenColor }
    }

    private val Int.sp: Float
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            this.toFloat(),
            resources.displayMetrics
        )

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        chartRect.left = (width * 0.05).toFloat()
        chartRect.right = (width * 0.84).toFloat()
        chartRect.top = (height * 0.1).toFloat()
        chartRect.bottom = (height * 0.8).toFloat()

        if (typeWeek) {
            cellWidth = (width - (chartRect.left + (width - chartRect.right))) / 9
            cellHeight = (height - (chartRect.top + (height - chartRect.bottom))) / 3
        } else {
            cellWidth = (width - (chartRect.left + (width - chartRect.right))) / 8
            cellHeight = (height - (chartRect.top + (height - chartRect.bottom))) / 2

            marginWidthSmallCell = cellWidth * 0.1f
            widthSmallCellSize = cellWidth / 3 - marginWidthSmallCell
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBackground(canvas)
        drawGrid(canvas)
        drawText(canvas)
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

    private fun drawGrid(canvas: Canvas) {
        if (typeWeek) {
            drawLines(
                canvas,
                lineDrawWay = LineDrawWay.Horizontally,
                linesCount = 4,
                spaceBetween = cellHeight,
                linesStartPoint = chartRect.top,
                lineLength = chartRect.width(),
                edgeStartPoint = chartRect.left,
                paint = straightLinePaint,
                typeWeek = true
            )

            drawLines(
                canvas,
                lineDrawWay = LineDrawWay.Vertically,
                linesCount = 9,
                spaceBetween = cellWidth,
                linesStartPoint = chartRect.left,
                lineLength = chartRect.height(),
                edgeStartPoint = chartRect.top,
                paint = brokenLinePaint,
                typeWeek = true
            )

        } else {
            drawLines(
                canvas,
                lineDrawWay = LineDrawWay.Horizontally,
                linesCount = 2,
                spaceBetween = cellHeight,
                linesStartPoint = chartRect.top,
                lineLength = chartRect.width(),
                edgeStartPoint = chartRect.left,
                paint = straightLinePaint,
                typeWeek = false
            )

            drawLines(
                canvas,
                lineDrawWay = LineDrawWay.Vertically,
                linesCount = 8,
                spaceBetween = cellWidth,
                linesStartPoint = chartRect.left,
                lineLength = chartRect.height(),
                edgeStartPoint = chartRect.top,
                paint = brokenLinePaint,
                typeWeek = false
            )
        }
    }

    private fun drawText(canvas: Canvas) {
        if (typeWeek) {
            drawHorizontalText(canvas, horizontalLinesTextsForWeek, cellHeight)
            drawVerticalText(canvas, verticalLinesTextsForWeek, cellWidth)
        } else {
            drawHorizontalText(canvas, horizontalLinesTextsForHour, cellHeight)
            drawVerticalText(canvas, verticalLinesTextsForHours, cellWidth)
        }
    }

    private fun drawHorizontalText(
        canvas: Canvas,
        textItems: List<String>,
        spaceBetweenLines: Float
    ) {
        for (i in textItems.indices) {
            canvas.drawText(
                textItems[i],
                chartRect.right + 20f,
                chartRect.top + (spaceBetweenLines * i),
                textPaint
            )
        }
    }

    private fun drawVerticalText(
        canvas: Canvas,
        textItems: List<String>,
        spaceBetweenLines: Float
    ) {
        for (i in textItems.indices) {
            canvas.drawText(
                textItems[i],
                chartRect.left + (spaceBetweenLines * i) + cellWidth / 3,
                chartRect.bottom + 40f,
                textPaint
            )
        }
    }

    private fun drawLines(
        canvas: Canvas,
        lineDrawWay: LineDrawWay,
        linesCount: Int,
        spaceBetween: Float,
        linesStartPoint: Float,
        edgeStartPoint: Float,
        lineLength: Float,
        paint: Paint,
        typeWeek: Boolean
    ) {
        if (typeWeek) {
            for (i in 0..linesCount) {
                val stepValue = linesStartPoint + spaceBetween * i
                when (lineDrawWay) {
                    LineDrawWay.Vertically -> {
                        canvas.drawLine(
                            stepValue,
                            edgeStartPoint,
                            stepValue,
                            lineLength + edgeStartPoint,
                            paint
                        )
                    }
                    LineDrawWay.Horizontally -> {
                        canvas.drawLine(
                            edgeStartPoint,
                            stepValue,
                            lineLength + edgeStartPoint,
                            stepValue,
                            paint
                        )
                    }
                }
            }

        } else {
            for (i in 0..linesCount) {
                val stepValue = linesStartPoint + spaceBetween * i
                when (lineDrawWay) {
                    LineDrawWay.Vertically -> {
                        canvas.drawLine(
                            stepValue,
                            edgeStartPoint,
                            stepValue,
                            lineLength + edgeStartPoint,
                            paint
                        )
                    }
                    LineDrawWay.Horizontally -> {
                        canvas.drawLine(
                            edgeStartPoint,
                            stepValue,
                            lineLength + edgeStartPoint,
                            stepValue,
                            paint
                        )
                    }
                }
            }
        }
    }

    private fun drawProgress(canvas: Canvas) {
        if (typeWeek) {
            val progressesCount = 9
            val progressContainerWidth = chartRect.width() / progressesCount
            val progressMargin = progressContainerWidth / 4
            val progressWidth = progressContainerWidth - progressMargin * 2

            canvas.clipRect(
                chartRect.left,
                chartRect.height() + chartRect.top,
                chartRect.right,
                chartRect.height() + 5f + chartRect.top,
                Region.Op.DIFFERENCE
            )

            progressesHeights.forEachIndexed { index, i ->
                val left = index * (progressContainerWidth) + chartRect.left + progressMargin
                val top = chartRect.bottom
                val right =
                    index * (progressContainerWidth) + progressWidth + chartRect.left + progressMargin
                val bottom =
                    (i.toFloat() / 90) * -chartRect.height() + chartRect.height() + chartRect.top

                progressPaint.shader = LinearGradient(
                    0f,
                    top,
                    0f,
                    bottom,
                    resources.getColor(R.color.white),
                    resources.getColor(R.color.green),
                    Shader.TileMode.CLAMP
                )

                canvas.drawRoundRect(
                    left,
                    top + 5f,
                    right,
                    bottom,
                    5f,
                    5f,
                    progressPaint
                )
            }

        } else {
            val progressesCount = 24
            val progressMargin = 5f
            val progressContainerWidth = chartRect.width() / progressesCount
            val progressWidth = progressContainerWidth - progressMargin * 2

            canvas.clipRect(
                chartRect.left,
                chartRect.height() + chartRect.top,
                chartRect.right,
                chartRect.height() + 5f + chartRect.top,
                Region.Op.DIFFERENCE
            )

            progressesHeights.forEachIndexed { index, i ->
                val left = index * (progressContainerWidth) + chartRect.left + progressMargin
                val top = chartRect.bottom
                val right =
                    index * (progressContainerWidth) + progressWidth + chartRect.left + progressMargin
                val bottom =
                    (i.toFloat() / 60) * -chartRect.height() + chartRect.height() + chartRect.top

                progressPaint.shader = LinearGradient(
                    0f,
                    top,
                    0f,
                    bottom,
                    resources.getColor(R.color.white),
                    resources.getColor(R.color.green),
                    Shader.TileMode.CLAMP
                )

                canvas.drawRoundRect(
                    left,
                    top + 5f,
                    right,
                    bottom,
                    5f,
                    5f,
                    progressPaint
                )
            }
        }
    }

    private fun checkDays() {
        for (i in 0..8) {
            val t = Calendar.getInstance().timeInMillis - (86_400_000L * i)
            verticalLinesTextsForWeek.add(SimpleDateFormat("EE").format(t))
        }
        verticalLinesTextsForWeek.reverse()
    }

    private enum class LineDrawWay {
        Vertically,
        Horizontally
    }
}