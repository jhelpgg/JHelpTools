package fr.jhelp.tools.mathformal.simplifier.sumcollector

import fr.jhelp.tools.mathformal.FunctionFormal

/**
 * Try to factorize a function like a sum if possible.
 *
 * @param functionFormal The function to factorize.
 * @return The factorized function.
 */
internal fun tryFactorizeSum(functionFormal: FunctionFormal<*>) : FunctionFormal<*>
{
    val collectedSum = collectSum(functionFormal)
    val factorizedSum = factorizeIfPossible(collectedSum)
    return recreateFunctionFormal(factorizedSum)
}
