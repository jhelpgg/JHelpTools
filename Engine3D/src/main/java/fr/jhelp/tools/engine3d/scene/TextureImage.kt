package fr.jhelp.tools.engine3d.scene

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import fr.jhelp.tools.engine3d.buffer.byteBuffer
import java.nio.ByteBuffer
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Texture based on image for [Material.texture] to decorate an [Object3D] or a [Clone3D]
 *
 * If the texture is sealed, the texture can't change
 */
class TextureImage internal constructor(bitmap: Bitmap, sealed: Boolean) : Texture()
{
    private val sealed = AtomicBoolean(false)
    private var pixels: ByteBuffer? = null
    private var bitmap: Bitmap? = null
    private var dirty = true
    private var canvas: Canvas? = null
    private var paint: Paint? = null
    override val width: Int
    override val height: Int

    init
    {
        this.sealed.set(sealed)
        this.width = bitmap.width
        this.height = bitmap.height
        this.pixels = byteBuffer((this.width * this.height) shl 2)

        if (sealed)
        {
            bitmap.copyPixelsToBuffer(this.pixels!!)
            this.pixels!!.position(0)
            bitmap.recycle()
        }
        else
        {
            if (bitmap.isMutable)
            {
                this.bitmap = bitmap
            }
            else
            {
                val width = bitmap.width
                val height = bitmap.height
                this.bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                val pixels = IntArray(width * height)
                bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
                this.bitmap!!.setPixels(pixels, 0, width, 0, 0, width, height)
                bitmap.recycle()
            }
        }

        System.gc()
    }

    /**
     * Indicates if texture is sealed
     */
    fun sealed(): Boolean =
        this.sealed.get()

    /**
     * Obtain, if texture not sealed, the texture associated bitmap
     */
    fun bitmap(): Bitmap? =
        if (this.sealed.get()) null
        else this.bitmap

    /**
     * Obtain, if texture not sealed, the texture associated canvas
     */
    fun canvas(): Canvas?
    {
        if (this.sealed.get())
        {
            return null
        }

        if (this.canvas == null)
        {
            this.canvas = Canvas(this.bitmap!!)
        }

        return this.canvas
    }

    /**
     * Obtain, if texture not sealed, the texture associated paint
     */
    fun paint(): Paint?
    {
        if (this.sealed.get())
        {
            return null
        }

        if (this.paint == null)
        {
            this.paint = Paint()
        }

        return this.paint
    }

    fun drawOnTexture(drawer: (bitmap: Bitmap, canvas: Canvas, paint: Paint) -> Unit)
    {
        val bitmap = this.bitmap() ?: return
        val canvas = this.canvas() ?: return
        val paint = this.paint() ?: return
        drawer(bitmap, canvas, paint)
        this.refresh()
    }

    /**
     * Request refresh to see last modifications
     */
    fun refresh()
    {
        if (!this.sealed.get())
        {
            this.dirty = true
        }
    }

    /**
     * Seal the texture. It can't be change later
     */
    fun seal()
    {
        if (!this.sealed.getAndSet(true))
        {
            this.bitmap!!.copyPixelsToBuffer(this.pixels!!)
            this.pixels!!.position(0)
            this.bitmap!!.recycle()
            this.bitmap = null
            this.canvas = null
            this.paint = null
            System.gc()
            this.dirty = true
        }
    }

    override fun pixels(): ByteBuffer?
    {
        if (this.dirty.not() || this.sealed.get())
        {
            return null
        }

        this.pixels?.let { pixels ->
            pixels.clear()
            this.bitmap!!.copyPixelsToBuffer(pixels)
        }

        return this.pixels
    }

    override fun afterPixelsPushedInVideoMemory()
    {
        if (this.sealed.get())
        {
            this.pixels?.clear()
            this.pixels = null
        }
    }

    override fun afterBindTexture()
    {
        this.dirty = false
    }
}