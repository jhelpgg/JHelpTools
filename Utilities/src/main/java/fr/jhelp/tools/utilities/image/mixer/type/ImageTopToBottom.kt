package fr.jhelp.tools.utilities.image.mixer.type

/**
 * Image mixing to progress from top to bottom
 */
data object ImageTopToBottom : ImageMixingType()
{
    override fun mix(firstImagePixels: IntArray, secondImagePixels: IntArray, resultImagePixels: IntArray, width: Int, height: Int, percent: Float)
    {
        val y = (height * percent).toInt()
        val numberPixelsFirst = (height-y)*width
        val numberPixelsSecond = y*width
        val startPixelSecond = secondImagePixels.size - numberPixelsSecond
        /* Draw the first image visible top on result bottom */
        System.arraycopy(firstImagePixels, 0, resultImagePixels, numberPixelsSecond, numberPixelsFirst)
        /* Draw the second image visible bottom on result top  */
        System.arraycopy(secondImagePixels, startPixelSecond, resultImagePixels, 0, numberPixelsSecond)
    }
}