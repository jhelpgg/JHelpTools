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
import fr.jhelp.tools.mathformal.dsl.ONE
import fr.jhelp.tools.mathformal.dsl.ZERO
import fr.jhelp.tools.mathformal.dsl.constant
import fr.jhelp.tools.mathformal.dsl.cos
import fr.jhelp.tools.mathformal.dsl.div
import fr.jhelp.tools.mathformal.dsl.minus
import fr.jhelp.tools.mathformal.dsl.plus
import fr.jhelp.tools.mathformal.dsl.sin
import fr.jhelp.tools.mathformal.dsl.times
import fr.jhelp.tools.mathformal.dsl.unaryMinus
import fr.jhelp.tools.mathformal.simplifier.sumcollector.tryFactorizeSum

internal fun simplifySubtraction(subtraction: SubtractionFormal): FunctionFormal<*>
{
    val parameter1 = subtraction.parameter1
    val parameter2 = subtraction.parameter2

    return when
    {
        parameter1 == ZERO                                               ->
            -simplifyFormal(parameter2)

        parameter2 == ZERO                                               ->
            simplifyFormal(parameter1)

        parameter1 == parameter2                                         ->
            ZERO

        parameter1 is ConstantFormal                                     ->
            simplifySubtraction(parameter1, parameter2)

        parameter2 is ConstantFormal                                     ->
            simplifySubtraction(parameter1, parameter2)

        // (-f1) - (-f2) -> f2 - f1
        parameter1 is UnaryMinusFormal && parameter2 is UnaryMinusFormal ->
            simplifyFormal(parameter2.parameter) - simplifyFormal(parameter1.parameter)

        // (-f1) - f2 -> -(f1 + f2)
        parameter1 is UnaryMinusFormal                                   ->
            -(simplifyFormal(parameter1.parameter) + simplifyFormal(parameter2))

        // f1 - (-f2) - > f1 + f2
        parameter2 is UnaryMinusFormal                                   ->
            parameter1 + simplifyFormal(parameter2.parameter)

        else                                                             ->
        {
            val factorization = tryFactorizeSum(subtraction)

            if (factorization == subtraction)
            {
                when
                {
                    parameter1 is AdditionFormal                                 ->
                        simplifySubtraction(parameter1, parameter2)

                    parameter2 is AdditionFormal                                 ->
                        simplifySubtraction(parameter1, parameter2)

                    parameter1 is MultiplicationFormal                           ->
                        simplifySubtraction(parameter1, parameter2)

                    parameter2 is MultiplicationFormal                           ->
                        simplifySubtraction(parameter1, parameter2)

                    parameter1 is DivisionFormal && parameter2 is DivisionFormal ->
                        simplifySubtraction(parameter1, parameter2)

                    parameter1 is DivisionFormal                                 ->
                        simplifySubtraction(parameter1, parameter2)

                    parameter2 is DivisionFormal                                 ->
                        simplifySubtraction(parameter1, parameter2)

                    else                                                         ->
                        simplifyFormal(parameter1) - simplifyFormal(parameter2)
                }
            }
            else
            {
                factorization
            }
        }
    }
}

private fun simplifySubtraction(constant: ConstantFormal, other: FunctionFormal<*>): FunctionFormal<*> =
    when (other)
    {
        is ConstantFormal    ->
            constant(constant.value - other.value)

        is UnaryMinusFormal  ->
            constant + simplifyFormal(other.parameter)

        is AdditionFormal    ->
            simplifySubtractionAddition(constant, other.parameter1, other.parameter2)

        is SubtractionFormal ->
            simplifySubtractionSubtraction(constant, other.parameter1, other.parameter2)

        else                 ->
            constant - simplifyFormal(other)
    }

// C - (f1 + f2)
private fun simplifySubtractionAddition(constant: ConstantFormal, additionParameter1: FunctionFormal<*>, additionParameter2: FunctionFormal<*>): FunctionFormal<*> =
    when
    {
        additionParameter1 is ConstantFormal && additionParameter2 is ConstantFormal ->
            constant(constant.value - (additionParameter1.value + additionParameter2.value))

        additionParameter1 is ConstantFormal                                         ->
            constant(constant.value - additionParameter1.value) - simplifyFormal(additionParameter2)

        additionParameter2 is ConstantFormal                                         ->
            constant(constant.value - additionParameter2.value) - simplifyFormal(additionParameter1)

        // C - (f1 + f2)
        else                                                                         ->
            constant - (simplifyFormal(additionParameter1) + simplifyFormal(additionParameter2))
    }

