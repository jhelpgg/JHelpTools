package fr.jhelp.tools.mathformal.parser

import fr.jhelp.tools.mathformal.BinaryOperatorFormal
import fr.jhelp.tools.mathformal.dsl.minus
import fr.jhelp.tools.mathformal.dsl.plus
import java.util.Optional

// Order in priority (most priority first)
// Don't forget to complete last when in parseBinary
private val operatorsSymbols = charArrayOf('+', '-')

internal fun parseBinary(string: String): Optional<BinaryOperatorFormal<*>>
{
    val (index, symbol) = indexAndSymbol(string)

    if (index <= 0)
    {
        return Optional.empty()
    }

    var parameter1 = string.substring(0, index)
    var parameter2 = string.substring(index + 1)

    if (parameter1[0] == '(')
    {
        parameter1 = extractInsideParenthesis(parameter1, 0).first
    }

    if (parameter2[0] == '(')
    {
        parameter2 = extractInsideParenthesis(parameter2, 0).first
    }

    return when (symbol)
    {
        '+'  -> Optional.of(parseFormal(parameter1) + parseFormal(parameter2))
        '-'  -> Optional.of(parseFormal(parameter1) - parseFormal(parameter2))
        else -> Optional.empty()
    }
}

private fun indexAndSymbol(string: String): Pair<Int, Char>
{
    var index = -1
    var symbol = ' '
    var indexSymbol = -1
    var parenthesis = 0
    val characters = string.toCharArray()

    for ((indexCharacter, character) in characters.withIndex())
    {
        when (character)
        {
            '('  -> parenthesis++
            ')'  -> parenthesis--
            else ->
                if (parenthesis == 0)
                {
                    val indexInSymbols = operatorsSymbols.indexOf(character)

                    if (indexInSymbols > indexSymbol)
                    {
                        indexSymbol = indexInSymbols
                        index = indexCharacter
                        symbol = character
                    }
                }
        }
    }

    return index to symbol
}