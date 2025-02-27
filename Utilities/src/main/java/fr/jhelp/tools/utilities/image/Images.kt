package fr.jhelp.tools.utilities.image

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import fr.jhelp.tools.utilities.checkArgument
import fr.jhelp.tools.utilities.math.bounds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/**Mask use to get only alpha part (remove color information) */
const val ALPHA_MASK: Int = 0xFF000000.toInt()

/**Mask use to get only color part (remove alpha information) */
const val COLOR_MASK: Int = 0x00FFFFFF

/** Color int alpha part */
val Int.alpha: Int get() = this ushr 24

/** Color int red part */
val Int.red: Int get() = (this shr 16) and 0xFF

/** Color int green part */
val Int.green: Int get() = (this shr 8) and 0xFF

/** Color int blue part */
val Int.blue: Int get() = this and 0xFF

/** Get color parts */
val Int.colorParts : ColorParts get() = ColorParts(this)

/**
 * Create color whit given Alpha, Red, Green and Blue parts
 */
fun color(alpha: Int, red: Int, green: Int, blue: Int): Int =
    (alpha shl 24) or (red shl 16) or (green shl 8) or blue

/**
 * Limit a value to what a color part support as value
 */
fun limitPart(integer: Int): Int = integer.bounds(0, 255)

/**
 * Limit a value to what a color part support as value
 */
fun limitPart(float: Float): Int = float.bounds(0f, 255f).toInt()

/**
 * Compute blue part of color from YUV
 *
 * B = Y + 1.7790 * (U - 128)
 *
 * @param y Y
 * @param u U
 * @param v V
 * @return Blue part
 */
fun computeBlue(y: Double, u: Double, v: Double): Int =
    limitPart((y + 1.7721604 * (u - 128) + 0.0009902 * (v - 128)).toInt())

/**
 * Compute green part of color from YUV

 * G = Y - 0.3455 * (U - 128) - (0.7169 * (V - 128))
 *
 * @param y Y
 * @param u U
 * @param v V
 * @return Green part
 */
fun computeGreen(y: Double, u: Double, v: Double): Int =
    limitPart((y - 0.3436954 * (u - 128) - 0.7141690 * (v - 128)).toInt())

/**
 * Compute red part of color from YUV

 * R = Y + 1.4075 * (V - 128)
 *
 * @param y Y
 * @param u U
 * @param v V
 * @return Red part
 */
fun computeRed(y: Double, u: Double, v: Double): Int =
    limitPart((y - 0.0009267 * (u - 128) + 1.4016868 * (v - 128)).toInt())

/**
 * Compute U of a color

 * U = R * -0.168736 + G * -0.331264 + B * 0.500000 + 128
 *
 * @param red   Red part
 * @param green Green part
 * @param blue  Blue part
 * @return U
 */
fun computeU(red: Int, green: Int, blue: Int): Double =
    -0.169 * red - 0.331 * green + 0.500 * blue + 128.0

/**
 * Compute V of a color

 * V = R * 0.500000 + G * -0.418688 + B * -0.081312 + 128
 *
 * @param red   Red part
 * @param green Green part
 * @param blue  Blue part
 * @return V
 */
fun computeV(red: Int, green: Int, blue: Int): Double =
    0.500 * red - 0.419 * green - 0.081 * blue + 128.0

/**
 * Compute Y of a color

 * Y = R * 0.299000 + G * 0.587000 + B * 0.114000
 *
 * @param red   Red part
 * @param green Green part
 * @param blue  Blue part
 * @return Y
 */
fun computeY(red: Int, green: Int, blue: Int): Double = red * 0.299 + green * 0.587 + blue * 0.114

/**
 * Clear bitmap with given color
 */
fun Bitmap.clear(color: Int)
{
    this.pixelsOperation { pixels ->
        for (index in pixels.indices)
        {
            pixels[index] = color
        }
    }
}

/**
 * Transform the bitmap to its gey version
 */