// C - (F - G)
private fun simplifySubtractionSubtraction(constant: ConstantFormal, subtractionParameter1: FunctionFormal<*>, subtractionParameter2: FunctionFormal<*>): FunctionFormal<*> =
    when
    {
        subtractionParameter1 is ConstantFormal && subtractionParameter2 is ConstantFormal ->
            constant(constant.value - (subtractionParameter1.value - subtractionParameter2.value))

        subtractionParameter1 is ConstantFormal                                            ->
            constant(constant.value - subtractionParameter1.value) + simplifyFormal(subtractionParameter2)

        subtractionParameter2 is ConstantFormal                                            ->
            constant(constant.value + subtractionParameter2.value) - simplifyFormal(subtractionParameter1)

        else                                                                               ->
            constant - simplifyFormal(subtractionParameter1) - simplifyFormal(subtractionParameter2)
    }

private fun simplifySubtraction(other: FunctionFormal<*>, constant: ConstantFormal): FunctionFormal<*> =
    when (other)
    {
        is ConstantFormal   ->
            constant(other.value - constant.value)

        is UnaryMinusFormal ->
            -(constant + simplifyFormal(other.parameter))

        else                ->
            constant(-constant.value) + simplifyFormal(other)
    }

private fun simplifySubtraction(addition: AdditionFormal, function: FunctionFormal<*>): FunctionFormal<*>
{
    val parameter1 = addition.parameter1
    val parameter2 = addition.parameter2

    return when
    {
        parameter1 == function           ->
            simplifyFormal(parameter2)

        parameter2 == function           ->
            simplifyFormal(parameter1)

        function is AdditionFormal       ->
            simplifySubtraction(addition, function)

        function is MultiplicationFormal ->
            simplifySubtraction(addition, function)

        else                             ->
            simplifyFormal(addition) - simplifyFormal(function)
    }
}

private fun simplifySubtraction(addition: AdditionFormal, multiplication: MultiplicationFormal): FunctionFormal<*>
{
    val additionParameter1 = addition.parameter1
    val additionParameter2 = addition.parameter2
    val multiplicationParameter1 = multiplication.parameter1
    val multiplicationParameter2 = multiplication.parameter2

    return when
    {
        // (f1 + f2) - (f1.f4) -> f1.(1 - f4) + f2
        additionParameter1 == multiplicationParameter1 ->
            simplifyFormal(additionParameter1) * (ONE - simplifyFormal(multiplicationParameter2)) + simplifyFormal(additionParameter2)

        // (f1 + f2) - (f3.f1) -> f1.(1 - f3) + f2
        additionParameter1 == multiplicationParameter2 ->
            simplifyFormal(additionParameter1) * (ONE - simplifyFormal(multiplicationParameter1)) + simplifyFormal(additionParameter2)

        // (f1 + f2) - (f2.f4) -> f2.(1 - f4) + f1
        additionParameter2 == multiplicationParameter1 ->
            simplifyFormal(additionParameter2) * (ONE - simplifyFormal(multiplicationParameter2)) + simplifyFormal(additionParameter1)

        // (f1 + f2) - (f3.f2) -> f2.(1 - f3) + f1
        additionParameter2 == multiplicationParameter2 ->
            simplifyFormal(additionParameter2) * (ONE - simplifyFormal(multiplicationParameter1)) + simplifyFormal(additionParameter1)

        else                                           ->
            simplifyFormal(addition) - simplifyFormal(multiplication)
    }
}

private fun simplifySubtraction(function: FunctionFormal<*>, addition: AdditionFormal): FunctionFormal<*>
{
    val parameter1 = addition.parameter1
    val parameter2 = addition.parameter2

    return when
    {
        parameter1 == function ->
            -simplifyFormal(parameter2)

        parameter2 == function ->
            -simplifyFormal(parameter1)

        else                   ->
            simplifyFormal(function) - simplifyFormal(addition)
    }
}

