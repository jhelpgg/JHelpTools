package fr.jhelp.tools.mathformal.simplifier

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
import fr.jhelp.tools.mathformal.dsl.UNDEFINED
import java.util.TreeSet

internal fun simplifyFormal(functionFormal: FunctionFormal<*>): FunctionFormal<*>
{
    val simplified = undefinedSimplifier(functionFormal)

    return when (simplified)
    {
        UNDEFINED               -> UNDEFINED
        is ConstantFormal       -> simplified
        is VariableFormal       -> simplified
        is UnaryMinusFormal     -> simplifyMinusUnary(simplified)
        is CosineFormal         -> simplifyCosine(simplified)
        is SineFormal           -> simplifySine(simplified)
        is AdditionFormal       -> simplifyAddition(simplified)
        is SubtractionFormal    -> simplifySubtraction(simplified)
        is MultiplicationFormal -> simplifyMultiplication(simplified)
        is DivisionFormal       -> simplifyDivision(simplified)
    }
}

val FunctionFormal<*>.simplified: FunctionFormal<*> get() = this.simplify()

fun FunctionFormal<*>.simplify(original: (FunctionFormal<*>) -> Unit = {},
                               step: (FunctionFormal<*>) -> Unit = {},
                               simplified: (FunctionFormal<*>) -> Unit = {}): FunctionFormal<*>
{
    original(this)
    val seen = TreeSet<FunctionFormal<*>>()
    seen.add(this)
    var simplify = simplifyFormal(this)

    while (seen.add(simplify))
    {
        step(simplify)
        simplify = simplifyFormal(simplify)
    }

    simplified(simplify)
    return simplify
}