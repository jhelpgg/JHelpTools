package fr.jhelp.tools.utilities.image.mixer.type

import fr.jhelp.tools.utilities.image.alpha
import fr.jhelp.tools.utilities.image.blue
import fr.jhelp.tools.utilities.image.green
import fr.jhelp.tools.utilities.image.red

/**
 * Image mixing to merge two images
 */
data object ImageMerge : ImageMixingType()
{
    override fun mix(firstImagePixels: IntArray, secondImagePixels: IntArray, resultImagePixels: IntArray, width: Int, height: Int, percent: Float)
    {
        /*
            For performance reason, we use value between [0, 256] integers instead of floats.
            The value will be multiplied by 256. Qo have to divide by 256.
            But 256 = 2 ^ 8, so move divide by 256 is same as shift bits for 8 rank to the right
        */
        val factor = (256 * percent).toInt()
        val rotcaf = 256 - factor
        var firstColor: Int
        var secondColor: Int
        var alpha: Int
        var red: Int
        var green: Int
        var blue: Int

        /* For each pixels compute the merging */
        for (index in firstImagePixels.indices)
        {
            firstColor = firstImagePixels[index]
            secondColor = secondImagePixels[index]
            alpha = (firstColor.alpha * rotcaf + secondColor.alpha * factor) shr 8
            red = (firstColor.red * rotcaf + secondColor.red * factor) shr 8
            green = (firstColor.green * rotcaf + secondColor.green * factor) shr 8
            blue = (firstColor.blue * rotcaf + secondColor.blue * factor) shr 8

            resultImagePixels[index] = (alpha shl 24) or (red shl 16) or (green shl 8) or blue
        }
    }
}