private fun simplifySubtraction(addition1: AdditionFormal, addition2: AdditionFormal): FunctionFormal<*>
{
    val parameter11 = addition1.parameter1
    val parameter12 = addition1.parameter2
    val parameter21 = addition2.parameter1
    val parameter22 = addition2.parameter2

    // (f1 + f2) - (f3 + f4)
    return when
    {
        // (C1 + f2) - (f3 + f4)
        parameter11 is ConstantFormal ->
            when
            {
                // (C1 + C2) - (f3 + f4)
                parameter12 is ConstantFormal ->
                    when
                    {
                        // (C1 + C2) - (C3 + f4)
                        parameter21 is ConstantFormal ->
                            when
                            {
                                // (C1 + C2) - (C3 + C4) -> C1 + C2 - C3 - C4
                                parameter22 is ConstantFormal ->
                                    constant(parameter11.value + parameter12.value - parameter21.value - parameter22.value)

                                // (C1 + C2) - (C3 + f4) -> (C1 + C2 - C3) - f4
                                else                          ->
                                    constant(parameter11.value + parameter12.value - parameter21.value) - simplifyFormal(parameter22)
                            }

                        // (C1 + C2) - (f3 + f4)
                        else                          ->
                            when
                            {
                                // (C1 + C2) - (f3 + C4) -> (C1 + C2 - C4) - f3
                                parameter22 is ConstantFormal ->
                                    constant(parameter11.value + parameter12.value - parameter22.value) - simplifyFormal(parameter21)

                                // (C1 + C2) - (f3 + f4) -> (C1 + C2) - f3 - f4
                                else                          ->
                                    constant(parameter11.value + parameter12.value) - simplifyFormal(parameter21) - simplifyFormal(parameter22)
                            }
                    }

                // (C1 + f2) - (f3 + f4)
                else                          ->
                    when
                    {
                        // (C1 + f2) - (C3 + f4)
                        parameter21 is ConstantFormal ->
                            when
                            {
                                // (C1 + f2) - (C3 + C4) ->(C1 - C3 - C4) + f2
                                parameter22 is ConstantFormal ->
                                    constant(parameter11.value - parameter21.value - parameter22.value) + simplifyFormal(parameter12)

                                // (C1 + f2) - (C3 + f4) -> (C1 - C3) + f2 - f4
                                else                          ->
                                    constant(parameter11.value - parameter21.value) + simplifyFormal(parameter12) - simplifyFormal(parameter22)
                            }

                        // (C1 + f2) - (f3 + f4)
                        else                          ->
                            when
                            {
                                // (C1 + f2) - (f3 + C4) -> (C1 - C4) + f2 - f3
                                parameter22 is ConstantFormal ->
                                    constant(parameter11.value - parameter22.value) + simplifyFormal(parameter12) - simplifyFormal(parameter21)

                                // (C1 + f2) - (f3 + f4) -> (C1) + f2 - f3 - f4
                                else                          ->
                                    parameter11 + simplifyFormal(parameter12) - simplifyFormal(parameter21) - simplifyFormal(parameter22)
                            }
                    }
            }

        // (f1 + C2) - (f3 + f4)
        parameter12 is ConstantFormal ->
            when
            {
                // (f1 + C2) - (C3 + f4)
                parameter21 is ConstantFormal ->
                    when
                    {
                        // (f1 + C2) - (C3 + C4) -> (C2 - C3 - C4) + f1
                        parameter22 is ConstantFormal ->
                            constant(parameter12.value - parameter21.value - parameter22.value) + simplifyFormal(parameter11)

                        // (f1 + C2) - (C3 + f4) -> (C2 - C3) + f1 - f4
                        else                          ->
                            constant(parameter12.value - parameter21.value) + simplifyFormal(parameter11) - simplifyFormal(parameter22)
                    }

                // (f1 + C2) - (f3 + f4)
                else                          ->
                    when
                    {
                        // (f1 + C2) - (f3 + C4) -> (C2 - C4) + f1 - f3
                        parameter22 is ConstantFormal ->
                            constant(parameter12.value - parameter22.value) + simplifyFormal(parameter11) - simplifyFormal(parameter21)

                        // (f1 + C2) - (f3 + f4) -> (C2) + f1 - f3 - f4
                        else                          ->
                            parameter12 + simplifyFormal(parameter11) - simplifyFormal(parameter21) - simplifyFormal(parameter22)
                    }
            }

        // (f1 + f2) - (C3 + f4)
        parameter21 is ConstantFormal ->
            when
            {
                // (f1 + f2) - (C3 + C4) -> (-C3-C4) + f1 + f2
                parameter22 is ConstantFormal ->
                    constant(-parameter21.value - parameter22.value) + simplifyFormal(parameter11) + simplifyFormal(parameter12)

                // (f1 + f2) - (C3 + f4) -> (-C3) + f1 + f2 - f4
                else                          ->
                    constant(-parameter21.value) + simplifyFormal(parameter11) + simplifyFormal(parameter12) - simplifyFormal(parameter22)
            }

        // (f1 + f2) - (f3 + C4) -> (-C4) + f1 + f2 - f3
        parameter22 is ConstantFormal ->
            constant(-parameter22.value) + simplifyFormal(parameter11) + simplifyFormal(parameter12) - simplifyFormal(parameter21)

        else                          ->
            simplifyFormal(addition1) - simplifyFormal(addition2)
    }
}

