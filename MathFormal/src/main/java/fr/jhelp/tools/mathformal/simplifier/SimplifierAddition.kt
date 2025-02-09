package fr.jhelp.tools.mathformal.simplifier

import fr.jhelp.tools.mathformal.AdditionFormal
import fr.jhelp.tools.mathformal.ConstantFormal
import fr.jhelp.tools.mathformal.CosineFormal
import fr.jhelp.tools.mathformal.FunctionFormal
import fr.jhelp.tools.mathformal.MultiplicationFormal
import fr.jhelp.tools.mathformal.SineFormal
import fr.jhelp.tools.mathformal.SubtractionFormal
import fr.jhelp.tools.mathformal.UnaryMinusFormal
import fr.jhelp.tools.mathformal.dsl.ONE
import fr.jhelp.tools.mathformal.dsl.UNDEFINED
import fr.jhelp.tools.mathformal.dsl.ZERO
import fr.jhelp.tools.mathformal.dsl.constant
import fr.jhelp.tools.mathformal.dsl.cos
import fr.jhelp.tools.mathformal.dsl.minus
import fr.jhelp.tools.mathformal.dsl.plus
import fr.jhelp.tools.mathformal.dsl.sin
import fr.jhelp.tools.mathformal.dsl.times
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
            simplifyFormal(parameter2.parameter1) + simplifyFormal(parameter2.parameter2 + parameter1)

        parameter1 is MultiplicationFormal                               ->
            simplifyAddition(parameter1, parameter2)

        parameter2 is MultiplicationFormal                               ->
            simplifyAddition(parameter2, parameter1)

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

private fun simplifyAddition(multiplicationFormal: MultiplicationFormal, function: FunctionFormal<*>): FunctionFormal<*>
{
    val parameter1 = multiplicationFormal.parameter1
    val parameter2 = multiplicationFormal.parameter2

    return when
    {
        parameter1 == UNDEFINED || parameter2 == UNDEFINED ->
            UNDEFINED

        function is MultiplicationFormal                   ->
            simplifyAdditionOfMultiplications(parameter1, parameter2, function.parameter1, function.parameter2)

        parameter1 == function                             ->
            simplifyFormal(function * (parameter2 + ONE))

        parameter2 == function                             ->
            simplifyFormal(function * (parameter1 + ONE))

        else                                               ->
            simplifyFormal(multiplicationFormal) + simplifyFormal(function)
    }
}

// f1.f2 + f3.f4
private fun simplifyAdditionOfMultiplications(parameter11: FunctionFormal<*>, parameter12: FunctionFormal<*>,
                                              parameter21: FunctionFormal<*>, parameter22: FunctionFormal<*>): FunctionFormal<*> =
    when
    {
        parameter11 == UNDEFINED || parameter12 == UNDEFINED || parameter21 == UNDEFINED || parameter22 == UNDEFINED         ->
            UNDEFINED

        // f1.f2 + f1.f4 -> f1.(f2 + f4)
        parameter11 == parameter21                                                                                           ->
            simplifyFormal(parameter11 * (parameter12 + parameter22))

        // f1.f2 + f3.f1 -> f1.(f2 + f3)
        parameter11 == parameter22                                                                                           ->
            simplifyFormal(parameter11 * (parameter12 + parameter21))

        // f1.f2 + f2.f4 -> f2.(f1 + f4)
        parameter12 == parameter21                                                                                           ->
            simplifyFormal(parameter12 * (parameter11 + parameter22))

        // f1.f2 + f3.f2 -> f2.(f1 + f3)
        parameter12 == parameter22                                                                                           ->
            simplifyFormal(parameter12 * (parameter11 + parameter21))

        parameter11 is CosineFormal && parameter12 is CosineFormal && parameter21 is SineFormal && parameter22 is SineFormal ->
            simplifyCosCos_SinSin(parameter11, parameter12, parameter21, parameter22)

        parameter11 is SineFormal && parameter12 is SineFormal && parameter21 is CosineFormal && parameter22 is CosineFormal ->
            simplifyCosCos_SinSin(parameter21, parameter22, parameter11, parameter12)

        parameter11 is CosineFormal && parameter12 is SineFormal && parameter21 is CosineFormal && parameter22 is SineFormal ->
            simplifyCosSin_CosSin(parameter11, parameter12, parameter21, parameter22)

        parameter11 is SineFormal && parameter12 is CosineFormal && parameter21 is CosineFormal && parameter22 is SineFormal ->
            simplifyCosSin_CosSin(parameter12, parameter11, parameter21, parameter22)

        parameter11 is CosineFormal && parameter12 is SineFormal && parameter21 is SineFormal && parameter22 is CosineFormal ->
            simplifyCosSin_CosSin(parameter11, parameter12, parameter22, parameter21)

        parameter11 is SineFormal && parameter12 is CosineFormal && parameter21 is SineFormal && parameter22 is CosineFormal ->
            simplifyCosSin_CosSin(parameter12, parameter11, parameter22, parameter21)

        // f1.f2 + f3.f4
        else                                                                                                                 ->
            simplifyFormal(parameter11 * parameter12) + simplifyFormal(parameter21 * parameter22)
    }

private fun simplifyCosCos_SinSin(cosine1: CosineFormal, cosine2: CosineFormal, sine1: SineFormal, sine2: SineFormal): FunctionFormal<*>
{
    val parameterCos1 = cosine1.parameter
    val parameterCos2 = cosine2.parameter
    val parameterSin1 = sine1.parameter
    val parameterSin2 = sine2.parameter

    return when
    {
        parameterCos1 == UNDEFINED || parameterCos2 == UNDEFINED || parameterSin1 == UNDEFINED || parameterSin2 == UNDEFINED ->
            UNDEFINED

        // cos(f) * cos(f) + sin(f) * sin(f)   -> 1
        parameterCos1 == parameterCos2 && parameterSin1 == parameterSin2 ->
            ONE

        // cos(f1) * cos(f2) + sin(f1) * sin(f2)   -> cos(f1-f2)
        parameterCos1 == parameterSin1 && parameterCos2 == parameterSin2 ->
            cos(simplifyFormal(parameterCos1 - parameterCos2))

        // cos(f1) * cos(f2) + sin(f2) * sin(f1)   -> cos(f1-f2)
        parameterCos1 == parameterSin2 && parameterCos2 == parameterSin1 ->
            cos(simplifyFormal(parameterCos1 - parameterCos2))

        else ->
            simplifyFormal(cosine1) * simplifyFormal(cosine2) + simplifyFormal(sine1) * simplifyFormal(sine2)
    }
}

private fun simplifyCosSin_CosSin(cosine1: CosineFormal, sine1: SineFormal, cosine2: CosineFormal, sine2: SineFormal): FunctionFormal<*>
{
    val parameterCos1 = cosine1.parameter
    val parameterSin1 = sine1.parameter
    val parameterCos2 = cosine2.parameter
    val parameterSin2 = sine2.parameter

    return when
    {
        parameterCos1 == UNDEFINED || parameterCos2 == UNDEFINED || parameterSin1 == UNDEFINED || parameterSin2 == UNDEFINED ->
            UNDEFINED

        // cos(f1) * sin(f2) + cos(f2) * sin(f1)   -> sin(f1+f2)
        parameterCos1 == parameterSin2 && parameterCos2 == parameterSin1 ->
            sin(simplifyFormal(parameterCos1 + parameterCos2))

        else ->
            simplifyFormal(cosine1) * simplifyFormal(sine1) + simplifyFormal(cosine2) * simplifyFormal(sine2)
    }
}
