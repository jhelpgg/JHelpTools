package fr.jhelp.tools.mathformal.simplifier

import fr.jhelp.tools.mathformal.ConstantFormal
import fr.jhelp.tools.mathformal.DivisionFormal
import fr.jhelp.tools.mathformal.FunctionFormal
import fr.jhelp.tools.mathformal.MultiplicationFormal
import fr.jhelp.tools.mathformal.UnaryMinusFormal
import fr.jhelp.tools.mathformal.dsl.MINUS_ONE
import fr.jhelp.tools.mathformal.dsl.ONE
import fr.jhelp.tools.mathformal.dsl.UNDEFINED
import fr.jhelp.tools.mathformal.dsl.ZERO
import fr.jhelp.tools.mathformal.dsl.constant
import fr.jhelp.tools.mathformal.dsl.div
import fr.jhelp.tools.mathformal.dsl.times
import fr.jhelp.tools.mathformal.dsl.unaryMinus
import fr.jhelp.tools.utilities.collections.SortedArray

internal fun simplifyDivision(division: DivisionFormal): FunctionFormal<*>
{
    val dividend = division.parameter1
    val divisor = division.parameter2

    return when
    {
        divisor == ZERO                                             -> UNDEFINED
        dividend == ZERO                                            -> ZERO
        divisor == ONE                                              -> simplifyFormal(dividend)
        divisor == MINUS_ONE                                        -> simplifyFormal(-dividend)
        dividend == divisor                                         -> ONE

        dividend is ConstantFormal && divisor is ConstantFormal     ->
            (dividend.value / divisor.value).constant

        dividend is UnaryMinusFormal && divisor is UnaryMinusFormal ->
            simplifyFormal(dividend.parameter) / simplifyFormal(divisor.parameter)

        dividend is UnaryMinusFormal                                ->
            when (divisor)
            {
                dividend.parameter -> MINUS_ONE
                else               -> simplifyFormal(dividend) / simplifyFormal(divisor)
            }

        divisor is UnaryMinusFormal                                 ->
            when (dividend)
            {
                divisor.parameter -> MINUS_ONE
                else              -> simplifyFormal(-dividend) / simplifyFormal(divisor.parameter)
            }

        else                                                        ->
            simplifyDivisionDeep(dividend, divisor)
    }
}

private fun simplifyDivisionDeep(dividend: FunctionFormal<*>, divisor: FunctionFormal<*>): FunctionFormal<*>
{
    // Initialization
    val dividendTerms = SortedArray<FunctionFormal<*>>()
    val divisorTerms = SortedArray<FunctionFormal<*>>()
    dividendTerms.add(dividend)
    divisorTerms.add(divisor)

    var loop: Boolean

    do
    {
        loop = false

        // Try simplify dividends
        for (dividendIndex in dividendTerms.size - 1 downTo 0)
        {
            val function = dividendTerms[dividendIndex]
            var indexInDivisors = divisorTerms.indexOf(function)

            if (indexInDivisors >= 0)
            {
                dividendTerms.removeAt(dividendIndex)
                divisorTerms.removeAt(indexInDivisors)
                break
            }

            if (function is UnaryMinusFormal)
            {
                indexInDivisors = divisorTerms.indexOf(function.parameter)
                if (indexInDivisors >= 0)
                {
                    dividendTerms.removeAt(dividendIndex)
                    dividendTerms.add(MINUS_ONE)
                    divisorTerms.removeAt(indexInDivisors)
                    break
                }
            }

            when (function)
            {
                is MultiplicationFormal ->
                {
                    dividendTerms.removeAt(dividendIndex)
                    dividendTerms.add(function.parameter1)
                    dividendTerms.add(function.parameter2)
                    loop = true
                    break
                }

                is DivisionFormal       ->
                {
                    dividendTerms.removeAt(dividendIndex)
                    dividendTerms.add(function.parameter1)
                    divisorTerms.add(function.parameter2)
                    loop = true
                    break
                }

                else                    -> Unit
            }
        }

        // Try simplify divisor
        for (divisorIndex in divisorTerms.size - 1 downTo 0)
        {
            val function = divisorTerms[divisorIndex]
            var indexInDividends = dividendTerms.indexOf(function)

            if (indexInDividends >= 0)
            {
                dividendTerms.removeAt(indexInDividends)
                divisorTerms.removeAt(divisorIndex)
                break
            }

            if (function is UnaryMinusFormal)
            {
                indexInDividends = dividendTerms.indexOf(function.parameter)

                if (indexInDividends >= 0)
                {
                    divisorTerms.removeAt(divisorIndex)
                    divisorTerms.add(MINUS_ONE)
                    dividendTerms.removeAt(indexInDividends)
                    break
                }
            }

            when (function)
            {
                is MultiplicationFormal ->
                {
                    divisorTerms.removeAt(divisorIndex)
                    divisorTerms.add(function.parameter1)
                    divisorTerms.add(function.parameter2)
                    loop = true
                    break
                }

                is DivisionFormal       ->
                {
                    divisorTerms.removeAt(divisorIndex)
                    divisorTerms.add(function.parameter1)
                    dividendTerms.add(function.parameter2)
                    loop = true
                    break
                }

                else                    -> Unit
            }
        }
    }
    while (loop)

    // Simplify constants
    while (dividendTerms.notEmpty && divisorTerms.notEmpty)
    {
        val dividendTerm = dividendTerms[0]
        val divisorTerm = divisorTerms[0]

        when
        {
            dividendTerm is ConstantFormal && divisorTerm is ConstantFormal ->
            {
                dividendTerms.removeAt(0)
                divisorTerms.removeAt(0)
                dividendTerms.add((dividendTerm.value / divisorTerm.value).constant)
            }

            else                                                            -> break
        }
    }

    // Collect result
    var dividendResult: FunctionFormal<*> = ONE
    var divisorResult: FunctionFormal<*> = ONE

    for (function in dividendTerms)
    {
        when
        {
            dividendResult == ONE                                          ->
                dividendResult = simplifyFormal(function)

            dividendResult is ConstantFormal && function is ConstantFormal ->
                dividendResult = (dividendResult.value * function.value).constant

            else                                                           ->
                dividendResult *= simplifyFormal(function)
        }
    }

    for (function in divisorTerms)
    {
        when
        {
            divisorResult == ONE                                          ->
                divisorResult = simplifyFormal(function)

            divisorResult is ConstantFormal && function is ConstantFormal ->
                divisorResult = (divisorResult.value * function.value).constant

            else                                                          ->
                divisorResult *= simplifyFormal(function)
        }
    }


    if (divisorResult == ONE)
    {
        return simplifyFormal(dividendResult)
    }

    return simplifyFormal(dividendResult) / simplifyFormal(divisorResult)
}