private fun simplifySubtraction(multiplication: MultiplicationFormal, function: FunctionFormal<*>): FunctionFormal<*>
{
    val parameter1 = multiplication.parameter1
    val parameter2 = multiplication.parameter2

    return when
    {
        function is MultiplicationFormal ->
            simplifySubtractionOfMultiplications(parameter1, parameter2, function.parameter1, function.parameter2)

        // f1.f2 - f1 -> f1.(f2 - 1)
        parameter1 == function           ->
            simplifyFormal(parameter1 * (parameter2 - ONE))

        // f1.f2 - f2 -> f2.(f1 - 1)
        parameter2 == function           ->
            simplifyFormal(parameter2 * (parameter1 - ONE))

        else                             ->
            simplifyFormal(multiplication) - simplifyFormal(function)
    }
}

private fun simplifySubtraction(function: FunctionFormal<*>, multiplication: MultiplicationFormal): FunctionFormal<*>
{
    val parameter1 = multiplication.parameter1
    val parameter2 = multiplication.parameter2

    return when
    {
        // f1 - f1.f2 -> f1.(1-f2)
        parameter1 == function ->
            simplifyFormal(parameter1 * (ONE - parameter2))

        // f1 - f2.f1 -> f1.(1-f2)
        parameter2 == function ->
            simplifyFormal(parameter2 * (ONE - parameter1))

        else                   ->
            simplifyFormal(function) - simplifyFormal(multiplication)
    }
}

// f1.f2 - f3.f4
private fun simplifySubtractionOfMultiplications(parameter11: FunctionFormal<*>, parameter12: FunctionFormal<*>,
                                                 parameter21: FunctionFormal<*>, parameter22: FunctionFormal<*>): FunctionFormal<*> =
    when
    {
        // f1.f2 - f1.f4 -> f1.(f2 - f4)
        parameter11 == parameter21                                                                                           ->
            simplifyFormal(parameter11 * (parameter12 - parameter22))

        // f1.f2 - f3.f1 -> f1.(f2 - f3)
        parameter11 == parameter22                                                                                           ->
            simplifyFormal(parameter11 * (parameter12 - parameter21))

        // f1.f2 - f2.f4 -> f2.(f1 - f4)
        parameter12 == parameter21                                                                                           ->
            simplifyFormal(parameter12 * (parameter11 - parameter22))

        // f1.f2 - f3.f2 -> f2.(f1 - f3)
        parameter12 == parameter22                                                                                           ->
            simplifyFormal(parameter12 * (parameter11 - parameter21))

        parameter11 is CosineFormal && parameter12 is CosineFormal && parameter21 is SineFormal && parameter22 is SineFormal ->
            simplifyCosCos_SinSin(parameter11, parameter12, parameter21, parameter22)

        parameter11 is SineFormal && parameter12 is SineFormal && parameter21 is CosineFormal && parameter22 is CosineFormal ->
            simplifySinSin_CosCos(parameter11, parameter12, parameter21, parameter22)

        parameter11 is CosineFormal && parameter12 is SineFormal && parameter21 is CosineFormal && parameter22 is SineFormal ->
            simplifyCosSin_CosSin(parameter11, parameter12, parameter21, parameter22)

        parameter11 is SineFormal && parameter12 is CosineFormal && parameter21 is CosineFormal && parameter22 is SineFormal ->
            simplifyCosSin_CosSin(parameter12, parameter11, parameter21, parameter22)

        parameter11 is CosineFormal && parameter12 is SineFormal && parameter21 is SineFormal && parameter22 is CosineFormal ->
            simplifyCosSin_CosSin(parameter11, parameter12, parameter22, parameter21)

        parameter11 is SineFormal && parameter12 is CosineFormal && parameter21 is SineFormal && parameter22 is CosineFormal ->
            simplifyCosSin_CosSin(parameter12, parameter11, parameter22, parameter21)

        else                                                                                                                 ->
            (simplifyFormal(parameter11) * simplifyFormal(parameter12)) - (simplifyFormal(parameter21) * simplifyFormal(parameter22))
    }

