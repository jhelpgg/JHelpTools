package fr.jhelp.tools.mathformal.dsl

import fr.jhelp.tools.mathformal.ConstantFormal
import fr.jhelp.tools.utilities.collections.cache.CacheSizeLimit
import fr.jhelp.tools.utilities.extensions.onPresentOtherwise

internal const val PRECISION = 1e-15
internal fun Double.sameFormal(other: Double): Boolean = kotlin.math.abs(this - other) < PRECISION
internal val Double.nulFormal: Boolean
    get() =
        this.sameFormal(0.0)
internal val Double.signFormal: Int
    get() =
        when
        {
            this.nulFormal -> 0
            this > 0       -> 1
            else           -> -1
        }

internal fun Double.compareFormal(other: Double): Int =
    when
    {
        this.isNaN() || this.isInfinite()   -> if (other.isNaN() || other.isInfinite()) 0 else 1

        other.isNaN() || other.isInfinite() -> -1

        else                                -> (this - other).signFormal
    }

val UNDEFINED = ConstantFormal(Double.NaN)
val ZERO = ConstantFormal(0.0)
val ONE = ConstantFormal(1.0)
val MINUS_ONE = ConstantFormal(-1.0)
val PI = ConstantFormal(Math.PI)
val E = ConstantFormal(Math.E)

private val constantsCache = CacheSizeLimit<Double, ConstantFormal>(1024)

fun constant(value: Double): ConstantFormal =
    when
    {
        value.isNaN() || value.isInfinite() -> UNDEFINED
        value.sameFormal(0.0)               -> ZERO
        value.sameFormal(1.0)               -> ONE
        value.sameFormal(-1.0)              -> MINUS_ONE
        value.sameFormal(Math.PI)           -> PI
        value.sameFormal(Math.E)            -> E
        else                                ->
            constantsCache[value].onPresentOtherwise(
                { constant -> constant },
                {
                    val constant = ConstantFormal(value)
                    constantsCache[value] = constant
                    constant
                })
    }

val Number.constant: ConstantFormal get() = constant(this.toDouble())
