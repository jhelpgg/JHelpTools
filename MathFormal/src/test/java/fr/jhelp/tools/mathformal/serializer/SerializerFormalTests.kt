package fr.jhelp.tools.mathformal.serializer

import fr.jhelp.tools.mathformal.dsl.PI
import fr.jhelp.tools.mathformal.dsl.T
import fr.jhelp.tools.mathformal.dsl.UNDEFINED
import fr.jhelp.tools.mathformal.dsl.W
import fr.jhelp.tools.mathformal.dsl.X
import fr.jhelp.tools.mathformal.dsl.Y
import fr.jhelp.tools.mathformal.dsl.Z
import fr.jhelp.tools.mathformal.dsl.constant
import fr.jhelp.tools.mathformal.dsl.cos
import fr.jhelp.tools.mathformal.dsl.minus
import fr.jhelp.tools.mathformal.dsl.plus
import fr.jhelp.tools.mathformal.dsl.sin
import fr.jhelp.tools.mathformal.dsl.unaryMinus
import fr.jhelp.tools.mathformal.dsl.variable
import org.junit.Assert
import org.junit.Test

class SerializerFormalTests
{
    private val serializeTest = listOf(
        PI to "PI",
        123.constant to "123.0",
        X to "x",
        Y to "y",
        X + Y to "x + y",
        X - Y to "x - y",
        X + (Y - Z) to "x + (y - z)",
        X - cos(Y + sin(Z)) to "x - cos(y + sin(z))",
        X - (Y + Z) to "x - (y + z)"
                                      )

    @Test
    fun serializeConstant()
    {
        Assert.assertEquals("123.0", serializeFormal(constant(123.0)))
        Assert.assertEquals("PI", serializeFormal(PI))
        Assert.assertEquals("UNDEFINED", serializeFormal(UNDEFINED))
    }

    @Test
    fun serializeVariable()
    {
        Assert.assertEquals("x", serializeFormal(X))
        Assert.assertEquals("y", serializeFormal(Y))
    }

    @Test
    fun serializeUnaryMinus()
    {
        Assert.assertEquals("-x", serializeFormal(-X))
    }

    @Test
    fun serializeCosine()
    {
        Assert.assertEquals("cos(x)", serializeFormal(cos(X)))
    }

    @Test
    fun serializeSine()
    {
        Assert.assertEquals("sin(x)", serializeFormal(sin(X)))
    }

    @Test
    fun serializeAddition()
    {
        Assert.assertEquals("x + y", serializeFormal(X + Y))
        Assert.assertEquals("(((x + y) + cos(a + b)) + z) + (t + w)", serializeFormal(X + Y + cos("a".variable + "b") + Z + (T + W)))
    }

    @Test
    fun serializeSubtraction()
    {
        Assert.assertEquals("x - y", serializeFormal(X - Y))
        Assert.assertEquals("(((x - y) - cos(a - b)) - z) - (t - w)", serializeFormal(X - Y - cos("a".variable - "b") - Z - (T - W)))
    }

    @Test
    fun serializeTests()
    {
        for ((formal, string) in this.serializeTest)
        {
            Assert.assertEquals(string, serializeFormal(formal))
        }
    }
}