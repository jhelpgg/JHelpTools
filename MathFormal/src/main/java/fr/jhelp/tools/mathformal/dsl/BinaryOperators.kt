package fr.jhelp.tools.mathformal.dsl

import fr.jhelp.tools.mathformal.AdditionFormal
import fr.jhelp.tools.mathformal.FunctionFormal
import fr.jhelp.tools.mathformal.SubtractionFormal

operator fun FunctionFormal<*>.plus(other: FunctionFormal<*>): AdditionFormal =
    createAddition(this, other)

operator fun FunctionFormal<*>.plus(other: Number): AdditionFormal =
    createAddition(this, other.constant)

operator fun Number.plus(other: FunctionFormal<*>): AdditionFormal =
    createAddition(constant(this.toDouble()), other)

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

operator fun FunctionFormal<*>.minus(other: FunctionFormal<*>): SubtractionFormal =
    SubtractionFormal(this, other)

operator fun FunctionFormal<*>.minus(other: Number): SubtractionFormal =
    SubtractionFormal(this, other.constant)

operator fun Number.minus(other: FunctionFormal<*>): SubtractionFormal =
    SubtractionFormal(constant(this.toDouble()), other)

operator fun FunctionFormal<*>.minus(other: String): SubtractionFormal =
    SubtractionFormal(this, other.variable)
