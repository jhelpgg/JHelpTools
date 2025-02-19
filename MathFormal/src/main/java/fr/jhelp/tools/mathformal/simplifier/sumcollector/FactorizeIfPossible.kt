package fr.jhelp.tools.mathformal.simplifier.sumcollector

import fr.jhelp.tools.mathformal.FunctionFormal
import fr.jhelp.tools.mathformal.MultiplicationFormal
import fr.jhelp.tools.mathformal.dsl.ZERO
import fr.jhelp.tools.mathformal.dsl.minus
import fr.jhelp.tools.mathformal.dsl.plus
import fr.jhelp.tools.mathformal.dsl.times
import fr.jhelp.tools.mathformal.simplifier.sumcollector.factorizeResult.FactorizeResult
import fr.jhelp.tools.mathformal.simplifier.sumcollector.factorizeResult.Factorized
import fr.jhelp.tools.mathformal.simplifier.sumcollector.factorizeResult.NotFactorized

/**
 * Try to factorize a generic sum of functions.
 *
 * @param sumCollected The sum to factorize.
 * @return The factorized sum.
 */
internal fun factorizeIfPossible(sumCollected: MutableList<SumCollectedElement>): List<SumCollectedElement>
{
    var indexActual = 0

    while (indexActual < sumCollected.size - 1)
    {
        var actual = sumCollected[indexActual]
        var indexOther = indexActual + 1

        while (indexOther < sumCollected.size)
        {
            val other = sumCollected[indexOther]
            val factorized = factorize(actual, other)

            if (factorized is Factorized)
            {
                sumCollected.removeAt(indexOther)
                sumCollected.removeAt(indexActual)
                sumCollected.add(indexActual, factorized.factorization)
                actual = factorized.factorization
                continue
            }

            indexOther++
        }

        indexActual++
    }

    return sumCollected
}

/**
 * Try to factorize two functions.
 *
 * @param sumCollectedElement1 The first function.
 * @param sumCollectedElement2 The second function.
 * @return The result of the factorization.
 */
private fun factorize(sumCollectedElement1: SumCollectedElement, sumCollectedElement2: SumCollectedElement): FactorizeResult
{
    val function1 = sumCollectedElement1.functionFormal
    val function2 = sumCollectedElement2.functionFormal

    return when
    {
        function1 is MultiplicationFormal ->
            when (function2)
            {
                is MultiplicationFormal ->
                    when
                    {
                        function1.parameter1 == function2.parameter1 ->
                            factorizeTwoMultiplications(function1.parameter1,
                                                        sumCollectedElement1.positive, function1.parameter2,
                                                        sumCollectedElement2.positive, function2.parameter2)

                        function1.parameter1 == function2.parameter2 ->
                            factorizeTwoMultiplications(function1.parameter1,
                                                        sumCollectedElement1.positive, function1.parameter2,
                                                        sumCollectedElement2.positive, function2.parameter1)

                        function1.parameter2 == function2.parameter1 ->
                            factorizeTwoMultiplications(function1.parameter2,
                                                        sumCollectedElement1.positive, function1.parameter1,
                                                        sumCollectedElement2.positive, function2.parameter2)

                        function1.parameter2 == function2.parameter2 ->
                            factorizeTwoMultiplications(function1.parameter2,
                                                        sumCollectedElement1.positive, function1.parameter1,
                                                        sumCollectedElement2.positive, function2.parameter1)

                        else                                         ->
                            NotFactorized
                    }

                function1.parameter1    ->
                    factorizeMultiplicationWithSomething(sumCollectedElement1.positive, function1.parameter2,
                                                         sumCollectedElement2.positive, function2)

                function1.parameter2    ->
                    factorizeMultiplicationWithSomething(sumCollectedElement1.positive, function1.parameter1,
                                                         sumCollectedElement2.positive, function2)

                else                    ->
                    NotFactorized
            }

        function2 is MultiplicationFormal ->
            when (function1)
            {
                function2.parameter1 ->
                    factorizeSomethingWithMultiplication(sumCollectedElement1.positive, function1,
                                                         sumCollectedElement2.positive, function2.parameter2)

                function2.parameter2 ->
                    factorizeSomethingWithMultiplication(sumCollectedElement1.positive, function1,
                                                         sumCollectedElement2.positive, function2.parameter1)

                else                 ->
                    NotFactorized
            }

        function1 == function2            ->
            when
            {
                sumCollectedElement1.positive == sumCollectedElement2.positive ->
                    Factorized(SumCollectedElement(2 * function1, positive = sumCollectedElement1.positive))

                else                                                           ->
                    Factorized(SumCollectedElement(ZERO, positive = true))
            }

        else                              ->
            NotFactorized
    }
}

