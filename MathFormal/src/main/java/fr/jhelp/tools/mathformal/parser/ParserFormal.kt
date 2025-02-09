package fr.jhelp.tools.mathformal.parser

import fr.jhelp.tools.mathformal.FunctionFormal
import fr.jhelp.tools.mathformal.dsl.UNDEFINED
import fr.jhelp.tools.mathformal.dsl.variable
import fr.jhelp.tools.mathformal.symbol

fun parse(string: String): FunctionFormal<*> =
    parseFormal(addMultiplication(removeSpaces(string)))

internal fun parseFormal(string: String): FunctionFormal<*>
{
    val symbol = symbol(string)

    if (symbol.isPresent)
    {
        return symbol.get()
    }

    val constant = parseConstants(string)

    if (constant.isPresent)
    {
        return constant.get()
    }

    val binary = parseBinary(string)

    if (binary.isPresent)
    {
        return binary.get()
    }

    val unary = parseUnary(string)

    if (unary.isPresent)
    {
        return unary.get()
    }


    return try
    {
        string.variable
    }
    catch (_: Exception)
    {
        UNDEFINED
    }
}