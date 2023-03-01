package yin_kio.horizontal_diagram

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes

class HorizontalDiagram : View {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) { initAttrs(context, attrs) }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    var coefs: List<Float> = listOf(0.2f,0.2f,0.2f,0.2f,0.2f,)
        set(value) {
            field = value
            invalidate()
        }
    var interval: Float = 50f
        set(value) {
            field = value
            invalidate()
        }
    var colors: List<Int> = listOf(Color.BLUE, Color.GREEN, Color.RED)
        set(value) {
            field = value
            invalidate()
        }
    var isDrawShadow: Boolean = false
        set(value) {
            field = value
            invalidate()
        }
    var shadowColor: Int = Color.BLACK
        set(value) {
            field = value
            invalidate()
        }
    var cornersRadius: Float = 25f
        set(value) {
            field = value
            invalidate()
        }
    var padding: Float = 25f
        set(value) {
            field = value
            invalidate()
        }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private fun initAttrs(context: Context?, attrs: AttributeSet?){
        context?.withStyledAttributes(
            attrs,
            R.styleable.HorizontalDiagram,
            0
        ){
            val colorsId = getResourceId(R.styleable.HorizontalDiagram_colors, 0)
            if (colorsId > 0) { colors = resources.getIntArray(colorsId).map { it } }
            cornersRadius = getDimension(R.styleable.HorizontalDiagram_corners_radius, 25f)
            interval = getDimension(R.styleable.HorizontalDiagram_interval, 25f)
            isDrawShadow = getBoolean(R.styleable.HorizontalDiagram_draw_shadow, false)
            shadowColor = getColor(R.styleable.HorizontalDiagram_shadow_color, Color.GRAY)

        }
    }



    override fun onDraw(canvas: Canvas) {
        canvas.apply {
            var inset = padding
            coefs.forEachIndexed {index, coef ->


                val pieceWidth = coef * (width - (coefs.size - 1) * interval - padding * 2)
                drawShadow { drawPiece(inset, pieceWidth) }

                paint.color = colors[index % colors.size]
                drawPiece(inset, pieceWidth)
                inset += pieceWidth + interval
            }


        }
    }

    private fun Canvas.drawPiece(
        inset: Float,
        pieceWidth: Float
    ){
        drawRoundRect(
            inset,
            padding,
            inset + pieceWidth,
            height.toFloat() - padding,
            cornersRadius,
            cornersRadius,
            paint
        )
    }

    private fun Canvas.drawShadow(
        drawPiece: Canvas.() -> Unit
    ) {
        if (!isDrawShadow) return
        paint.color = shadowColor
        val bitmap = shadowBitmapOnNewCanvas(draw = drawPiece)

        drawBitmap(bitmap, 0f, 0f, null)
    }

    private fun shadowBitmapOnNewCanvas(
        blurRadius: Float = 25f,
        draw: Canvas.() -> Unit
    ): Bitmap {
        val shadowBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        val shadowCanvas = Canvas(shadowBitmap)
        draw(shadowCanvas)

        val output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val rs = RenderScript.create(context)
        val blurScript = ScriptIntrinsicBlur.create(rs, Element.RGBA_8888(rs))
        val inAlloc = Allocation.createFromBitmap(rs, shadowBitmap)
        val outAlloc = Allocation.createFromBitmap(rs, output)

        blurScript.setRadius(blurRadius)
        blurScript.setInput(inAlloc)
        blurScript.forEach(outAlloc)
        outAlloc.copyTo(output)
        rs.destroy()
        return output
    }

}