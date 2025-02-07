package fr.jhelp.tools.mathformal.parser

import fr.jhelp.tools.mathformal.ConstantFormal
import fr.jhelp.tools.mathformal.dsl.constant
import java.util.Optional

internal fun parseConstants(string: String): Optional<ConstantFormal> =
    try
    {
        val value = string.toDouble()
        Optional.of(constant(value))
    }
    catch (_: NumberFormatException)
    {
        Optional.empty()
    }