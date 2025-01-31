package fr.jhelp.tools.utilities.extensions

/**
 * Try parse a string to an integer.
 * It returns the default value if fails
 */
fun String?.int(defaultValue: Int = 0): Int =
    try
    {
        this?.toInt() ?: defaultValue
    }
    catch (_: NumberFormatException)
    {
        defaultValue
    }

/**
 * Try parse a string to an integer.
 * It returns the default value if fails
 */
fun String?.long(defaultValue: Long = 0L): Long =
    try
    {
        this?.toLong() ?: defaultValue
    }
    catch (_: NumberFormatException)
    {
        defaultValue
    }
