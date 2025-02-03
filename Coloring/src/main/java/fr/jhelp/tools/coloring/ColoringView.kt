package fr.jhelp.tools.coloring

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import fr.jhelp.tools.utilities.defaultBitmapBlackWhite
import fr.jhelp.tools.utilities.image.blue
import fr.jhelp.tools.utilities.image.coloringAreaFromPointWithColorAnimated
import fr.jhelp.tools.utilities.image.draw
import fr.jhelp.tools.utilities.image.fitRectangle
import fr.jhelp.tools.utilities.image.fitSpace
import fr.jhelp.tools.utilities.image.green
import fr.jhelp.tools.utilities.image.red
import fr.jhelp.tools.utilities.source.Source
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.max

class ColoringView(context: Context, attributeSet: AttributeSet? = null) : View(context, attributeSet)
{
    private var imageWidth = 2
    private var imageHeight = 2
    private var image = defaultBitmapBlackWhite()
    private val coloring = AtomicBoolean(false)
    var color = 0xFF00FF00.toInt()
    var precision = 1
        set(value)
        {
            field = max(1, value)
        }
    var refreshRate = 8192
        set(value)
        {
            field = max(1024, value)
        }

    fun image(source: Source)
    {
        CoroutineScope(Dispatchers.Default).launch {
            delay(1024)
            this@ColoringView.image.fitSpace(BitmapFactory.decodeStream(source.inputStream()))
            this@ColoringView.postInvalidate()
        }
    }

    /**
     * Called when view size changed
     */
    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int)
    {
        if (width != this.imageWidth || height != this.imageHeight)
        {
            this.imageWidth = width
            this.imageHeight = height
            this.updateImage()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean
    {
        if (this.coloring.get())
        {
            return true
        }

        val x = event.x.toInt()
        val y = event.y.toInt()

        val pixel = this.image.getPixel(x, y)

        if (pixel.red < 16 && pixel.green < 16 && pixel.blue < 16)
        {
            return true
        }

        this.coloring.set(true)

        this.image.coloringAreaFromPointWithColorAnimated(x, y, this.color, this.precision, this.refreshRate) { finished ->
            this.postInvalidate()

            if (finished)
            {
                this.coloring.set(false)
            }
        }

        return true
    }

    override fun onDraw(canvas: Canvas)
    {
        canvas.drawBitmap(this.image, 0f, 0f, null)
    }

    private fun updateImage()
    {
        val bitmap =
            Bitmap.createBitmap(this.imageWidth, this.imageHeight, Bitmap.Config.ARGB_8888)
        bitmap.draw { canvas, _ ->
            canvas.fitRectangle(this.image, 0, 0, this.imageWidth, this.imageHeight)
        }
        this.image.recycle()
        this.image = bitmap
        this.postInvalidate()
    }
}