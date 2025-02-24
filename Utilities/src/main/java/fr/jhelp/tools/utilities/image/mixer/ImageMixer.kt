package fr.jhelp.tools.utilities.image.mixer

import android.graphics.Bitmap
import androidx.core.graphics.scale
import fr.jhelp.tools.utilities.image.mixer.type.ImageMixingType
import fr.jhelp.tools.utilities.math.bounds
import kotlin.math.max

/**
 * Image mixer. It mix two images using a given mixing type
 * @param firstImage First/start image
 * @param secondImage Second/end image
 * @param imageMixingType Mixing type
 */
class ImageMixer(firstImage: Bitmap, secondImage: Bitmap, private val imageMixingType: ImageMixingType)
{
    private val firstPixels: IntArray
    private val secondPixels: IntArray
    private val resultPixels: IntArray

    /**
     * Width of the result image
     */
    val width: Int

    /**
     * Height of the result image
     */
    val height: Int

    /**
     * Result image
     */
    val resultImage: Bitmap

    /**
     * Current percent of mixing.
     *
     * Between [[0, 1]]. More near 0, more the result look first image, more near 1, more the result look second image
     */
    var percent: Float = 0f
        set(value)
        {
            field = percent(value)
        }

    init
    {
        this.width = max(firstImage.width, secondImage.width)
        this.height = max(firstImage.height, secondImage.height)
        this.firstPixels = IntArray(width * height)
        firstImage.scale(width, height, false).getPixels(this.firstPixels, 0, width, 0, 0, width, height)
        this.secondPixels = IntArray(width * height)
        secondImage.scale(width, height, false).getPixels(this.secondPixels, 0, width, 0, 0, width, height)
        this.resultPixels = IntArray(width * height)
        System.arraycopy(this.firstPixels, 0, this.resultPixels, 0, this.firstPixels.size)
        this.resultImage = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        this.resultImage.setPixels(this.resultPixels, 0, width, 0, 0, width, height)
        this.imageMixingType.initialize(width, height)
    }

    private fun percent(percent: Float): Float
    {
        val percentReal = percent.bounds(0f, 1f)
        this.imageMixingType.mixing(this.firstPixels, this.secondPixels, this.resultPixels, this.resultImage.width, this.resultImage.height, percentReal)
        this.resultImage.setPixels(this.resultPixels, 0, this.resultImage.width, 0, 0, this.resultImage.width, this.resultImage.height)
        return percentReal
    }
}