fun Bitmap.grey()
{
    this.pixelsOperation { pixels ->
        var color: Int
        var y: Int

        for (index in pixels.indices)
        {
            color = pixels[index]
            y = limitPart(computeY(color.red, color.green, color.blue).toInt())
            pixels[index] = (color and ALPHA_MASK) or (y shl 16) or (y shl 8) or y
        }
    }
}

/**
 * Tint the bitmap with given color
 */
fun Bitmap.tint(color: Int)
{
    this.grey()

    this.pixelsOperation { pixels ->
        val red = color.red
        val green = color.green
        val blue = color.blue
        var col: Int
        var grey: Int

        for (index in pixels.indices)
        {
            col = pixels[index]
            grey = col.blue
            pixels[index] = (col and ALPHA_MASK) or
                    (((red * grey) shr 8) shl 16) or
                    (((green * grey) shr 8) shl 8) or
                    ((blue * grey) shr 8)
        }
    }
}

/**
 * Apply a mask to a part of the bitmap
 *
 * It applies mask alpha information to this bitmap map
 */
fun Bitmap.mask(mask: Bitmap, x: Int, y: Int, width: Int, height: Int)
{
    val sourceWidth = this.width
    val sourceHeight = this.height
    val maskWidth = mask.width
    val maskHeight = mask.height
    var xx = x
    var yy = y
    var w = width
    var h = height

    if (xx < 0)
    {
        w += xx
        xx = 0
    }

    if (yy < 0)
    {
        h += yy
        yy = 0
    }

    w = minOf(w, sourceWidth - xx, maskWidth - xx)
    h = minOf(h, sourceHeight - yy, maskHeight - yy)

    if (w <= 0 || h <= 0)
    {
        return
    }

    this.pixelsOperation { pixels ->
        mask.pixelsOperation { alphas ->
            var lineSource = xx + yy * sourceWidth
            var lineMask = 0
            var pixelSource: Int
            var pixelsMask: Int

            for (yyy in 0 until h)
            {
                pixelSource = lineSource
                pixelsMask = lineMask

                for (xxx in 0 until w)
                {
                    pixels[pixelSource] = (alphas[pixelsMask] and ALPHA_MASK) or
                            (pixels[pixelSource] and COLOR_MASK)
                    pixelSource++
                    pixelsMask++
                }

                lineSource += sourceWidth
                lineMask += maskWidth
            }
        }
    }
}

/**
 * Shift bitmap pixels
 */
fun Bitmap.shift(x: Int, y: Int)
{
    var index = x + y * this.width

    this.pixelsOperation { pixels ->
        val size = pixels.size
        index %= size

        if (index < 0)
        {
            index += size
        }

        val temp = IntArray(size)
        System.arraycopy(pixels, 0, temp, 0, size)

        for (pix in 0 until size)
        {
            pixels[pix] = temp[index]
            index = (index + 1) % size
        }
    }
}

/**
 * Copy another bitmap.
 *
 * The bitmap must have same dimensions as this bitmap
 */
fun Bitmap.copy(bitmap: Bitmap)
{
    if (this.width != bitmap.width || this.height != bitmap.height)
    {
        throw IllegalArgumentException("We can only copy with an image of same size")
    }

    this.pixelsOperation { destination ->
        bitmap.pixelsOperation { source ->
            System.arraycopy(source, 0, destination, 0, source.size)
        }
    }
}

/**
 * Create a copy of the bitmap
 */
fun Bitmap.copy(): Bitmap =
    this.copy(Bitmap.Config.ARGB_8888, true)

/**
 * Make the bitmap mutable.
 *
 * If the bitmap already mutable, it is return.
 *
 * Else a mutable copy is created and returned
 */
fun Bitmap.mutable(): Bitmap
{
    if (this.isMutable)
    {
        return this
    }

    val bitmap = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888)
    bitmap.fitSpace(this)
    return bitmap
}

/**
 * Change image contrast by using the middle of the minimum and maximum
 * @param factor Factor to apply to the contrast
 */
