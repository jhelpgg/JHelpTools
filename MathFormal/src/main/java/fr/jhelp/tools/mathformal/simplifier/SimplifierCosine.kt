package fr.jhelp.tools.mathformal.simplifier

import fr.jhelp.tools.mathformal.ConstantFormal
import fr.jhelp.tools.mathformal.CosineFormal
import fr.jhelp.tools.mathformal.FunctionFormal
import fr.jhelp.tools.mathformal.UnaryMinusFormal
import fr.jhelp.tools.mathformal.dsl.UNDEFINED
import fr.jhelp.tools.mathformal.dsl.constant
import kotlin.math.cos

internal fun simplifyCosine(cosine: CosineFormal): FunctionFormal<*>
{
    val parameter = cosine.parameter

    return when (parameter)
    {
        UNDEFINED -> UNDEFINED
        is ConstantFormal -> constant(cos(parameter.value))
        is UnaryMinusFormal -> CosineFormal(simplifyFormal(parameter.parameter))
        else -> CosineFormal(simplifyFormal(parameter))
    }
}