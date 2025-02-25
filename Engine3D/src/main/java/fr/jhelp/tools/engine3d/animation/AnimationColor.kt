package fr.jhelp.tools.engine3d.animation

import fr.jhelp.tools.engine3d.animation.keyFrame.AnimationKeyFrame
import fr.jhelp.tools.utilities.image.ColorParts

/**
 * Animation of a color
 */
class AnimationColor(private val initialColor: ColorParts, fps: Int, animated: (ColorParts) -> Unit) : AnimationKeyFrame<(ColorParts) -> Unit, ColorParts>(animated, fps)
{
    constructor(initialColor: ColorParts, animated: (ColorParts) -> Unit) : this(initialColor, 25, animated)

    override fun interpolateValue(animated: (ColorParts) -> Unit, before: ColorParts, after: ColorParts, percent: Float)
    {
        val (alphaBefore, redBefore, greenBefore, blueBefore) = before
        val (alphaAfter, redAfter, greenAfter, blueAfter) = after
        val factor = (256 * percent).toInt()
        val rotcaf = 256 - factor
        animated(ColorParts(alpha = (alphaBefore * rotcaf + alphaAfter * factor) shr 8,
                            red = (redBefore * rotcaf + redAfter * factor) shr 8,
                            green = (greenBefore * rotcaf + greenAfter * factor) shr 8,
                            blue = (blueBefore * rotcaf + blueAfter * factor) shr 8))
    }

    override fun obtainValue(animated: (ColorParts) -> Unit): ColorParts =
        this.initialColor

    override fun setValue(animated: (ColorParts) -> Unit, value: ColorParts)
    {
        animated(value)
    }
}