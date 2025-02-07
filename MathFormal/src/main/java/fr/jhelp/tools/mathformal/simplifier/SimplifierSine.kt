package fr.jhelp.tools.mathformal.simplifier

import fr.jhelp.tools.mathformal.ConstantFormal
import fr.jhelp.tools.mathformal.FunctionFormal
import fr.jhelp.tools.mathformal.SineFormal
import fr.jhelp.tools.mathformal.UnaryMinusFormal
import fr.jhelp.tools.mathformal.dsl.UNDEFINED
import fr.jhelp.tools.mathformal.dsl.constant
import kotlin.math.sin

internal fun simplifySine(sine: SineFormal): FunctionFormal<*>
{
    val parameter = sine.parameter

    return when (parameter)
    {
        UNDEFINED           -> UNDEFINED
        is ConstantFormal   -> constant(sin(parameter.value))
        is UnaryMinusFormal -> UnaryMinusFormal(SineFormal(simplifyFormal(parameter.parameter)))
        else                -> SineFormal(simplifyFormal(parameter))
    }
}