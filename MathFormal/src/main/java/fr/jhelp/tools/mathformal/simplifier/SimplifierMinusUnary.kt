package fr.jhelp.tools.mathformal.simplifier

import fr.jhelp.tools.mathformal.ConstantFormal
import fr.jhelp.tools.mathformal.FunctionFormal
import fr.jhelp.tools.mathformal.UnaryMinusFormal
import fr.jhelp.tools.mathformal.dsl.MINUS_ONE
import fr.jhelp.tools.mathformal.dsl.ONE
import fr.jhelp.tools.mathformal.dsl.UNDEFINED
import fr.jhelp.tools.mathformal.dsl.ZERO
import fr.jhelp.tools.mathformal.dsl.constant
import fr.jhelp.tools.mathformal.dsl.unaryMinus

internal fun simplifyMinusUnary(minusUnary: UnaryMinusFormal): FunctionFormal<*>
{
    val parameter = minusUnary.parameter

    return when (parameter)
    {
        UNDEFINED           -> UNDEFINED
        ZERO                -> ZERO
        ONE                 -> MINUS_ONE
        MINUS_ONE           -> ONE
        is ConstantFormal   -> constant(-parameter.value)
        is UnaryMinusFormal -> simplifyFormal(parameter.parameter)
        else                -> -simplifyFormal(parameter)
    }
}