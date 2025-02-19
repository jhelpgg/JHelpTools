package fr.jhelp.tools.mathformal.simplifier

import fr.jhelp.tools.mathformal.ConstantFormal
import fr.jhelp.tools.mathformal.FunctionFormal
import fr.jhelp.tools.mathformal.SineFormal
import fr.jhelp.tools.mathformal.UnaryMinusFormal
import fr.jhelp.tools.mathformal.dsl.constant
import fr.jhelp.tools.mathformal.dsl.sin
import fr.jhelp.tools.mathformal.dsl.unaryMinus
import kotlin.math.sin

internal fun simplifySine(sine: SineFormal): FunctionFormal<*>
{
    val parameter = sine.parameter

    return when (parameter)
    {
        is ConstantFormal   -> constant(sin(parameter.value))
        is UnaryMinusFormal -> -sin(simplifyFormal(parameter.parameter))
        else                -> sin(simplifyFormal(parameter))
    }
}