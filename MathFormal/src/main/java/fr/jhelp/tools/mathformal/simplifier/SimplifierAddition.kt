package fr.jhelp.tools.mathformal.simplifier

import fr.jhelp.tools.mathformal.AdditionFormal
import fr.jhelp.tools.mathformal.ConstantFormal
import fr.jhelp.tools.mathformal.FunctionFormal
import fr.jhelp.tools.mathformal.SubtractionFormal
import fr.jhelp.tools.mathformal.UnaryMinusFormal
import fr.jhelp.tools.mathformal.dsl.UNDEFINED
import fr.jhelp.tools.mathformal.dsl.ZERO
import fr.jhelp.tools.mathformal.dsl.constant
import fr.jhelp.tools.mathformal.dsl.minus
import fr.jhelp.tools.mathformal.dsl.plus
import fr.jhelp.tools.mathformal.dsl.unaryMinus

internal fun simplifyAddition(addition: AdditionFormal): FunctionFormal<*>
{
    val parameter1 = addition.parameter1
    val parameter2 = addition.parameter2

    return when
    {
        parameter1 == UNDEFINED || parameter2 == UNDEFINED               ->
            UNDEFINED

        parameter1 == ZERO                                               ->
            simplifyFormal(parameter2)

        parameter2 == ZERO                                               ->
            simplifyFormal(parameter1)

        parameter1 is ConstantFormal                                     ->
            simplifyAddition(parameter1, parameter2)

        parameter2 is ConstantFormal                                     ->
            simplifyAddition(parameter2, parameter1)

        parameter1 is UnaryMinusFormal && parameter2 is UnaryMinusFormal ->
            -(simplifyFormal(parameter1.parameter) + simplifyFormal(parameter2.parameter))

        parameter1 is UnaryMinusFormal                                   ->
            simplifyFormal(parameter2) - simplifyFormal(parameter1.parameter)

        parameter2 is UnaryMinusFormal                                   ->
            simplifyFormal(parameter1) - simplifyFormal(parameter2.parameter)

        parameter1 is AdditionFormal && parameter2 is AdditionFormal     ->
            simplifyAddition(parameter1, parameter2)

        parameter1 is AdditionFormal                                     ->
            simplifyFormal(parameter1.parameter1) + simplifyFormal(parameter1.parameter2 + parameter2)

        parameter2 is AdditionFormal                                     ->
            simplifyFormal(parameter2.parameter1) + simplifyFormal(AdditionFormal(parameter2.parameter2, parameter1))

        else                                                             ->
            simplifyFormal(parameter1) + simplifyFormal(parameter2)
    }
}

private fun simplifyAddition(constant: ConstantFormal, other: FunctionFormal<*>): FunctionFormal<*> =
    when (other)
    {
        is ConstantFormal    ->
            constant(constant.value + other.value)

        is UnaryMinusFormal  ->
            constant - simplifyFormal(other.parameter)

        is AdditionFormal    ->
        {
            val parameter21 = other.parameter1
            val parameter22 = other.parameter2

            when
            {
                parameter21 == UNDEFINED || parameter22 == UNDEFINED           ->
                    UNDEFINED

                parameter21 is ConstantFormal && parameter22 is ConstantFormal ->
                    constant(constant.value + parameter21.value + parameter22.value)

                parameter21 is ConstantFormal                                  ->
                    constant(constant.value + parameter21.value) + simplifyFormal(parameter22)

                parameter22 is ConstantFormal                                  ->
                    constant(constant.value + parameter22.value) + simplifyFormal(parameter21)

                else                                                           ->
                    constant + simplifyFormal(other)
            }
        }

        // C + (f1 - f2)
        is SubtractionFormal ->
        {
            val parameter21 = other.parameter1
            val parameter22 = other.parameter2

            when
            {
                parameter21 == UNDEFINED || parameter22 == UNDEFINED           ->
                    UNDEFINED

                // C + (C1 - C2)
                parameter21 is ConstantFormal && parameter22 is ConstantFormal ->
                    constant(constant.value + parameter21.value - parameter22.value)

                // C + (C1 - f2)
                parameter21 is ConstantFormal                                  ->
                    constant(constant.value + parameter21.value) - simplifyFormal(parameter22)

                // C + (f1 - C2)
                parameter22 is ConstantFormal                                  ->
                    constant(constant.value - parameter22.value) + simplifyFormal(parameter21)

                else                                                           ->
                    constant + simplifyFormal(other)
            }
        }

        else                 ->
            constant + simplifyFormal(other)
    }

private fun simplifyAddition(addition1: AdditionFormal, addition2: AdditionFormal): FunctionFormal<*>
{
    val parameter11 = addition1.parameter1
    val parameter12 = addition1.parameter2
    val parameter21 = addition2.parameter1
    val parameter22 = addition2.parameter2
    val parametersLeft = mutableListOf<FunctionFormal<*>>(parameter11, parameter12, parameter21, parameter22)
    val iterator = parametersLeft.iterator()
    var constant = ZERO

    while (iterator.hasNext())
    {
        val parameter = iterator.next()

        if (parameter == UNDEFINED)
        {
            return UNDEFINED
        }

        if (parameter is ConstantFormal)
        {
            constant = constant(constant.value + parameter.value)
            iterator.remove()
        }
    }

    if (parametersLeft.size == 4)
    {
        return simplifyFormal(parameter11) + simplifyFormal(parameter12) + simplifyFormal(parameter21) + simplifyFormal(parameter22)
    }

    var result: FunctionFormal<*> = constant

    for (parameter in parametersLeft)
    {
        result += simplifyFormal(parameter)
    }

    return result
}
