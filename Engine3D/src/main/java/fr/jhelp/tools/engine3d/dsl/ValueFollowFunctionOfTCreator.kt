package fr.jhelp.tools.engine3d.dsl

import fr.jhelp.tools.engine3d.annotations.ValueFollowFunctionOfT
import fr.jhelp.tools.mathformal.FunctionFormal
import fr.jhelp.tools.mathformal.dsl.T
import fr.jhelp.tools.mathformal.simplifier.simplified
import fr.jhelp.tools.mathformal.variables

/**
 * Create a value follow a function of T
 */
@ValueFollowFunctionOfT
class ValueFollowFunctionOfTCreator
{
    /**
     * Function of T
     */
    @ValueFollowFunctionOfT
    var functionOfT: FunctionFormal<*> = T
        set(value)
        {
            field = checkDependsOnT(value)
        }

    /**
     * Start value
     */
    @ValueFollowFunctionOfT
    var tStart: Float = 0f

    /**
     * End value
     */
    @ValueFollowFunctionOfT
    var tEnd: Float = 1f

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