package fr.jhelp.tools.mathformal.parser

import fr.jhelp.tools.mathformal.FunctionFormal
import fr.jhelp.tools.mathformal.dsl.UNDEFINED
import fr.jhelp.tools.mathformal.dsl.variable
import fr.jhelp.tools.mathformal.symbol

fun parseFormal(string: String): FunctionFormal<*>
{
    val stringTrim = removeSpaces(string)

    val symbol = symbol(stringTrim)

    if (symbol.isPresent)
    {
        return symbol.get()
    }

    val constant = parseConstants(stringTrim)

    if (constant.isPresent)
    {
        return constant.get()
    }

    val binary = parseBinary(stringTrim)

    if (binary.isPresent)
    {
        return binary.get()
    }

    val unary = parseUnary(stringTrim)

    if (unary.isPresent)
    {
        return unary.get()
    }


    return try
    {
        stringTrim.variable
    }
    catch (_: Exception)
    {
        UNDEFINED
    }
}