package fr.jhelp.tools.mathformal.derivative

import fr.jhelp.tools.mathformal.FunctionFormal
import fr.jhelp.tools.mathformal.dsl.ONE
import fr.jhelp.tools.mathformal.dsl.X
import fr.jhelp.tools.mathformal.dsl.Y
import fr.jhelp.tools.mathformal.dsl.ZERO
import fr.jhelp.tools.mathformal.dsl.constant
import fr.jhelp.tools.mathformal.dsl.cos
import fr.jhelp.tools.mathformal.dsl.*
import fr.jhelp.tools.mathformal.dsl.sin
import fr.jhelp.tools.mathformal.dsl.times
import fr.jhelp.tools.mathformal.dsl.unaryMinus
import org.junit.Assert
import org.junit.Test

class DerivativeTests
{
    private val derivativesOnX: Array<Pair<FunctionFormal<*>, FunctionFormal<*>>> =
        arrayOf(
            X to ONE,
            Y to ZERO,
            2 * X to 2.constant,
            3 * X * X + 5 * X + 1 to 6 * X + 5,
            cos(X) to -sin(X),
            cos(5 * X) to -5 * sin(5 * X),
            sin(X) to cos(X),
            sin(5 * X) to 5 * cos(5 * X)
               )

    @Test
    fun testDerivatives()
    {
        for ((functionTested, derivative) in derivativesOnX)
        {
            Assert.assertEquals(derivative, functionTested.derivative(X))
        }
    }
}