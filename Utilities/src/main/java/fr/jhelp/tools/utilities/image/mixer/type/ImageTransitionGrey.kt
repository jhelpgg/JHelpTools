package fr.jhelp.tools.utilities.image.mixer.type

import android.graphics.Bitmap
import fr.jhelp.tools.utilities.image.colorParts
import fr.jhelp.tools.utilities.image.computeY
import fr.jhelp.tools.utilities.image.limitPart

/**
 * Image mixing to progress along the grey version of an image.
 *
 * Progression goes from dark to light
 *
 * @param sourceImageGrey Image used to have grey progression
 */
class ImageTransitionGrey(sourceImageGrey: Bitmap) : ImageMixingType()
{
    private val widthGrey = sourceImageGrey.width
    private val heightGrey = sourceImageGrey.height
    private val greyPixels = IntArray(sourceImageGrey.width * sourceImageGrey.height)

    init
    {
        val pixels = IntArray(this.widthGrey * this.heightGrey)
        sourceImageGrey.getPixels(pixels, 0, this.widthGrey, 0, 0, this.widthGrey, this.heightGrey)

        for ((index, color) in pixels.withIndex())
        {
            val (_, red, green, blue) = color.colorParts
            this.greyPixels[index] = limitPart(computeY(red, green, blue).toInt())
        }
    }

    override fun mix(firstImagePixels: IntArray, secondImagePixels: IntArray, resultImagePixels: IntArray, width: Int, height: Int, percent: Float)
    {
        /* Compute grey value at pixel coordinates */
        val grey: (x: Int, y: Int) -> Int = { x, y -> this.greyPixels[(x * this.widthGrey) / width + ((y * this.heightGrey) / height) * width] }
        /* Grey limit to know if have to use first or second image pixel */
        val greyLimit = (255 * percent).toInt()
        var pixel = 0

        for (y in 0 until height)
        {
            for (x in 0 until width)
            {
                resultImagePixels[pixel] = if (grey(x, y) > greyLimit) firstImagePixels[pixel] else secondImagePixels[pixel]
                pixel++
            }
        }
    }
}