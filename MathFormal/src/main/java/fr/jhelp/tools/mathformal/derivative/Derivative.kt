package fr.jhelp.tools.mathformal.derivative

import fr.jhelp.tools.mathformal.AdditionFormal
import fr.jhelp.tools.mathformal.ConstantFormal
import fr.jhelp.tools.mathformal.CosineFormal
import fr.jhelp.tools.mathformal.DivisionFormal
import fr.jhelp.tools.mathformal.FunctionFormal
import fr.jhelp.tools.mathformal.MultiplicationFormal
import fr.jhelp.tools.mathformal.SineFormal
import fr.jhelp.tools.mathformal.SubtractionFormal
import fr.jhelp.tools.mathformal.UnaryMinusFormal
import fr.jhelp.tools.mathformal.VariableFormal
import fr.jhelp.tools.mathformal.dsl.ONE
import fr.jhelp.tools.mathformal.dsl.UNDEFINED
import fr.jhelp.tools.mathformal.dsl.ZERO
import fr.jhelp.tools.mathformal.dsl.cos
import fr.jhelp.tools.mathformal.dsl.div
import fr.jhelp.tools.mathformal.dsl.minus
import fr.jhelp.tools.mathformal.dsl.plus
import fr.jhelp.tools.mathformal.dsl.sin
import fr.jhelp.tools.mathformal.dsl.times
import fr.jhelp.tools.mathformal.dsl.unaryMinus
import fr.jhelp.tools.mathformal.simplifier.simplified
import fr.jhelp.tools.mathformal.variables
import fr.jhelp.tools.utilities.collections.SortedArray

fun FunctionFormal<*>.derivative(variable: VariableFormal): FunctionFormal<*> =
    this.derivativeInternal(variable).simplified

private fun FunctionFormal<*>.derivativeInternal(variable: VariableFormal) : FunctionFormal<*> =
    when (this)
    {
        UNDEFINED                            -> UNDEFINED
        variable                             -> ONE
        is ConstantFormal, is VariableFormal -> ZERO
        is UnaryMinusFormal                  ->
            this.parameter.derivativeInternal(variable).unaryMinus()

        is CosineFormal                      ->
            -this.parameter.derivativeInternal(variable) * sin(this.parameter)

        is SineFormal                        ->
            this.parameter.derivativeInternal(variable) * cos(this.parameter)

        is AdditionFormal                    ->
            this.parameter1.derivativeInternal(variable) + this.parameter2.derivativeInternal(variable)

        is SubtractionFormal                 ->
            this.parameter1.derivativeInternal(variable) - this.parameter2.derivativeInternal(variable)

        is MultiplicationFormal              ->
            this.parameter1.derivativeInternal(variable) * this.parameter2 + this.parameter1 * this.parameter2.derivativeInternal(variable)

        is DivisionFormal                    ->
            (this.parameter1.derivativeInternal(variable) * this.parameter2 - this.parameter1 * this.parameter2.derivativeInternal(variable)) / (this.parameter2 * this.parameter2)
    }

fun FunctionFormal<*>.derivatives(variableFirst:VariableFormal, vararg variables: VariableFormal) : FunctionFormal<*>
{
    val sortedVariables = SortedArray<VariableFormal>(true)
    sortedVariables.add(variableFirst)

    for(variable in variables)
    {
        sortedVariables.add(variable)
    }

    return derivatives(this, sortedVariables)
}

fun FunctionFormal<*>.delta() : FunctionFormal<*> =
    derivatives(this, this.variables())

private fun derivatives(function: FunctionFormal<*>, variables: SortedArray<VariableFormal>) : FunctionFormal<*>
{
    if(function == UNDEFINED)
    {
        return UNDEFINED
    }

    if(variables.empty)
    {
        return ZERO
    }

    var result = function.derivative(variables[0])

    for(index in 1 until variables.size)
    {
        result += result.derivative(variables[index])
    }

    return result
}