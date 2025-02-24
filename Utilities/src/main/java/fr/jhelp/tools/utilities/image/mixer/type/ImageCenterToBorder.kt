package fr.jhelp.tools.utilities.image.mixer.type

/**
 * Image mixing to progress from center to border
 */
data object ImageCenterToBorder : ImageMixingType()
{
    override fun mix(firstImagePixels: IntArray, secondImagePixels: IntArray, resultImagePixels: IntArray, width: Int, height: Int, percent: Float)
    {
        /* Draw the first image in background */
        System.arraycopy(firstImagePixels, 0, resultImagePixels, 0, firstImagePixels.size)

        /* Draw visible rectangle of second image */
        val w = (width * percent).toInt()
        val h = (height * percent).toInt()
        val x = (width - w) / 2
        val y = (height - h) / 2
        var pixelStartLine = x + y * width

        for (yy in 0 until h)
        {
            System.arraycopy(secondImagePixels, pixelStartLine, resultImagePixels, pixelStartLine, w)
            pixelStartLine += width
        }
    }
}