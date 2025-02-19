package fr.jhelp.tools.mathformal.simplifier

import fr.jhelp.tools.mathformal.ConstantFormal
import fr.jhelp.tools.mathformal.DivisionFormal
import fr.jhelp.tools.mathformal.FunctionFormal
import fr.jhelp.tools.mathformal.MultiplicationFormal
import fr.jhelp.tools.mathformal.UnaryMinusFormal
import fr.jhelp.tools.mathformal.dsl.MINUS_ONE
import fr.jhelp.tools.mathformal.dsl.ONE
import fr.jhelp.tools.mathformal.dsl.ZERO
import fr.jhelp.tools.mathformal.dsl.constant
import fr.jhelp.tools.mathformal.dsl.div
import fr.jhelp.tools.mathformal.dsl.times
import fr.jhelp.tools.mathformal.dsl.unaryMinus
import fr.jhelp.tools.utilities.collections.SortedArray
import java.util.Stack

internal fun simplifyMultiplication(multiplication: MultiplicationFormal): FunctionFormal<*>
{
    val parameter1 = multiplication.parameter1
    val parameter2 = multiplication.parameter2

    return when
    {
        parameter1 == ZERO                                               ->
            ZERO

        parameter2 == ZERO                                               ->
            ZERO

        parameter1 is ConstantFormal && parameter2 is ConstantFormal     ->
            (parameter1.value * parameter2.value).constant

        parameter1 == ONE                                                ->
            simplifyFormal(parameter2)

        parameter2 == ONE                                                ->
            simplifyFormal(parameter1)

        parameter1 == MINUS_ONE                                          ->
            simplifyFormal(-parameter2)

        parameter2 == MINUS_ONE                                          ->
            simplifyFormal(-parameter1)

        parameter1 is UnaryMinusFormal && parameter2 is UnaryMinusFormal ->
            simplifyFormal(parameter1.parameter) * simplifyFormal(parameter2.parameter)

        parameter1 is UnaryMinusFormal                                   ->
            -(simplifyFormal(parameter1.parameter) * simplifyFormal(parameter2))

        parameter2 is UnaryMinusFormal                                   ->
            -(simplifyFormal(parameter1) * simplifyFormal(parameter2.parameter))

        parameter1 is DivisionFormal && parameter2 is DivisionFormal     ->
            simplifyMultiplication(parameter1, parameter2)

        parameter1 is DivisionFormal                                     ->
            simplifyMultiplication(parameter2, parameter1)

        parameter2 is DivisionFormal                                     ->
            simplifyMultiplication(parameter1, parameter2)

        else                                                             ->
            simplifyMultipleMultiplications(parameter1, parameter2)
    }
}

private fun simplifyMultiplication(function: FunctionFormal<*>, division: DivisionFormal): FunctionFormal<*> =
    simplifyFormal(function * division.parameter1) / simplifyFormal(division.parameter2)

private fun simplifyMultiplication(division1: DivisionFormal, division2: DivisionFormal): FunctionFormal<*> =
    simplifyFormal(division1.parameter1 * division2.parameter1) / simplifyFormal(division1.parameter2 * division2.parameter2)

private fun simplifyMultipleMultiplications(parameter1: FunctionFormal<*>, parameter2: FunctionFormal<*>): FunctionFormal<*>
{
    val collect = SortedArray<FunctionFormal<*>>()
    val stack = Stack<FunctionFormal<*>>()
    stack.push(parameter1)
    stack.push(parameter2)

    while (stack.isNotEmpty())
    {
        val function = stack.pop()

        if (function is MultiplicationFormal)
        {
            stack.push(function.parameter1)
            stack.push(function.parameter2)
        }
        else
        {
            collect.add(function)
        }
    }
    var result: FunctionFormal<*> = ONE

    for (function in collect)
    {
        result = when
        {
            result == ONE                                          ->
                simplifyFormal(function)

            result is ConstantFormal && function is ConstantFormal ->
                (result.value * function.value).constant

            else                                                   ->
                result * simplifyFormal(function)
        }
    }

    return result
}