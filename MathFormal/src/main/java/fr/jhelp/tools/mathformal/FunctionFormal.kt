package fr.jhelp.tools.mathformal

import fr.jhelp.tools.mathformal.serializer.serializeFormal

/**
 * A function formal is a function that can be evaluated in formal way.
 *
 * For example its represents the function `f(X) = 3X + 6` without knowing the value of `X`.
 * If we have an other function `g(X) = 5 - 2X` and do `h(X) = f(X) + g(X)`
 * then `h(X)` will be first evaluate to `h(X) = 3X + 6 + 5 - 2X` and simplify to `h(X) = X + 11`.
 */
sealed class FunctionFormal<FF : FunctionFormal<FF>> : Comparable<FunctionFormal<*>>
{
    final override fun equals(other: Any?): Boolean =
        when
        {
            this === other                                         -> true
            other == null || other::class.java != this::class.java -> false
            else                                                   -> this.equalsIntern(other as FF)
        }

    final override fun hashCode(): Int =
        this::class.java.name.hashCode() + this.hasCodeIntern() * 31

    final override fun toString(): String =
        serializeFormal(this)

    final override fun compareTo(other: FunctionFormal<*>): Int =
        if(this::class.java == other::class.java)
        {
            this.compareToIntern(other as FF)
        }
        else
        {
            this.order.compareTo(other.order)
        }


    protected abstract fun equalsIntern(functionFormal: FF): Boolean
    protected abstract fun hasCodeIntern(): Int
    protected abstract fun compareToIntern(functionFormal: FF) : Int
}