package fr.jhelp.tools.engine3d.animation.change

import fr.jhelp.tools.mathformal.FunctionFormal
import fr.jhelp.tools.mathformal.dsl.T
import fr.jhelp.tools.mathformal.simplifier.simplified
import fr.jhelp.tools.mathformal.variables

/**
 * Indicates that a value depends on function of [T]
 * @property function Function that depends on [T]
 * @property tStart Start value of [T]
 * @property tEnd End value of [T]
 * @throws IllegalArgumentException if [function] depends on other variables than [T]
 */
class ValueFollowFunctionOfT(function: FunctionFormal<*>,
                             private val tStart: Float, private val tEnd: Float) : ValueChange
{
    val function = checkDependsOnT(function)

    internal operator fun invoke(percent: Float): Float =
        this.tStart * (1f - percent) + this.tEnd * percent

    private fun checkDependsOnT(function: FunctionFormal<*>): FunctionFormal<*>
    {
        val simplified = function.simplified
        val variables = simplified.variables()

        if (variables.size > 1)
        {
            throw IllegalArgumentException("Function must depends only of `t` that is not the case of $function")
        }

        if (variables.size > 0 && variables[0] != T)
        {
            throw IllegalArgumentException("Function must depends only of `t` that is not the case of $function")
        }

        return simplified
    }
}