private fun simplifyCosCos_SinSin(cosine1: CosineFormal, cosine2: CosineFormal, sine1: SineFormal, sine2: SineFormal): FunctionFormal<*>
{
    val parameterCos1 = cosine1.parameter
    val parameterCos2 = cosine2.parameter
    val parameterSin1 = sine1.parameter
    val parameterSin2 = sine2.parameter

    return when
    {
        // cos(F1) * cos(F2) - sin(F1) * sin(F2) -> cos(F1+F2)
        parameterCos1 == parameterSin1 && parameterCos2 == parameterSin2 ->
            cos(simplifyFormal(parameterCos1 + parameterCos2))

        // cos(F1) * cos(F2) - sin(F2) * sin(F1) -> cos(F1+F2)
        parameterCos1 == parameterSin2 && parameterCos2 == parameterSin1 ->
            cos(simplifyFormal(parameterCos1 + parameterCos2))

        else                                                             ->
            simplifyFormal(cosine1) * simplifyFormal(cosine2) - simplifyFormal(sine1) * simplifyFormal(sine2)
    }
}

private fun simplifySinSin_CosCos(sine1: SineFormal, sine2: SineFormal, cosine1: CosineFormal, cosine2: CosineFormal): FunctionFormal<*>
{
    val parameterSin1 = sine1.parameter
    val parameterSin2 = sine2.parameter
    val parameterCos1 = cosine1.parameter
    val parameterCos2 = cosine2.parameter

    return when
    {
        //  sin(F1) * sin(F2)  - cos(F1) * cos(F2) -> -cos(F1+F2)
        parameterSin1 == parameterCos1 && parameterSin2 == parameterCos2 ->
            -cos(simplifyFormal(parameterSin1 + parameterSin2))

        //  sin(F1) * sin(F2)  - cos(F2) * cos(F1) -> -cos(F1+F2)
        parameterSin1 == parameterCos2 && parameterSin2 == parameterCos1 ->
            -cos(simplifyFormal(parameterSin1 + parameterSin2))

        else                                                             ->
            simplifyFormal(sine1) * simplifyFormal(sine2) - simplifyFormal(cosine1) * simplifyFormal(cosine2)
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
        // cos(F1) * sin(F2) - cos(F2) * sin(F1)  -> sin(F2-F1)
        parameterCos1 == parameterSin2 && parameterSin1 == parameterCos2 ->
            sin(simplifyFormal(parameterCos1 - parameterSin1))

        else                                                             ->
            simplifyFormal(cosine1) * simplifyFormal(sine1) - simplifyFormal(cosine2) * simplifyFormal(sine2)
    }
}

private fun simplifySubtraction(division1: DivisionFormal, division2: DivisionFormal): FunctionFormal<*> =
    simplifyFormal(((division1.parameter1 * division2.parameter2) - (division2.parameter1 * division1.parameter2)) / (division1.parameter2 * division2.parameter2))

private fun simplifySubtraction(function: FunctionFormal<*>, division: DivisionFormal): FunctionFormal<*> =
    simplifyFormal((function * division.parameter2 - division.parameter1) / division.parameter2)

private fun simplifySubtraction(division: DivisionFormal, function: FunctionFormal<*>): FunctionFormal<*> =
    simplifyFormal((division.parameter1 - function * division.parameter2) / division.parameter2)