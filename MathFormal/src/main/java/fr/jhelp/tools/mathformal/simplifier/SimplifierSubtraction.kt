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

internal fun simplifySubtraction(subtraction: SubtractionFormal): FunctionFormal<*>
{
    val parameter1 = subtraction.parameter1
    val parameter2 = subtraction.parameter2

    return when
    {
        parameter1 == UNDEFINED || parameter2 == UNDEFINED           ->
            UNDEFINED

        parameter1 == ZERO                                           ->
            -simplifyFormal(parameter2)

        parameter2 == ZERO                                           ->
            simplifyFormal(parameter1)

        parameter1 == parameter2                                     ->
            ZERO

        parameter1 is ConstantFormal                                 ->
            simplifySubtraction(parameter1, parameter2)

        parameter2 is ConstantFormal                                 ->
            simplifySubtraction(parameter1, parameter2)

        // (-f1) - (-f2) -> f2 - f1
        parameter1 is UnaryMinusFormal && parameter2 is UnaryMinusFormal ->
            simplifyFormal(parameter2.parameter) - simplifyFormal(parameter1.parameter)

        // (-f1) - f2 -> -(f1 + f2)
        parameter1 is UnaryMinusFormal                                ->
            -(simplifyFormal(parameter1.parameter) + simplifyFormal(parameter2))

        // f1 - (-f2) - > f1 + f2
        parameter2 is UnaryMinusFormal                                ->
            parameter1 + simplifyFormal(parameter2.parameter)

        parameter1 is AdditionFormal && parameter2 is AdditionFormal ->
            simplifySubtraction(parameter1, parameter2)

        parameter1 is AdditionFormal ->
        {
            val parameter11 = parameter1.parameter1
            val parameter12 = parameter1.parameter2

            when
            {
                parameter11 == UNDEFINED || parameter12 == UNDEFINED           ->
                    UNDEFINED

                parameter11 == parameter2                                     ->
                    simplifyFormal(parameter12)

                parameter12 == parameter2                                     ->
                    simplifyFormal(parameter11)

                else                                                          ->
                    simplifyFormal(parameter1) - simplifyFormal(parameter2)
            }
        }

        else                                                         ->
            simplifyFormal(parameter1) - simplifyFormal(simplifyFormal(parameter2))
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
        additionParameter1 == UNDEFINED || additionParameter2 == UNDEFINED           ->
            UNDEFINED

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
        subtractionParameter1 == UNDEFINED || subtractionParameter2 == UNDEFINED           ->
            UNDEFINED

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

private fun simplifySubtraction(addition1: AdditionFormal, addition2: AdditionFormal): FunctionFormal<*>
{
    val parameter11 = addition1.parameter1
    val parameter12 = addition1.parameter2
    val parameter21 = addition2.parameter1
    val parameter22 = addition2.parameter2

    // (f1 + f2) - (f3 + f4)
    return when
    {
        parameter11 == UNDEFINED || parameter12 == UNDEFINED || parameter21 == UNDEFINED || parameter22 == UNDEFINED ->
            UNDEFINED

        // (C1 + f2) - (f3 + f4)
        parameter11 is ConstantFormal                                                                                ->
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
        parameter12 is ConstantFormal                                                                                ->
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
        parameter21 is ConstantFormal                                                                                ->
            when
            {
                // (f1 + f2) - (C3 + C4) -> (-C3-C4) + f1 + f2
                parameter22 is ConstantFormal ->
                    constant(-parameter21.value - parameter22.value) + simplifyFormal(parameter11) + simplifyFormal(parameter12)

                // (f1 + f2) - (C3 + f4) -> (-C3) + f1 + f2 - f4
                else ->
                    constant(-parameter21.value) + simplifyFormal(parameter11) + simplifyFormal(parameter12) - simplifyFormal(parameter22)
            }

        // (f1 + f2) - (f3 + C4) -> (-C4) + f1 + f2 - f3
        parameter22 is ConstantFormal ->
            constant(-parameter22.value) + simplifyFormal(parameter11) + simplifyFormal(parameter12) - simplifyFormal(parameter21)

        else                                                                                                         ->
            simplifyFormal(addition1) - simplifyFormal(addition2)
    }
}