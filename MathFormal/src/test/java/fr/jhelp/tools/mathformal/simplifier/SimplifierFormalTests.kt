package fr.jhelp.tools.mathformal.simplifier

import fr.jhelp.tools.mathformal.dsl.MINUS_ONE
import fr.jhelp.tools.mathformal.dsl.ONE
import fr.jhelp.tools.mathformal.dsl.PI
import fr.jhelp.tools.mathformal.dsl.UNDEFINED
import fr.jhelp.tools.mathformal.dsl.W
import fr.jhelp.tools.mathformal.dsl.X
import fr.jhelp.tools.mathformal.dsl.Y
import fr.jhelp.tools.mathformal.dsl.Z
import fr.jhelp.tools.mathformal.dsl.ZERO
import fr.jhelp.tools.mathformal.dsl.constant
import fr.jhelp.tools.mathformal.dsl.cos
import fr.jhelp.tools.mathformal.dsl.div
import fr.jhelp.tools.mathformal.dsl.minus
import fr.jhelp.tools.mathformal.dsl.plus
import fr.jhelp.tools.mathformal.dsl.sin
import fr.jhelp.tools.mathformal.dsl.times
import fr.jhelp.tools.mathformal.dsl.unaryMinus
import org.junit.Assert
import org.junit.Test

class SimplifierFormalTests
{
    private val simplificationTest = listOf(
        PI to PI,
        123.constant to 123.constant,
        UNDEFINED to UNDEFINED,
        X - X to ZERO,
        X + Y to X + Y,
        X - Y to X - Y,
        X + 3 - (X - 3) to 6.constant,
        X + Y - cos(Z + sin(W - UNDEFINED)) to UNDEFINED,
        3 * X + 2 * Y + 2 * X to 5 * X + 2 * Y,
        3 * X + 2 * Y - 2 * X to X + 2 * Y,
        (10 * X * Y) / (2 * X) to (5 * Y),
        (10 * X * Y) / sin(PI) to UNDEFINED
                                           )

    @Test
    fun simplifyConstant()
    {
        Assert.assertEquals(PI, PI.simplified)
        Assert.assertEquals(123.0.constant, 123.0.constant.simplified)
        Assert.assertEquals(UNDEFINED, UNDEFINED.simplified)
    }

    @Test
    fun simplifyVariable()
    {
        Assert.assertEquals(X, X.simplified)
        Assert.assertEquals(Y, Y.simplified)
    }

    @Test
    fun simplifyUnaryMinus()
    {
        Assert.assertEquals(X, (-(-X)).simplified)
        Assert.assertEquals(-X, (-X).simplified)
        Assert.assertEquals((-123.0).constant, (-(123.0.constant)).simplified)
    }

    @Test
    fun simplifyCosine()
    {
        Assert.assertEquals(cos(X), cos(X).simplified)
        Assert.assertEquals(cos(X), cos(-(X)).simplified)
        Assert.assertEquals(ONE, cos(ZERO).simplified)
        Assert.assertEquals(ZERO, cos(kotlin.math.PI / 2.0).simplified)
        Assert.assertEquals(MINUS_ONE, cos(PI).simplified)
    }

    @Test
    fun simplifySine()
    {
        Assert.assertEquals(sin(X), sin(X).simplified)
        Assert.assertEquals(-sin(X), sin(-X).simplified)
        Assert.assertEquals(ZERO, sin(ZERO).simplified)
        Assert.assertEquals(ONE, sin(kotlin.math.PI / 2.0).simplified)
        Assert.assertEquals(ZERO, sin(PI).simplified)
    }

    @Test
    fun simplifyAddition()
    {
        Assert.assertEquals(ZERO, (ONE + MINUS_ONE).simplified)
        Assert.assertEquals(3 + X, (1 + X + 2).simplified)
        Assert.assertEquals(3 + (X + Y),
                            (1 + X + 1 + Y + 1)
                                .simplify(original = { function -> println(function) },
                                          step = { function -> println("-> $function") },
                                          simplified = { function -> println("=> $function \n") }))
    }

    @Test
    fun simplifySubtraction()
    {
        Assert.assertEquals(2.constant, (ONE - MINUS_ONE).simplified)
        Assert.assertEquals(-1 - X, (1 - X - 2).simplify(original = { function -> println(function) },
                                                         step = { function -> println("-> $function") },
                                                         simplified = { function -> println("=> $function \n") }))
        Assert.assertEquals(-1 - (X + Y),
                            (1 - X - 1 - Y - 1).simplify(original = { function -> println(function) },
                                                         step = { function -> println("-> $function") },
                                                         simplified = { function -> println("=> $function \n") }))
    }

    @Test
    fun simplifyMultiplication()
    {
        Assert.assertEquals(MINUS_ONE, (ONE * MINUS_ONE).simplified)
        Assert.assertEquals(2 * X, (1 * X * 2).simplify(original = { function -> println(function) },
                                                        step = { function -> println("-> $function") },
                                                        simplified = { function -> println("=> $function \n") }))
    }

    @Test
    fun simplifyTests()
    {
        for ((formal, simplified) in this.simplificationTest)
        {
            Assert.assertEquals("Wrong simplification for $formal",
                                simplified,
                                formal.simplify(original = { function -> println(function) },
                                                step = { function -> println("-> $function") },
                                                simplified = { function -> println("=> $function \n") }))
        }
    }
}