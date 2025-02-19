package fr.jhelp.tools.mathformal

import fr.jhelp.tools.mathformal.dsl.cos
import fr.jhelp.tools.mathformal.dsl.div
import fr.jhelp.tools.mathformal.dsl.minus
import fr.jhelp.tools.mathformal.dsl.plus
import fr.jhelp.tools.mathformal.dsl.sin
import fr.jhelp.tools.mathformal.dsl.times
import fr.jhelp.tools.mathformal.dsl.unaryMinus
import fr.jhelp.tools.utilities.collections.SortedArray
import java.util.Stack

fun FunctionFormal<*>.replaceAll(functionToReplace: FunctionFormal<*>, replacement: FunctionFormal<*>): FunctionFormal<*> =
    when (this)
    {
        functionToReplace                    -> replacement

        is ConstantFormal, is VariableFormal -> this

        is UnaryMinusFormal                  ->
            -this.parameter.replaceAll(functionToReplace, replacement)

        is CosineFormal                      ->
            cos(this.parameter.replaceAll(functionToReplace, replacement))

        is SineFormal                        ->
            sin(this.parameter.replaceAll(functionToReplace, replacement))

        is AdditionFormal                    ->
            this.parameter1.replaceAll(functionToReplace, replacement) + this.parameter2.replaceAll(functionToReplace, replacement)

        is SubtractionFormal                 ->
            this.parameter1.replaceAll(functionToReplace, replacement) - this.parameter2.replaceAll(functionToReplace, replacement)

        is MultiplicationFormal              ->
            this.parameter1.replaceAll(functionToReplace, replacement) * this.parameter2.replaceAll(functionToReplace, replacement)

        is DivisionFormal                    ->
            this.parameter1.replaceAll(functionToReplace, replacement) / this.parameter2.replaceAll(functionToReplace, replacement)
    }

fun FunctionFormal<*>.variables(): SortedArray<VariableFormal>
{
    val variables = SortedArray<VariableFormal>(unique = true)
    val stack = Stack<FunctionFormal<*>>()
    stack.push(this)

    while (stack.isNotEmpty())
    {
        val function = stack.pop()

        when (function)
        {
            is VariableFormal          ->
                variables.add(function)

            is UnaryOperatorFormal<*>  ->
                stack.push(function.parameter)

            is BinaryOperatorFormal<*> ->
            {
                stack.push(function.parameter1)
                stack.push(function.parameter2)
            }

            else                       -> Unit
        }
    }

    return variables
}