package fr.jhelp.tools.utilities.image.mixer.type

import fr.jhelp.tools.utilities.math.compare

/**
 * Image mixing type
 */
sealed class ImageMixingType
{
    /**
     * Initialize mixing
     * @param width Width of the image
     * @param height Height of the image
     */
    open fun initialize(width: Int, height: Int) = Unit

    /**
     * Compute mix between two images.
     *
     * It suppose that images have same dimensions
     *
     * @param firstImagePixels First image pixels
     * @param secondImagePixels Second image pixels
     * @param resultImagePixels Result image pixels
     * @param width Width of the images
     * @param height Height of the images
     * @param percent Percent of mixing.
     */
    fun mixing(firstImagePixels: IntArray, secondImagePixels: IntArray, resultImagePixels: IntArray,
               width: Int, height: Int,
               percent: Float)
    {
        if (percent.compare(0f) <= 0)
        {
            System.arraycopy(firstImagePixels, 0, resultImagePixels, 0, firstImagePixels.size)
            return
        }

        if (percent.compare(1f) >= 0)
        {
            System.arraycopy(secondImagePixels, 0, resultImagePixels, 0, secondImagePixels.size)
            return
        }

        this.mix(firstImagePixels, secondImagePixels, resultImagePixels, width, height, percent)
    }

    protected abstract fun mix(firstImagePixels: IntArray, secondImagePixels: IntArray, resultImagePixels: IntArray,
                               width: Int, height: Int,
                               percent: Float)
}
