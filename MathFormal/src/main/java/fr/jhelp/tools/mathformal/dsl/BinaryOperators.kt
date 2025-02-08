package fr.jhelp.tools.mathformal.dsl

import fr.jhelp.tools.mathformal.*

operator fun FunctionFormal<*>.plus(other: FunctionFormal<*>): FunctionFormal<*> =
    AdditionFormal(this, other)

operator fun FunctionFormal<*>.plus(other: Number): FunctionFormal<*> =
    AdditionFormal(this, other.constant)

operator fun Number.plus(other: FunctionFormal<*>): FunctionFormal<*> =
    AdditionFormal(constant(this.toDouble()), other)

operator fun FunctionFormal<*>.plus(other: String): FunctionFormal<*> =
    AdditionFormal(this, other.variable)

operator fun FunctionFormal<*>.minus(other: FunctionFormal<*>): FunctionFormal<*> =
    SubtractionFormal(this, other)

operator fun FunctionFormal<*>.minus(other: Number): FunctionFormal<*> =
    SubtractionFormal(this, other.constant)

operator fun Number.minus(other: FunctionFormal<*>): FunctionFormal<*> =
    SubtractionFormal(constant(this.toDouble()), other)

operator fun FunctionFormal<*>.minus(other: String): FunctionFormal<*> =
    SubtractionFormal(this, other.variable)
