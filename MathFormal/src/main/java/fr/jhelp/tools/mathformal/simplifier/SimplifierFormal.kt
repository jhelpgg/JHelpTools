package fr.jhelp.tools.mathformal.simplifier

import fr.jhelp.tools.mathformal.AdditionFormal
import fr.jhelp.tools.mathformal.ConstantFormal
import fr.jhelp.tools.mathformal.CosineFormal
import fr.jhelp.tools.mathformal.FunctionFormal
import fr.jhelp.tools.mathformal.SineFormal
import fr.jhelp.tools.mathformal.UnaryMinusFormal
import fr.jhelp.tools.mathformal.VariableFormal
import java.util.TreeSet

fun simplifyFormal(functionFormal: FunctionFormal<*>): FunctionFormal<*> =
    when (functionFormal)
    {
        is ConstantFormal   -> functionFormal
        is VariableFormal   -> functionFormal
        is UnaryMinusFormal -> simplifyMinusUnary(functionFormal)
        is CosineFormal     -> simplifyCosine(functionFormal)
        is SineFormal       -> simplifySine(functionFormal)
        is AdditionFormal   -> simplifyAddition(functionFormal)
    }

val FunctionFormal<*>.simplify: FunctionFormal<*>
    get()
    {
        println(this)
        val seen = TreeSet<FunctionFormal<*>>()
        seen.add(this)
        var simplify = simplifyFormal(this)

        while (seen.add(simplify))
        {
            println("-> $simplify")
            simplify = simplifyFormal(simplify)
        }

        println("=> $simplify")
        return simplify
    }