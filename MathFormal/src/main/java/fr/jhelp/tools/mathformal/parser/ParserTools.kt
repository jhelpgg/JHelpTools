package fr.jhelp.tools.mathformal.parser

import fr.jhelp.tools.utilities.checkArgument
import java.util.regex.Pattern

fun removeSpaces(string: String): String
{
    val chars = string.toCharArray()
    val result = CharArray(chars.size)
    var index = 0

    for (char in chars)
    {
        if (char > ' ')
        {
            result[index] = char
            index++
        }
    }

    return String(result, 0, index)
}

fun extractInsideParenthesis(string: String, firstOpenParenthesisIndex: Int): Pair<String, Int>
{
    (string[firstOpenParenthesisIndex] == '(').checkArgument("first index must be a (")

    var openParenthesis = 1
    var index = firstOpenParenthesisIndex + 1

    while (openParenthesis > 0 && index < string.length)
    {
        when (string[index])
        {
            '(' -> openParenthesis++
            ')' ->
            {
                openParenthesis--

                if (openParenthesis <= 0)
                {
                    break
                }
            }
        }

        index++
    }

    if (openParenthesis > 0)
    {
        throw IllegalArgumentException("No closing parenthesis")
    }

    return string.substring(firstOpenParenthesisIndex + 1, index) to index
}

private val endConstantStartVariablePattern = Pattern.compile("([0-9])([a-zA-Z])")
private const val endConstantStartVariableReplacement = "$1*$2"

internal fun addMultiplication(string: String): String =
    endConstantStartVariablePattern.matcher(string).replaceAll(endConstantStartVariableReplacement)
