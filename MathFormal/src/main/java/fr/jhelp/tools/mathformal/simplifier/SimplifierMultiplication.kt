package fr.jhelp.tools.mathformal.simplifier

import fr.jhelp.tools.mathformal.ConstantFormal
import fr.jhelp.tools.mathformal.FunctionFormal
import fr.jhelp.tools.mathformal.MultiplicationFormal
import fr.jhelp.tools.mathformal.UnaryMinusFormal
import fr.jhelp.tools.mathformal.dsl.MINUS_ONE
import fr.jhelp.tools.mathformal.dsl.ONE
import fr.jhelp.tools.mathformal.dsl.ZERO
import fr.jhelp.tools.mathformal.dsl.constant
import fr.jhelp.tools.mathformal.dsl.times
import fr.jhelp.tools.mathformal.dsl.unaryMinus

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

        parameter1 is MultiplicationFormal                               ->
            simplifyMultiplication(parameter1, parameter2)

        parameter2 is MultiplicationFormal                               ->
            simplifyMultiplication(parameter2, parameter1)

        else                                                             ->
            simplifyFormal(parameter1) * simplifyFormal(parameter2)
    }
}

private fun simplifyMultiplication(multiplication: MultiplicationFormal, function: FunctionFormal<*>): FunctionFormal<*>
{
    val parameter1 = multiplication.parameter1
    val parameter2 = multiplication.parameter2

    return when
    {
        parameter1 == ZERO                                 ->
            ZERO

        parameter2 == ZERO                                 ->
            ZERO

        parameter1 == ONE                                  ->
            simplifyFormal(parameter2 * function)

        parameter2 == ONE                                  ->
            simplifyFormal(parameter1 * function)

        parameter1 == MINUS_ONE                            ->
            -(simplifyFormal(parameter2 * function))

        parameter2 == MINUS_ONE                            ->
            -(simplifyFormal(parameter1 * function))

        parameter1 is ConstantFormal                       ->
            parameter1 * simplifyFormal(parameter2 * function)

        parameter2 is ConstantFormal                       ->
            parameter2 * simplifyFormal(parameter1 * function)

        function is MultiplicationFormal                   ->
            simplifyMultiplicationOfMultiplication(parameter1, parameter2, function.parameter1, function.parameter2)

        else                                               ->
            simplifyFormal(parameter1) * simplifyFormal(parameter2) * simplifyFormal(function)
    }
}

private fun simplifyMultiplicationOfMultiplication(parameter11: FunctionFormal<*>, parameter12: FunctionFormal<*>,
                                                   parameter21: FunctionFormal<*>, parameter22: FunctionFormal<*>): FunctionFormal<*> =
    when
    {
        parameter11 == ZERO || parameter12 == ZERO || parameter21 == ZERO || parameter22 == ZERO                                         ->
            ZERO

        parameter11 is ConstantFormal && parameter12 is ConstantFormal && parameter21 is ConstantFormal && parameter22 is ConstantFormal ->
            (parameter11.value * parameter12.value * parameter21.value * parameter22.value).constant

        parameter11 is ConstantFormal && parameter12 is ConstantFormal && parameter21 is ConstantFormal                                  ->
            (parameter11.value * parameter12.value * parameter21.value).constant * simplifyFormal(parameter22)

        parameter11 is ConstantFormal && parameter12 is ConstantFormal && parameter22 is ConstantFormal                                  ->
            (parameter11.value * parameter12.value * parameter22.value).constant * simplifyFormal(parameter21)

        parameter11 is ConstantFormal && parameter21 is ConstantFormal && parameter22 is ConstantFormal                                  ->
            (parameter11.value * parameter21.value * parameter22.value).constant * simplifyFormal(parameter12)

        parameter12 is ConstantFormal && parameter21 is ConstantFormal && parameter22 is ConstantFormal                                  ->
            (parameter12.value * parameter21.value * parameter22.value).constant * simplifyFormal(parameter11)

        parameter11 is ConstantFormal && parameter12 is ConstantFormal                                                                   ->
            (parameter11.value * parameter12.value).constant * simplifyFormal(parameter21 * parameter22)

        parameter11 is ConstantFormal && parameter21 is ConstantFormal                                                                   ->
            (parameter11.value * parameter21.value).constant * simplifyFormal(parameter12 * parameter22)

        parameter11 is ConstantFormal && parameter22 is ConstantFormal                                                                   ->
            (parameter11.value * parameter22.value).constant * simplifyFormal(parameter12 * parameter21)

        parameter12 is ConstantFormal && parameter21 is ConstantFormal                                                                   ->
            (parameter12.value * parameter21.value).constant * simplifyFormal(parameter11 * parameter22)

        parameter12 is ConstantFormal && parameter22 is ConstantFormal                                                                   ->
            (parameter12.value * parameter22.value).constant * simplifyFormal(parameter11 * parameter21)

        parameter21 is ConstantFormal && parameter22 is ConstantFormal                                                                   ->
            (parameter21.value * parameter22.value).constant * simplifyFormal(parameter11 * parameter12)

        else                                                                                                                             ->
            simplifyFormal(simplifyFormal(parameter11) * simplifyFormal(parameter22)) * simplifyFormal(simplifyFormal(parameter12) * simplifyFormal(parameter21))
    }