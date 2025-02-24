package fr.jhelp.tools.utilities.image

/**
 * Color parts
 */
data class ColorParts(val alpha: Int, val red: Int, val green: Int, val blue: Int)
{
    /**
     * Constructor from color int
     */
    constructor(color: Int) : this(color.alpha, color.red, color.green, color.blue)
}
