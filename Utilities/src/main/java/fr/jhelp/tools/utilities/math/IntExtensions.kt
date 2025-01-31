package fr.jhelp.tools.utilities.math

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/**
 * Limit an integer in given interval
 */
fun Int.bounds(value1: Int, value2: Int): Int =
    max(min(value1, value2), min(this, max(value1, value2)))

/**
 * Apply an alpha to a color
 */
fun Int.useAlpha(colorAlpha: Int): Int = (this and COLOR_MASK) or colorAlpha

/**
 * Greatest Common Divider
 */
infix fun Int.GCD(integer: Int): Int
{
    var minimum = min(abs(this), abs(integer))
    var maximum = max(abs(this), abs(integer))
    var temporary: Int

    while (minimum > 0)
    {
        temporary = minimum
        minimum = maximum % minimum
        maximum = temporary
    }

    return maximum
}

/**
 * Lowest Common Multiple
 */
infix fun Int.LCM(integer: Int): Int
{
    val gcd = this GCD integer

    if (gcd == 0)
    {
        return 0
    }

    return this * (integer / gcd)
}

