package fr.jhelp.tools.mathformal

sealed class BinaryOperatorFormal<BO : BinaryOperatorFormal<BO>>(val parameter1: FunctionFormal<*>, val parameter2: FunctionFormal<*>, val symbol: String, val permutative: Boolean) : FunctionFormal<BO>()
{
    final override fun equalsIntern(functionFormal: BO): Boolean =
        (this.parameter1 == functionFormal.parameter1 && this.parameter2 == functionFormal.parameter2)
                || (this.permutative && this.parameter1 == functionFormal.parameter2 && this.parameter2 == functionFormal.parameter1)

    final override fun hasCodeIntern(): Int =
        this.parameter1.hashCode() + this.parameter2.hashCode() * 31

    final override fun compareToIntern(functionFormal: BO): Int
    {
        val comparison = this.parameter1.compareTo(functionFormal.parameter1)

        if (comparison != 0)
        {
            return comparison
        }

        return this.parameter2.compareTo(functionFormal.parameter2)
    }
}