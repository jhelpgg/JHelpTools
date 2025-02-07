package fr.jhelp.tools.mathformal.simplifier

import fr.jhelp.tools.mathformal.ConstantFormal
import fr.jhelp.tools.mathformal.CosineFormal
import fr.jhelp.tools.mathformal.SineFormal
import fr.jhelp.tools.mathformal.UnaryMinusFormal
import fr.jhelp.tools.mathformal.dsl.MINUS_ONE
import fr.jhelp.tools.mathformal.dsl.ONE
import fr.jhelp.tools.mathformal.dsl.PI
import fr.jhelp.tools.mathformal.dsl.UNDEFINED
import fr.jhelp.tools.mathformal.dsl.X
import fr.jhelp.tools.mathformal.dsl.Y
import fr.jhelp.tools.mathformal.dsl.ZERO
import fr.jhelp.tools.mathformal.dsl.constant
import fr.jhelp.tools.mathformal.dsl.plus
import fr.jhelp.tools.mathformal.dsl.variable
import org.junit.Assert
import org.junit.Test

class SimplifierFormalTests
{
    @Test
    fun simplifyConstant()
    {
        Assert.assertEquals(PI, simplifyFormal(PI))
        Assert.assertEquals(constant(123.0), simplifyFormal(constant(123.0)))
        Assert.assertEquals(UNDEFINED, simplifyFormal(UNDEFINED))
    }

    @Test
    fun simplifyVariable()
    {
        Assert.assertEquals(variable("x"), simplifyFormal(variable("x")))
        Assert.assertEquals(variable("y"), simplifyFormal(variable("y")))
    }

    @Test
    fun simplifyUnaryMinus()
    {
        Assert.assertEquals(variable("X"), simplifyFormal(UnaryMinusFormal(UnaryMinusFormal(variable("X")))))
        Assert.assertEquals(UnaryMinusFormal(variable("X")), simplifyFormal(UnaryMinusFormal(variable("X"))))
        Assert.assertEquals(ConstantFormal(-123.0), simplifyFormal(UnaryMinusFormal(ConstantFormal(123.0))))
    }

    @Test
    fun simplifyCosine()
    {
        Assert.assertEquals(CosineFormal(variable("X")), simplifyFormal(CosineFormal(variable("X"))))
        Assert.assertEquals(CosineFormal(variable("X")), simplifyFormal(CosineFormal(UnaryMinusFormal(variable("X")))))
        Assert.assertEquals(ONE, simplifyFormal(CosineFormal(ZERO)))
        Assert.assertEquals(ZERO, simplifyFormal(CosineFormal(constant(kotlin.math.PI / 2.0))))
        Assert.assertEquals(MINUS_ONE, simplifyFormal(CosineFormal(PI)))
    }

    @Test
    fun simplifySine()
    {
        Assert.assertEquals(SineFormal(variable("X")), simplifyFormal(SineFormal(variable("X"))))
        Assert.assertEquals(UnaryMinusFormal(SineFormal(variable("X"))), simplifyFormal(SineFormal(UnaryMinusFormal(variable("X")))))
        Assert.assertEquals(ZERO, simplifyFormal(SineFormal(ZERO)))
        Assert.assertEquals(ONE, simplifyFormal(SineFormal(constant(kotlin.math.PI / 2.0))))
        Assert.assertEquals(ZERO, simplifyFormal(SineFormal(PI)))
    }

    @Test
    fun simplifyAddition()
    {
        Assert.assertEquals(ZERO, simplifyFormal(ONE + MINUS_ONE))
        Assert.assertEquals(3 + X, simplifyFormal(1 + X + 2))
        Assert.assertEquals(3 + (X + Y), (1 + X + 1 + Y + 1).simplify)
    }
}