fun Bitmap.contrast(factor: Double)
{
    this.pixelsOperation { pixels ->
        var y: Double
        var index = pixels.size - 1
        var color = pixels[index]
        var red = color.red
        var green = color.green
        var blue = color.blue
        var yMax = computeY(red, green, blue)
        var yMin = yMax

        index--
        while (index >= 0)
        {
            color = pixels[index]
            red = color.red
            green = color.green
            blue = color.blue

            y = computeY(red, green, blue)

            yMin = min(yMin, y)
            yMax = max(yMax, y)

            index--
        }

        val yMil = (yMin + yMax) / 2
        var u: Double
        var v: Double

        pixels.indices.forEach {
            color = pixels[it]
            red = color.red
            green = color.green
            blue = color.blue

            y = computeY(red, green, blue)
            u = computeU(red, green, blue)
            v = computeV(red, green, blue)

            y = yMil + factor * (y - yMil)

            pixels[it] = (color and ALPHA_MASK
                    or (computeRed(y, u, v) shl 16)
                    or (computeGreen(y, u, v) shl 8)
                    or computeBlue(y, u, v))
        }
    }
}

/**
 * Multiply this bitmap pixels by given bitmap pixels.
 *
 * The given bitmap must have same dimension that this one
 */
fun Bitmap.multiply(bitmap: Bitmap)
{
    if (this.width != bitmap.width || this.height != bitmap.height)
    {
        throw IllegalArgumentException("We can only multiply with an image of same size")
    }

    this.pixelsOperation { thisPixels ->
        bitmap.pixelsOperation { imagePixels ->
            var colorThis: Int
            var colorImage: Int

            thisPixels.indices.forEach {
                colorThis = thisPixels[it]
                colorImage = imagePixels[it]

                thisPixels[it] = colorThis and ALPHA_MASK or
                        (((colorThis.red * colorImage.red) / 255) shl 16) or
                        (((colorThis.green * colorImage.green) / 255) shl 8) or
                        ((colorThis.blue * colorImage.blue) / 255)
            }
        }
    }
}

/**
 * Add this bitmap pixels with given bitmap pixels.
 *
 * The given bitmap must have same dimension that this one
 */
fun Bitmap.add(bitmap: Bitmap)
{
    if (this.width != bitmap.width || this.height != bitmap.height)
    {
        throw IllegalArgumentException("We can only add with an image of same size")
    }

    this.pixelsOperation { thisPixels ->
        bitmap.pixelsOperation { imagePixels ->
            var colorThis: Int
            var colorImage: Int

            thisPixels.indices.forEach {
                colorThis = thisPixels[it]
                colorImage = imagePixels[it]

                thisPixels[it] = colorThis and ALPHA_MASK or
                        (limitPart(colorThis.red + colorImage.red) shl 16) or
                        (limitPart(colorThis.green + colorImage.green) shl 8) or
                        limitPart(colorThis.blue + colorImage.blue)
            }
        }
    }
}

/**
 * Make the image darker
 */
fun Bitmap.darker(darkerFactor: Int)
{
    val factor = darkerFactor.bounds(0, 255)

    this.pixelsOperation { pixels ->
        var color: Int

        pixels.indices.forEach {
            color = pixels[it]
            pixels[it] = color and ALPHA_MASK or
                    (limitPart(color.red - factor) shl 16) or
                    (limitPart(color.green - factor) shl 8) or
                    limitPart(color.blue - factor)
        }
    }
}

/**
 * Make the image lighter
 */
fun Bitmap.lighter(lighterFactor: Int)
{
    val factor = lighterFactor.bounds(0, 255)

    this.pixelsOperation { pixels ->
        var color: Int

        pixels.indices.forEach {
            color = pixels[it]
            pixels[it] = color and ALPHA_MASK or
                    (limitPart(color.red + factor) shl 16) or
                    (limitPart(color.green + factor) shl 8) or
                    limitPart(color.blue + factor)
        }
    }
}

/**
 * Invert bitmap colors
 */
fun Bitmap.invertColors()
{
    this.pixelsOperation { pixels ->
        var color: Int
        pixels.indices.forEach {
            color = pixels[it]

            pixels[it] = color and ALPHA_MASK or
                    ((255 - color.red) shl 16) or
                    ((255 - color.green) shl 8) or
                    (255 - color.blue)
        }
    }
}

