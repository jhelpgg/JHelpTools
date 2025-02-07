package fr.jhelp.tools.mathformal.dsl

import fr.jhelp.tools.mathformal.CosineFormal
import fr.jhelp.tools.mathformal.FunctionFormal
import fr.jhelp.tools.mathformal.SineFormal
import fr.jhelp.tools.mathformal.UnaryMinusFormal

operator fun FunctionFormal<*>.unaryMinus(): UnaryMinusFormal = UnaryMinusFormal(this)

fun cos(parameter: FunctionFormal<*>): FunctionFormal<*> = CosineFormal(parameter)

fun cos(value: Number): FunctionFormal<*> = CosineFormal(value.constant)

fun cos(name:String) : FunctionFormal<*> = CosineFormal(name.variable)

fun sin(parameter: FunctionFormal<*>): FunctionFormal<*> = SineFormal(parameter)

fun sin(value: Number): FunctionFormal<*> = SineFormal(value.constant)

fun sin(name:String) : FunctionFormal<*> = SineFormal(name.variable)
