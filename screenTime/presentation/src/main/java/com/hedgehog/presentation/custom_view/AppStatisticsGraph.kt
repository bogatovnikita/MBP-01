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
    private var cellWidth = 0f
    private var cellHeight = 0f

    private var marginWidthSmallCell = 0f
    private var widthSmallCellSize = 0f

    private var backgroundScreenColor by Delegates.notNull<Int>()
    private var lineColor by Delegates.notNull<Int>()
    private val verticalLinesTexts = listOf("00", "03", "06", "09", "12", "15", "18", "21", "00")

    private val horizontalLinesTexts = listOf(
        resources.getString(R.string.min_60),
        resources.getString(R.string.min_30),
        resources.getString(R.string.min_0)
    )


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
        backgroundColorPaint = bgColorPaint()
        brokenLinePaint = brokenLinePaint()
        straightLinePaint = straightLinePaint()
        textPaint = textPaint()
        progressPaint = progressPaint()
    }

    private fun progressPaint() : Paint {
        return Paint(Paint.ANTI_ALIAS_FLAG).apply {
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

    private fun textPaint() : Paint {
        return Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            style = Paint.Style.FILL
            textSize = 10.sp
        }
    }

    private fun straightLinePaint() : Paint{
        return Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = lineColor
            style = Paint.Style.STROKE
        }
    }

    private fun brokenLinePaint() : Paint {
        return Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = lineColor
            style = Paint.Style.STROKE
            pathEffect = DashPathEffect(floatArrayOf(10f, 10f, 10f, 10f), 0f)
        }
    }

    private fun bgColorPaint() : Paint {
        return Paint(Paint.ANTI_ALIAS_FLAG).apply { color = backgroundScreenColor }
    }

    private val Int.sp : Float get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this.toFloat(),
        resources.displayMetrics
    )





    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        fieldRect.left = (width * 0.05).toFloat()
        fieldRect.right = (width * 0.84).toFloat()
        fieldRect.top = (height * 0.1).toFloat()
        fieldRect.bottom = (height * 0.8).toFloat()

        cellWidth = (width - (fieldRect.left + (width - fieldRect.right))) / 8
        cellHeight = (height - (fieldRect.top + (height - fieldRect.bottom))) / 2

        marginWidthSmallCell = cellWidth * 0.1f
        widthSmallCellSize = cellWidth / 3 - marginWidthSmallCell
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
        drawLines(
            canvas,
            lineDrawWay = LineDrawWay.Horizontally,
            linesCount = 2,
            spaceBetween = cellHeight,
            linesStartPoint = fieldRect.top,
            lineLength = fieldRect.width(),
            edgeStartPoint = fieldRect.left,
            paint = straightLinePaint
        )


        drawLines(
            canvas,
            lineDrawWay = LineDrawWay.Vertically,
            linesCount = 8,
            spaceBetween = cellWidth,
            linesStartPoint = fieldRect.left,
            lineLength = fieldRect.height(),
            edgeStartPoint = fieldRect.top,
            paint = brokenLinePaint
        )
    }


    private fun drawText(canvas: Canvas) {
        drawHorizontaText(canvas, horizontalLinesTexts, cellHeight)
        drawVerticalText(canvas, verticalLinesTexts, cellWidth)
    }




    private fun drawHorizontaText(
        canvas: Canvas,
        textItems: List<String>,
        spaceBetweenLines: Float
    ) {

        for (i in 0 until textItems.size) {
            canvas.drawText(
                textItems[i],
                fieldRect.right ,
                fieldRect.top + (spaceBetweenLines * i),
                textPaint
            )
        }
    }

    private fun drawVerticalText(
        canvas: Canvas,
        textItems: List<String>,
        spaceBetweenLines: Float
    ) {

        for (i in 0 until textItems.size) {
            canvas.drawText(
                textItems[i],
                fieldRect.left + (spaceBetweenLines * i),
                fieldRect.bottom,
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
        paint: Paint
    ){
        for (i in 0..linesCount) {
            val stepValue = linesStartPoint + spaceBetween * i
            when(lineDrawWay){
                LineDrawWay.Vertically -> {
                    canvas.drawLine(stepValue, edgeStartPoint, stepValue, lineLength + edgeStartPoint, paint)
                }
                LineDrawWay.Horizontally -> {
                    canvas.drawLine(edgeStartPoint, stepValue, lineLength + edgeStartPoint, stepValue, paint)
                }
            }
        }
    }


    private fun drawProgress(canvas: Canvas) {

    }

    private enum class LineDrawWay{
        Vertically,
        Horizontally
    }
}