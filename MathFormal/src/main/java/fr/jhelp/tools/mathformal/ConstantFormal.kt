package fr.jhelp.tools.mathformal

import fr.jhelp.tools.mathformal.dsl.compareFormal
import fr.jhelp.tools.mathformal.dsl.sameFormal
import java.util.Objects

/**
 * A constant formal is a function that represents a constant.
 */
data class ConstantFormal internal constructor(val value: Double) : FunctionFormal<ConstantFormal>()
{
    override fun equalsIntern(functionFormal: ConstantFormal): Boolean =
        when
        {
            this.value.isNaN()           -> functionFormal.value.isNaN()
            functionFormal.value.isNaN() -> false
            else                         -> this.value.sameFormal(functionFormal.value)
        }

    override fun hasCodeIntern(): Int =
        Objects.hash(this.value)

    override fun compareToIntern(functionFormal: ConstantFormal): Int =
        this.value.compareFormal(functionFormal.value)
}