package fr.jhelp.tools.mathformal

sealed class UnaryOperatorFormal<UO : UnaryOperatorFormal<UO>>(val parameter: FunctionFormal<*>, val symbol: String, val alwaysParenthesis: Boolean) : FunctionFormal<UO>()
{
    final override fun equalsIntern(functionFormal: UO): Boolean =
        this.parameter == functionFormal.parameter

    final override fun hasCodeIntern(): Int =
        this.parameter.hashCode()

    final override fun compareToIntern(functionFormal: UO): Int =
        this.parameter.compareTo(functionFormal.parameter)
}