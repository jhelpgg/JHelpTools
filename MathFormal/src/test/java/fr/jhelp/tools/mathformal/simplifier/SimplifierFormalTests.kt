package fr.jhelp.tools.mathformal.simplifier

import fr.jhelp.tools.mathformal.CosineFormal
import fr.jhelp.tools.mathformal.SineFormal
import fr.jhelp.tools.mathformal.UnaryMinusFormal
import fr.jhelp.tools.mathformal.dsl.MINUS_ONE
import fr.jhelp.tools.mathformal.dsl.ONE
import fr.jhelp.tools.mathformal.dsl.PI
import fr.jhelp.tools.mathformal.dsl.UNDEFINED
import fr.jhelp.tools.mathformal.dsl.X
import fr.jhelp.tools.mathformal.dsl.Y
import fr.jhelp.tools.mathformal.dsl.Z
import fr.jhelp.tools.mathformal.dsl.ZERO
import fr.jhelp.tools.mathformal.dsl.constant
import fr.jhelp.tools.mathformal.dsl.cos
import fr.jhelp.tools.mathformal.dsl.minus
import fr.jhelp.tools.mathformal.dsl.plus
import fr.jhelp.tools.mathformal.dsl.*
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
        X + Y - cos(Z + sin(W - UNDEFINED)) to UNDEFINED
                                           )

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
        Assert.assertEquals(constant(-123.0), simplifyFormal(UnaryMinusFormal(constant(123.0))))
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
        Assert.assertEquals(3 + (X + Y),
                            (1 + X + 1 + Y + 1)
                                .simplify(original = { function -> println(function) },
                                          step = { function -> println("-> $function") },
                                          simplified = { function -> println("=> $function \n") }))
    }

    @Test
    fun simplifySubtraction()
    {
        Assert.assertEquals(2.constant, simplifyFormal(ONE - MINUS_ONE))
        Assert.assertEquals(-1 - X, (1 - X - 2).simplify(original = { function -> println(function) },
                                                         step = { function -> println("-> $function") },
                                                         simplified = { function -> println("=> $function \n") }))
        Assert.assertEquals(-1 - (X + Y),
                            (1 - X - 1 - Y - 1).simplify(original = { function -> println(function) },
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