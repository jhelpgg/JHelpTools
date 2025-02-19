package fr.jhelp.tools.mathformal.simplifier

import fr.jhelp.tools.mathformal.ConstantFormal
import fr.jhelp.tools.mathformal.CosineFormal
import fr.jhelp.tools.mathformal.FunctionFormal
import fr.jhelp.tools.mathformal.UnaryMinusFormal
import fr.jhelp.tools.mathformal.dsl.constant
import fr.jhelp.tools.mathformal.dsl.cos
import kotlin.math.cos

internal fun simplifyCosine(cosine: CosineFormal): FunctionFormal<*>
{
    val parameter = cosine.parameter

    return when (parameter)
    {
        is ConstantFormal   -> constant(cos(parameter.value))
        is UnaryMinusFormal -> cos(simplifyFormal(parameter.parameter))
        else                -> cos(simplifyFormal(parameter))
    }
}