/**
 * Do operation on bitmap pixels
 */
inline fun Bitmap.pixelsOperation(operation: (IntArray) -> Unit)
{
    val pixels = IntArray(this.width * this.height)
    this.getPixels(pixels, 0, this.width, 0, 0, this.width, this.height)
    operation(pixels)
    this.setPixels(pixels, 0, this.width, 0, 0, this.width, this.height)
}

/**
 * Draw a bitmap scaled to take all place in this bitmap
 */
fun Bitmap.fitSpace(bitmap: Bitmap)
{
    val canvas = Canvas(this)
    canvas.fitRectangle(bitmap, 0, 0, this.width, this.height)
}

private const val addLastLeft = 0b0001
private const val addLastRight = 0b0010
private const val addLastUp = 0b0100
private const val addLastDown = 0b1000
private const val addLastAll = addLastLeft or addLastRight or addLastUp or addLastDown
private const val addLastUpRight = addLastUp or addLastRight
private const val addLastUpLeft = addLastUp or addLastLeft
private const val addLastDownRight = addLastDown or addLastRight
private const val addLastDownLeft = addLastDown or addLastLeft

private data class PixelInfo(val x: Int, val y: Int, val pixel: Int, val addLast: Int)

/**
 * Color a bitmap from a given position
 * @param x X position
 * @param y Y position
 * @param color Color to apply
 * @param precision Precision to choose what means same color
 * @param refreshFrequency Frequency to refresh bitmap (number of pixels colored between each refresh callback
 * @param refresh Callback to refresh bitmap. It takes a boolean in parameter to indicates if the coloring is finished
 * @throws IllegalArgumentException If the point is out of the bitmap or precision is negative or zero
 */
@Throws(IllegalArgumentException::class)
fun Bitmap.coloringAreaFromPointWithColorAnimated(x: Int, y: Int, color: Int, precision: Int = 1, refreshFrequency: Int = 16384, refresh: (Boolean) -> Unit = {})
{
    (x >= 0 && x < this.width && y >= 0 && y < this.height).checkArgument("The point ($x, $y) is out of the bitmap ${this.width}x${this.height}")

    (precision > 0).checkArgument("The precision must be greater than 0")

    val pixels = IntArray(this.width * this.height)
    this.getPixels(pixels, 0, this.width, 0, 0, this.width, this.height)
    val bitmap = this
    val width = this.width
    val height = this.height
    val same: (color1: Int, color2: Int) -> Boolean =
        { color1, color2 ->
            abs(color1.red - color2.red) <= precision
                    && abs(color1.green - color2.green) <= precision
                    && abs(color1.blue - color2.blue) <= precision
        }

    CoroutineScope(Dispatchers.Default).launch {
        val colorOnBitmap = pixels[x + y * width]

        if (same(color, colorOnBitmap))
        {
            refresh(true)
            return@launch
        }

        val stack = ArrayDeque<PixelInfo>()
        stack.addLast(PixelInfo(x, y, x + y * width, addLastAll))
        var count = 0

        while (stack.isNotEmpty())
        {
            val (xx, yy, pixel, addLast) = stack.removeFirst()

            if (same(pixels[pixel], colorOnBitmap).not())
            {
                continue
            }

            pixels[pixel] = color

            count++

            if (count >= refreshFrequency)
            {
                bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
                refresh(false)
                count = 0
            }

            if (xx > 0 && same(pixels[pixel - 1], colorOnBitmap))
            {
                if (addLast and addLastLeft != 0)
                {
                    stack.addLast(PixelInfo(xx - 1, yy, pixel - 1, addLastUpLeft))
                }
                else
                {
                    stack.addFirst(PixelInfo(xx - 1, yy, pixel - 1, addLastUpLeft))
                }
            }

            if (xx < width - 1 && same(pixels[pixel + 1], colorOnBitmap))
            {
                if (addLast and addLastRight != 0)
                {
                    stack.addLast(PixelInfo(xx + 1, yy, pixel + 1, addLastDownRight))
                }
                else
                {
                    stack.addFirst(PixelInfo(xx + 1, yy, pixel + 1, addLastDownRight))
                }
            }

            if (yy > 0 && same(pixels[pixel - width], colorOnBitmap))
            {
                if (addLast and addLastUp != 0)
                {
                    stack.addLast(PixelInfo(xx, yy - 1, pixel - width, addLastUpRight))
                }
                else
                {
                    stack.addFirst(PixelInfo(xx, yy - 1, pixel - width, addLastUpRight))
                }
            }

            if (yy < height - 1 && same(pixels[pixel + width], colorOnBitmap))
            {
                if (addLast and addLastDown != 0)
                {
                    stack.addLast(PixelInfo(xx, yy + 1, pixel + width, addLastDownLeft))
                }
                else
                {
                    stack.addFirst(PixelInfo(xx, yy + 1, pixel + width, addLastDownLeft))
                }
            }
        }

        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        refresh(true)
    }
}

