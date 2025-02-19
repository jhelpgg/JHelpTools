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

/**
 * Compute the derivative of a function with respect to a variable.
 *
 * @param variable The variable with respect to which the derivative is computed.
 * @return The derivative of the function with respect to the variable.
 */
fun FunctionFormal<*>.derivative(variable: VariableFormal): FunctionFormal<*> =
    this.derivativeInternal(variable).simplified

/**
 * Compute the derivative of a function with respect to a variable.
 * @param variable The variable with respect to which the derivative is computed.
 */
private fun FunctionFormal<*>.derivativeInternal(variable: VariableFormal) : FunctionFormal<*> =
    when (this)
    {
        UNDEFINED                            -> UNDEFINED

        /*
            d (   )
            --( x ) = 1
            dx(   )
        */
        variable                             -> ONE

        /*
            d (   )
            --( 5 ) = 0
            dx(   )

            d (   )
            --( y ) = 1
            dx(   )
        */
        is ConstantFormal, is VariableFormal -> ZERO

        /*
            d (       )     d (      )
            --( -f(x) ) = - --( f(x) )
            dx(       )     dx(      )
        */
        is UnaryMinusFormal                  ->
            this.parameter.derivativeInternal(variable).unaryMinus()

        /*
            d (           )     d (      )
            --( cos(f(x)) ) = - --( f(x) ) * sin(f(x))
            dx(           )     dx(      )
        */
        is CosineFormal                      ->
            -this.parameter.derivativeInternal(variable) * sin(this.parameter)

        /*
            d (           )   d (      )
            --( sin(f(x)) ) = --( f(x) ) * cos(f(x))
            dx(           )   dx(      )
        */
        is SineFormal                        ->
            this.parameter.derivativeInternal(variable) * cos(this.parameter)

        /*
            d (             )   d (      )   d (      )
            --( f(x) + g(x) ) = --( f(x) ) + --( g(x) )
            dx(             )   dx(      )   dx(      )
        */
        is AdditionFormal                    ->
            this.parameter1.derivativeInternal(variable) + this.parameter2.derivativeInternal(variable)


        /*
            d (             )   d (      )   d (      )
            --( f(x) - g(x) ) = --( f(x) ) - --( g(x) )
            dx(             )   dx(      )   dx(      )
        */
        is SubtractionFormal                 ->
            this.parameter1.derivativeInternal(variable) - this.parameter2.derivativeInternal(variable)

        /*
            d (             )   d (      )           d
            --( f(x) * g(x) ) = --( f(x) ) * g(x)  + --( g(x) ) * f(x)
            dx(             )   dx(      )           dx(      )
         */
        is MultiplicationFormal              ->
            this.parameter1.derivativeInternal(variable) * this.parameter2 + this.parameter1 * this.parameter2.derivativeInternal(variable)

        /*
                         d                   d (      )
                         --( f(x) ) * g(x) - --( g(x) ) * f(x)
            d ( f(x) )   dx(      )          dx(      )
            --( ---- ) = --------------------------------------
            dx( g(x) )      g(x) * g(x)
         */
        is DivisionFormal                    ->
            (this.parameter1.derivativeInternal(variable) * this.parameter2 - this.parameter1 * this.parameter2.derivativeInternal(variable)) / (this.parameter2 * this.parameter2)
    }

/**
 * Compute the derivative of a function with respect to several variables
 *
 * @param variableFirst The first variable with respect to which the derivative is computed.
 * @param variables The variables with respect to which the derivative is computed.
 * @return The derivative of the function with respect to the variables.
 */
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

/**
 * Compute the function's delta
 *
 * @return The delta of the function.
 */
fun FunctionFormal<*>.delta() : FunctionFormal<*> =
    derivatives(this, this.variables())

/**
 * Compute the derivative of a function with respect to several variables.
 * @param function The function whose derivative is computed.
 * @param variables The variables with respect to which the derivative is computed.
 * @return The derivative of the function with respect to the variables.
 */
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