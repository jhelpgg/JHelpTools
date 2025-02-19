package fr.jhelp.tools.mathformal.serializer

import fr.jhelp.tools.mathformal.BinaryOperatorFormal
import fr.jhelp.tools.mathformal.UnaryOperatorFormal

internal fun serializeUnaryOperator(unaryOperatorFormal: UnaryOperatorFormal<*>): String
{
    val stringBuilder = StringBuilder()
    stringBuilder.append(unaryOperatorFormal.symbol)
    val parameter = unaryOperatorFormal.parameter
    val needParenthesis = unaryOperatorFormal.alwaysParenthesis || parameter is BinaryOperatorFormal<*>

    if (needParenthesis)
    {
        stringBuilder.append('(')
    }

    stringBuilder.append(serializeFormal(unaryOperatorFormal.parameter))

    if (needParenthesis)
    {
        stringBuilder.append(')')
    }

    return stringBuilder.toString()
}