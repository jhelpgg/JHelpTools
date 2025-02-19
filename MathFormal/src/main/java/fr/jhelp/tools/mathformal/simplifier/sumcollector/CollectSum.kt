package fr.jhelp.tools.mathformal.simplifier.sumcollector

import fr.jhelp.tools.mathformal.AdditionFormal
import fr.jhelp.tools.mathformal.FunctionFormal
import fr.jhelp.tools.mathformal.SubtractionFormal
import fr.jhelp.tools.mathformal.dsl.UNDEFINED
import java.util.Stack

/**
 * Try to see the function as a generic sum of positive and negative numbers.
 *
 * We collect all elements of the sum
 *
 * @param functionFormal Function where we want to collect the sum
 * @return List of elements of the sum
 */
internal fun collectSum(functionFormal: FunctionFormal<*>) : MutableList<SumCollectedElement>
{
    val list = ArrayList<SumCollectedElement>()
    val stack = Stack<SumCollectedElement>()
    stack.push(SumCollectedElement(functionFormal, positive=true))

    while(stack.isNotEmpty())
    {
        val collected = stack.pop()
        val function = collected.functionFormal

        when(function)
        {
            is AdditionFormal ->
            {
                stack.push(SumCollectedElement(function.parameter1, collected.positive))
                stack.push(SumCollectedElement(function.parameter2, collected.positive))
            }

            is SubtractionFormal ->
            {
                stack.push(SumCollectedElement(function.parameter1, collected.positive))
                stack.push(SumCollectedElement(function.parameter2, !collected.positive))
            }

            else ->
                list.add(collected)
        }
    }

    return list
}
