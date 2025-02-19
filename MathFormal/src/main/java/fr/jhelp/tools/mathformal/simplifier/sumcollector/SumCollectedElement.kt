package fr.jhelp.tools.mathformal.simplifier.sumcollector

import fr.jhelp.tools.mathformal.FunctionFormal

/**
 * Element of a generic sum, here subtraction ar seen as addition of the opposite of the element.
 * @property functionFormal the function formal of the element
 * @property positive if the element is positive (If not have to take its opposite)
 */
internal class SumCollectedElement(val functionFormal: FunctionFormal<*>, val positive: Boolean)
