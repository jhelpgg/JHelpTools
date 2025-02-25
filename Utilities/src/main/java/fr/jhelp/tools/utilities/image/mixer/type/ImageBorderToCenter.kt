package fr.jhelp.tools.utilities.image.mixer.type

/**
 * Image mixing to progress from border to center
 */
data object ImageBorderToCenter : ImageMixingType()
{
    override fun mix(firstImagePixels: IntArray, secondImagePixels: IntArray, resultImagePixels: IntArray, width: Int, height: Int, percent: Float)
    {
        /* Draw the second image in background */
        System.arraycopy(secondImagePixels, 0, resultImagePixels, 0, secondImagePixels.size)

        /* Draw the visible rectangle of first image */
        val anti = 1f - percent
        val w = (width * anti).toInt()
        val h = (height * anti).toInt()
        val x = (width - w) / 2
        val y = (height - h) / 2
        var pixelStartLine = x + y * width

        for (yy in 0 until h)
        {
            System.arraycopy(firstImagePixels, pixelStartLine, resultImagePixels, pixelStartLine, w)
            pixelStartLine += width
        }
    }
}