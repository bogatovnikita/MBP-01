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

class RectProgressBar : View {

    var progressColor: Int = Color.BLUE
        set(value) {
            field = value
            invalidate()
        }
    var canalColor: Int = Color.CYAN
        set(value) {
            field = value
            invalidate()
        }
    var cornersRadius: Float
        get() = _cornerRadiusDp.dpToFloat()
        set(value) {
            _cornerRadiusDp = value.dp
            invalidate()
        }

    private var _cornerRadiusDp: Float = 5f.dp

    var progress: Float = 0.5f
        set(value) {
            field = value
            invalidate()
        }

    private val Float.dp: Float
        get() = (this * Resources.getSystem().displayMetrics.density + 0.5f)

    private fun Float.dpToFloat(): Float {
        val scale = resources.displayMetrics.density
        return (this * scale)
    }


    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    private val canalPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = canalColor
    }
    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = progressColor
    }
    private val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
    }
    private val clipPath = Path()
    private val canalRect = RectF()


    override fun onDraw(canvas: Canvas?) {
        canvas?.apply {
            setupCanalRect()
            clipOutCanal()
            drawCanal()
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
        val bitmap = shadowBitmapOnNewCanvas {
            drawProgressRect(shadowPaint)
        }

        drawBitmap(bitmap, 0f, 0f, null)
        drawProgressRect(progressPaint)
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