/**
 * Create a bumped image from base image and image for bump.
 *
 * The images must have same dimensions
 */
fun createBumped(source: Bitmap, bump: Bitmap,
                 contrast: Double = 0.75, dark: Int = 12, shiftX: Int = 1, shiftY: Int = 1): Bitmap
{
    var contrastLocal = contrast
    val width = source.width
    val height = source.height

    if (width != bump.width || height != bump.height)
    {
        throw IllegalArgumentException("Images must have the same size")
    }

    if (contrastLocal < 0.5)
    {
        contrastLocal *= 2.0
    }
    else
    {
        contrastLocal = contrastLocal * 18 - 8
    }

    val bumped = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val temp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

    bumped.copy(bump)
    bumped.grey()
    bumped.contrast(contrastLocal)

    temp.copy(bumped)
    temp.multiply(source)
    temp.darker(dark)

    bumped.invertColors()
    bumped.multiply(source)
    bumped.darker(dark)
    bumped.shift(shiftX, shiftY)
    bumped.add(temp)

    temp.recycle()
    return bumped
}

/**
 * Create a bitmap and specify what to draw on it
 */
fun createBitmap(width: Int, height: Int, draw: (Bitmap, Canvas, Paint) -> Unit): Bitmap
{
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    draw(bitmap, canvas, paint)
    return bitmap
}

/**
 * Draw on bitmap
 */
fun Bitmap.draw(draw: (Canvas, Paint) -> Unit)
{
    val canvas = Canvas(this)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    draw(canvas, paint)
}

internal fun mixParts(under: Int, over: Int, alpha: Int, ahpla: Int) =
    (under * ahpla + over * alpha) shr 8

internal fun mix(under: Int, over: Int): Int
{
    val alpha = over ushr 24
    val ahpla = 256 - alpha
    return (min(255, (under ushr 24) + alpha) shl 24) or
            (mixParts((under shr 16) and 0xFF, (over shr 16) and 0xFF,
                      alpha, ahpla) shl 16) or
            (mixParts((under shr 8) and 0xFF, (over shr 8) and 0xFF, alpha, ahpla) shl 8) or
            mixParts(under and 0xFF, over and 0xFF, alpha, ahpla)
}

internal fun drawPixels(source: IntArray, xSource: Int, ySource: Int, widthSource: Int,
                        destination: IntArray, xDestination: Int, yDestination: Int,
                        widthDestination: Int,
                        width: Int, height: Int)
{
    var lineSource = xSource + ySource * widthSource
    var lineDestination = xDestination + yDestination * widthDestination
    var pixelSource: Int
    var pixelDestination: Int

    for (y in 0 until height)
    {
        pixelSource = lineSource
        pixelDestination = lineDestination

        for (x in 0 until width)
        {
            destination[pixelDestination] =
                mix(destination[pixelDestination], source[pixelSource])
            pixelSource++
            pixelDestination++
        }

        lineSource += widthSource
        lineDestination += widthDestination
    }
}