/**
 * Factorize two multiplications with a common factor
 *
 * @param commonFactor The common factor.
 * @param firstPositive If the first multiplication is positive.
 * @param firstParameter The parameter of first multiplication that is not the common factor.
 * @param secondPositive If the second multiplication is positive.
 * @param secondParameter The parameter of second multiplication that is not the common factor.
 *  @return The result of the factorization.
 */
private fun factorizeTwoMultiplications(commonFactor: FunctionFormal<*>,
                                        firstPositive: Boolean, firstParameter: FunctionFormal<*>,
                                        secondPositive: Boolean, secondParameter: FunctionFormal<*>): FactorizeResult =
    when
    {
        firstPositive && secondPositive ->
            Factorized(SumCollectedElement(commonFactor * (firstParameter + secondParameter), positive = true))

        firstPositive                   ->
            Factorized(SumCollectedElement(commonFactor * (firstParameter - secondParameter), positive = true))

        secondPositive                  ->
            Factorized(SumCollectedElement(commonFactor * (secondParameter - firstParameter), positive = true))

        else                            ->
            Factorized(SumCollectedElement(commonFactor * (firstParameter + secondParameter), positive = false))
    }

/**
 * Factorize something with a multiplication where one of multiplication parameters is equals to the common factor.
 * @param somethingPositive If the something is positive.
 * @param commonFactor The common factor.
 * @param multiplicationPositive If the multiplication is positive.
 *  @param multiplicationParameter The parameter of multiplication that is not the common factor.
 *  @return The result of the factorization.
 */
private fun factorizeSomethingWithMultiplication(somethingPositive: Boolean, commonFactor: FunctionFormal<*>,
                                                 multiplicationPositive: Boolean, multiplicationParameter: FunctionFormal<*>): FactorizeResult =
    when
    {
        somethingPositive && multiplicationPositive ->
            Factorized(SumCollectedElement(commonFactor * (1 + multiplicationParameter), positive = true))

        somethingPositive                           ->
            Factorized(SumCollectedElement(commonFactor * (1 - multiplicationParameter), positive = true))

        multiplicationPositive                      ->
            Factorized(SumCollectedElement(commonFactor * (multiplicationParameter - 1), positive = true))

        else                                        ->
            Factorized(SumCollectedElement(commonFactor * (1 + multiplicationParameter), positive = false))
    }

/**
 * Factorize multiplication with something where one of multiplication parameters is equals to the common factor.
 *
 * @param multiplicationPositive If the multiplication is positive.
 * @param multiplicationParameter The parameter of multiplication that is not the common factor.
 * @param somethingPositive If the something is positive.
 * @param commonFactor The common factor.
 * @return The result of the factorization.
 */
private fun factorizeMultiplicationWithSomething(multiplicationPositive: Boolean, multiplicationParameter: FunctionFormal<*>,
                                                 somethingPositive: Boolean, commonFactor: FunctionFormal<*>): FactorizeResult =
    when
    {
        multiplicationPositive && somethingPositive ->
            Factorized(SumCollectedElement(commonFactor * (multiplicationParameter + 1), positive = true))

        multiplicationPositive                      ->
            Factorized(SumCollectedElement(commonFactor * (multiplicationParameter - 1), positive = true))

        somethingPositive                           ->
            Factorized(SumCollectedElement(commonFactor * (1 - multiplicationParameter), positive = true))

        else                                        ->
            Factorized(SumCollectedElement(commonFactor * (multiplicationParameter + 1), positive = false))
    }