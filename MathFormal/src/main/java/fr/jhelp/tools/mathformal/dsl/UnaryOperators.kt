package fr.jhelp.tools.mathformal.dsl

import fr.jhelp.tools.mathformal.CosineFormal
import fr.jhelp.tools.mathformal.FunctionFormal
import fr.jhelp.tools.mathformal.SineFormal
import fr.jhelp.tools.mathformal.UnaryMinusFormal

operator fun FunctionFormal<*>.unaryMinus(): UnaryMinusFormal = UnaryMinusFormal(this)

fun cos(parameter: FunctionFormal<*>): CosineFormal = CosineFormal(parameter)

fun cos(value: Number): CosineFormal = CosineFormal(value.constant)

fun cos(name:String) : CosineFormal = CosineFormal(name.variable)

fun sin(parameter: FunctionFormal<*>): SineFormal = SineFormal(parameter)

fun sin(value: Number): SineFormal = SineFormal(value.constant)

fun sin(name:String) : SineFormal = SineFormal(name.variable)
