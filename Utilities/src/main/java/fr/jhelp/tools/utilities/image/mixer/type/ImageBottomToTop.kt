package fr.jhelp.tools.utilities.image.mixer.type

/**
 * Image mixing to progress from bottom to top
 */
data object ImageBottomToTop : ImageMixingType()
{
    override fun mix(firstImagePixels: IntArray, secondImagePixels: IntArray, resultImagePixels: IntArray, width: Int, height: Int, percent: Float)
    {
        val y = (height * (1f - percent)).toInt()
        val numberPixelsFirst = y * width
        val numberPixelsSecond = (height - y) * width
        val startPixelFirst = secondImagePixels.size - numberPixelsFirst
        /* Draw the first image visible bottom on result top  */
        System.arraycopy(firstImagePixels, startPixelFirst, resultImagePixels, 0, numberPixelsFirst)
        /* Draw the second image visible top on result bottom */
        System.arraycopy(secondImagePixels, 0, resultImagePixels, numberPixelsFirst, numberPixelsSecond)
    }
}