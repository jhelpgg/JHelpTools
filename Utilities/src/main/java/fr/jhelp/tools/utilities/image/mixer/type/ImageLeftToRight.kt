package fr.jhelp.tools.utilities.image.mixer.type

/**
 * Image mixing to progress from left to right
 */
data object ImageLeftToRight : ImageMixingType()
{
    override fun mix(firstImagePixels: IntArray, secondImagePixels: IntArray, resultImagePixels: IntArray, width: Int, height: Int, percent: Float)
    {
        val x = (width * percent).toInt()
        val left = width - x
        var pixelFirstLine = 0
        var pixelSecondLine = left
        var pixelStartLine1 = 0
        var pixelStartLine2 = x

        for (y in 0 until height)
        {
            /* Draw first image visible left on result right */
            System.arraycopy(firstImagePixels, pixelFirstLine, resultImagePixels, pixelStartLine2, left)
            /* Draw second image visible right on result left */
            System.arraycopy(secondImagePixels, pixelSecondLine, resultImagePixels, pixelStartLine1, x)
            pixelStartLine1 += width
            pixelStartLine2 += width
            pixelFirstLine += width
            pixelSecondLine += width
        }
    }
}