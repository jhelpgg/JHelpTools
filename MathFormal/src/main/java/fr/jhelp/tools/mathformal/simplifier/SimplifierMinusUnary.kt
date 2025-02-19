package fr.jhelp.tools.mathformal.simplifier

import fr.jhelp.tools.mathformal.ConstantFormal
import fr.jhelp.tools.mathformal.DivisionFormal
import fr.jhelp.tools.mathformal.FunctionFormal
import fr.jhelp.tools.mathformal.MultiplicationFormal
import fr.jhelp.tools.mathformal.UnaryMinusFormal
import fr.jhelp.tools.mathformal.dsl.MINUS_ONE
import fr.jhelp.tools.mathformal.dsl.ONE
import fr.jhelp.tools.mathformal.dsl.ZERO
import fr.jhelp.tools.mathformal.dsl.*
import fr.jhelp.tools.mathformal.dsl.unaryMinus

internal fun simplifyMinusUnary(minusUnary: UnaryMinusFormal): FunctionFormal<*>
{
    val parameter = minusUnary.parameter

    return when (parameter)
    {
        ZERO                    -> ZERO
        ONE                     -> MINUS_ONE
        MINUS_ONE               -> ONE
        is ConstantFormal       -> constant(-parameter.value)
        is UnaryMinusFormal     -> simplifyFormal(parameter.parameter)
        is MultiplicationFormal -> simplifyMinusMultiplication(parameter)
        is DivisionFormal       -> simplifyMinusDivision(parameter)
        else                    -> -simplifyFormal(parameter)
    }
}

private fun simplifyMinusMultiplication(multiplication: MultiplicationFormal): FunctionFormal<*>
{
    val parameter1 = multiplication.parameter1
    val parameter2 = multiplication.parameter2

    return when
    {
        parameter1 == ZERO || parameter2 == ZERO -> ZERO
        parameter1 == ONE                        ->  -simplifyFormal(parameter2)
        parameter2 == ONE                        ->  -simplifyFormal(parameter1)
        parameter1 == MINUS_ONE                  ->  simplifyFormal(parameter2)
        parameter2 == MINUS_ONE                  ->  simplifyFormal(parameter1)
        parameter1 is ConstantFormal && parameter2 is ConstantFormal ->
            (-parameter1.value * parameter2.value).constant
        parameter1 is ConstantFormal ->
            (-parameter1.value).constant * simplifyFormal(parameter2)
        parameter2 is ConstantFormal ->
            (-parameter2.value).constant * simplifyFormal(parameter1)
        parameter1 is UnaryMinusFormal ->
            simplifyFormal(parameter1.parameter) * simplifyFormal(parameter2)
        parameter2 is UnaryMinusFormal ->
            simplifyFormal(parameter1) * simplifyFormal(parameter2.parameter)
        else ->
            -(simplifyFormal(parameter1) * simplifyFormal(parameter2))
    }
}

private fun simplifyMinusDivision(multiplication: DivisionFormal): FunctionFormal<*>
{
    val parameter1 = multiplication.parameter1
    val parameter2 = multiplication.parameter2

    return when
    {
        parameter1 == ZERO  -> ZERO
        parameter2 == ZERO -> UNDEFINED
        parameter2 == ONE   -> -simplifyFormal(parameter1)
        parameter2 == MINUS_ONE -> simplifyFormal(parameter1)
        parameter1 is ConstantFormal && parameter2 is ConstantFormal ->
            (-parameter1.value / parameter2.value).constant
        parameter1 is ConstantFormal ->
            (-parameter1.value).constant / simplifyFormal(parameter2)
        parameter2 is ConstantFormal ->
            simplifyFormal(parameter1) / (-parameter2.value).constant
        parameter1 is UnaryMinusFormal ->
            simplifyFormal(parameter1.parameter) / simplifyFormal(parameter2)
        parameter2 is UnaryMinusFormal ->
            simplifyFormal(parameter1) / simplifyFormal(parameter2.parameter)
        else ->
            -(simplifyFormal(parameter1) / simplifyFormal(parameter2))
    }
}