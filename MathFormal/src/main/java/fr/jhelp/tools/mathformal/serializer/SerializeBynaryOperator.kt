package fr.jhelp.tools.mathformal.serializer

import fr.jhelp.tools.mathformal.BinaryOperatorFormal
import java.util.regex.Pattern

private val constantMultiplyVariablePattern = Pattern.compile("([0-9]+)\\s*\\*\\s*([a-zA-Z])")
private const val constantMultiplyVariableReplacement = "$1$2"

fun serializeBinaryOperator(binaryOperatorFormal: BinaryOperatorFormal<*>): String
{
    val stringBuilder = StringBuilder()
    val parameter1 = binaryOperatorFormal.parameter1
    val parameter2 = binaryOperatorFormal.parameter2
    val needParenthesis1 = parameter1 is BinaryOperatorFormal<*>
    val needParenthesis2 = parameter2 is BinaryOperatorFormal<*>

    if (needParenthesis1)
    {
        stringBuilder.append('(')
    }

    stringBuilder.append(serializeFormal(parameter1))

    if (needParenthesis1)
    {
        stringBuilder.append(')')
    }

    stringBuilder.append(' ')
    stringBuilder.append(binaryOperatorFormal.symbol)
    stringBuilder.append(' ')

    if (needParenthesis2)
    {
        stringBuilder.append('(')
    }

    stringBuilder.append(serializeFormal(parameter2))

    if (needParenthesis2)
    {
        stringBuilder.append(')')
    }

    return constantMultiplyVariablePattern.matcher(stringBuilder.toString()).replaceAll(constantMultiplyVariableReplacement)
}

