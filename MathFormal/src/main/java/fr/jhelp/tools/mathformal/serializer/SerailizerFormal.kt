package fr.jhelp.tools.mathformal.serializer

import fr.jhelp.tools.mathformal.BinaryOperatorFormal
import fr.jhelp.tools.mathformal.ConstantFormal
import fr.jhelp.tools.mathformal.FunctionFormal
import fr.jhelp.tools.mathformal.UnaryOperatorFormal
import fr.jhelp.tools.mathformal.VariableFormal
import fr.jhelp.tools.mathformal.obtainSymbolKeyOf

fun serializeFormal(functionFormal: FunctionFormal<*>): String
{
    val symbolKey = obtainSymbolKeyOf(functionFormal)

    if (symbolKey.isPresent)
    {
        return symbolKey.get()
    }

    return when (functionFormal)
    {
        is ConstantFormal -> functionFormal.value.toString()
        is VariableFormal -> functionFormal.name
        is UnaryOperatorFormal<*> -> serializeUnaryOperator(functionFormal)
        is BinaryOperatorFormal<*> -> serializeBinaryOperator(functionFormal)
    }
}