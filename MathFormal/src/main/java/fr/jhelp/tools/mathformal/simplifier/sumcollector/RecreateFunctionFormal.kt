package fr.jhelp.tools.mathformal.simplifier.sumcollector

import fr.jhelp.tools.mathformal.FunctionFormal
import fr.jhelp.tools.mathformal.dsl.UNDEFINED
import fr.jhelp.tools.mathformal.dsl.ZERO
import fr.jhelp.tools.mathformal.dsl.minus
import fr.jhelp.tools.mathformal.dsl.plus
import fr.jhelp.tools.mathformal.dsl.unaryMinus

/**
 * Recreate a function from a generic sum
 * @param sumCollected the generic sum
 * @return the function
 */
internal fun recreateFunctionFormal(sumCollected: List<SumCollectedElement>): FunctionFormal<*>
{
    var result: FunctionFormal<*> = ZERO

    for (sum in sumCollected)
    {
        result = when
        {
            sum.functionFormal == UNDEFINED -> return UNDEFINED
            sum.functionFormal == ZERO      -> result

            result == ZERO                  ->
                when
                {
                    sum.positive -> sum.functionFormal
                    else         -> -sum.functionFormal
                }

            else                            ->
                when
                {
                    sum.positive -> result + sum.functionFormal
                    else         -> result - sum.functionFormal
                }
        }
    }

    return result
}