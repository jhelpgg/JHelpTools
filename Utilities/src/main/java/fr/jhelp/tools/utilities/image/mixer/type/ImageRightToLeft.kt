package fr.jhelp.tools.utilities.image.mixer.type

/**
 * Image mixing to progress from right to left
 */
data object ImageRightToLeft : ImageMixingType()
{
    override fun mix(firstImagePixels: IntArray, secondImagePixels: IntArray, resultImagePixels: IntArray, width: Int, height: Int, percent: Float)
    {
        val x = (width * (1f - percent)).toInt()
        val left = width - x
        var pixelFirstLine = left
        var pixelSecondLine = 0
        var pixelResultLine1 = 0
        var pixelResultLine2 = x

        for (y in 0 until height)
        {
            /* Draw first image visible right on result left */
            System.arraycopy(firstImagePixels, pixelFirstLine, resultImagePixels, pixelResultLine1, x)
            /* Draw second image visible left on result right */
            System.arraycopy(secondImagePixels, pixelSecondLine, resultImagePixels, pixelResultLine2, left)
            pixelResultLine1 += width
            pixelResultLine2 += width
            pixelFirstLine += width
            pixelSecondLine += width
        }
    }
}