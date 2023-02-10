package yin_kio.rect_progress_bar

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes

class RectProgressBar : View {


    private val canalPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.GREEN
    }
    private val indicatorPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLUE
    }
    private val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
    }

    var indicatorColor: Int get() =  indicatorPaint.color
        set(value) {
            indicatorPaint.color = value
            invalidate()
        }
    var canalColor: Int get() = canalPaint.color
        set(value) {
            canalPaint.color = value
            invalidate()
        }
    var shadowColor: Int get() = shadowPaint.color
        set(value) {
            shadowPaint.color = value
            invalidate()
        }
    var isDrawShadow: Boolean = false


    private var _cornerRadiusDp: Float = 5f.dp
    var cornersRadius: Float
        get() = _cornerRadiusDp.dpToFloat()
        set(value) {
            _cornerRadiusDp = value.dp
            invalidate()
        }


    var progress: Float = 0.5f
        set(value) {
            field = value
            invalidate()
        }


    private val clipPath = Path()
    private val canalRect = RectF()


    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) { initAttrs(context, attrs) }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    private fun initAttrs(context: Context?, attrs: AttributeSet?){
        context?.withStyledAttributes(
            attrs,
            R.styleable.RectProgressBar,
            0
        ){
            progress = getFloat(R.styleable.RectProgressBar_progress, 0f)
            indicatorColor = getColor(R.styleable.RectProgressBar_indicatorColor, Color.BLUE)
            canalColor = getColor(R.styleable.RectProgressBar_canalColor, Color.GREEN)
            shadowColor = getColor(R.styleable.RectProgressBar_shadowColor, Color.BLACK)
            _cornerRadiusDp = getDimension(R.styleable.RectProgressBar_cornersRadius, 0f)
            isDrawShadow = getBoolean(R.styleable.RectProgressBar_drawShadow, false)
        }
    }




    private val Float.dp: Float
        get() = (this * Resources.getSystem().displayMetrics.density + 0.5f)

    private fun Float.dpToFloat(): Float {
        val scale = resources.displayMetrics.density
        return (this * scale)
    }


    override fun onDraw(canvas: Canvas?) {
        canvas?.apply {
            setupCanalRect()
            clipOutCanal()
            drawCanal()
            drawShadow()
            drawProgress()
        }
    }

    private fun Canvas.setupCanalRect() {
        canalRect.apply {
            left = 0f
            top = 0f
            right = width.toFloat()
            bottom = height.toFloat()
        }
    }

    private fun Canvas.clipOutCanal() {
        clipPath.apply {
            reset()
            addRoundRect(canalRect, _cornerRadiusDp, _cornerRadiusDp, Path.Direction.CW)
            close()
        }
        clipPath(clipPath)
    }

    private fun Canvas.drawCanal(){
        drawRoundRect(
            canalRect,
            _cornerRadiusDp,
            _cornerRadiusDp,
            canalPaint
        )
    }

    private fun Canvas.drawProgress(){
        drawProgressRect(indicatorPaint)
    }

    private fun Canvas.drawShadow() {
        if (!isDrawShadow) return

        val bitmap = shadowBitmapOnNewCanvas {
            drawProgressRect(shadowPaint)
        }

        drawBitmap(bitmap, 0f, 0f, null)
    }

    private fun Canvas.drawProgressRect(paint: Paint){
        drawRoundRect(
            0f,
            0f,
            width.toFloat() * progress,
            height.toFloat(),
            _cornerRadiusDp,
            _cornerRadiusDp,
            paint
        )
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