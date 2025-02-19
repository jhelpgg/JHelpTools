package fr.jhelp.tools.mathformal.simplifier

import fr.jhelp.tools.mathformal.*
import fr.jhelp.tools.mathformal.dsl.*
import java.util.Stack

/**
 * Detect if [UNDEFINED] is somewhere.
 *
 * If one [UNDEFINED] is find, [UNDEFINED] is returned.
 *
 * If no [UNDEFINED] found, [function] is returned.
 *
 * @param function The function to simplify.
 * @return The simplified function.
 */
internal fun undefinedSimplifier(function:FunctionFormal<*>) : FunctionFormal<*>
{
    val stack = Stack<FunctionFormal<*>>()
    stack.push(function)

    while (stack.isNotEmpty())
    {
        val functionTested = stack.pop()

        when (functionTested)
        {
            UNDEFINED -> return UNDEFINED
            is UnaryOperatorFormal<*> -> stack.push(functionTested.parameter)
            is BinaryOperatorFormal<*> ->
            {
                stack.push(functionTested.parameter1)
                stack.push(functionTested.parameter2)
            }
            else -> Unit
        }
    }

    return function
}