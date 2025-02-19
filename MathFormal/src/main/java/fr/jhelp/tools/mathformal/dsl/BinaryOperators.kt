package fr.jhelp.tools.mathformal.dsl

import fr.jhelp.tools.mathformal.AdditionFormal
import fr.jhelp.tools.mathformal.DivisionFormal
import fr.jhelp.tools.mathformal.FunctionFormal
import fr.jhelp.tools.mathformal.MultiplicationFormal
import fr.jhelp.tools.mathformal.SubtractionFormal

// ################
// ### ADDITION ###
// ################

operator fun FunctionFormal<*>.plus(other: FunctionFormal<*>): AdditionFormal =
    createAddition(this, other)

operator fun FunctionFormal<*>.plus(other: Number): AdditionFormal =
    createAddition(this, other.constant)

operator fun Number.plus(other: FunctionFormal<*>): AdditionFormal =
    createAddition(this.constant, other)

operator fun FunctionFormal<*>.plus(other: String): AdditionFormal =
    createAddition(this, other.variable)

private fun createAddition(parameter1: FunctionFormal<*>, parameter2: FunctionFormal<*>): AdditionFormal =
    if (parameter1 <= parameter2)
    {
        AdditionFormal(parameter1, parameter2)
    }
    else
    {
        AdditionFormal(parameter2, parameter1)
    }

// ###################
// ### SUBTRACTION ###
// ###################

operator fun FunctionFormal<*>.minus(other: FunctionFormal<*>): SubtractionFormal =
    SubtractionFormal(this, other)

operator fun FunctionFormal<*>.minus(other: Number): SubtractionFormal =
    SubtractionFormal(this, other.constant)

operator fun Number.minus(other: FunctionFormal<*>): SubtractionFormal =
    SubtractionFormal(this.constant, other)

operator fun FunctionFormal<*>.minus(other: String): SubtractionFormal =
    SubtractionFormal(this, other.variable)

// ######################
// ### MULTIPLICATION ###
// ######################

operator fun FunctionFormal<*>.times(other: FunctionFormal<*>): MultiplicationFormal =
    createMultiplication(this, other)

operator fun FunctionFormal<*>.times(other: Number): MultiplicationFormal =
    createMultiplication(this, other.constant)

operator fun Number.times(other: FunctionFormal<*>): MultiplicationFormal =
    createMultiplication(this.constant, other)

operator fun FunctionFormal<*>.times(other: String): MultiplicationFormal =
    createMultiplication(this, other.variable)

private fun createMultiplication(parameter1: FunctionFormal<*>, parameter2: FunctionFormal<*>): MultiplicationFormal =
    if (parameter1 <= parameter2)
    {
        MultiplicationFormal(parameter1, parameter2)
    }
    else
    {
        MultiplicationFormal(parameter2, parameter1)
    }


// ################
// ### DIVISION ###
// ################

operator fun FunctionFormal<*>.div(other: FunctionFormal<*>): DivisionFormal =
    DivisionFormal(this, other)

operator fun FunctionFormal<*>.div(other: Number): DivisionFormal =
    DivisionFormal(this, other.constant)

operator fun Number.div(other: FunctionFormal<*>): DivisionFormal =
    DivisionFormal(this.constant, other)

operator fun FunctionFormal<*>.div(other: String): DivisionFormal =
    DivisionFormal(this, other.variable)