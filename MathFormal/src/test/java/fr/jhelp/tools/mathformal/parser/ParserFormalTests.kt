package fr.jhelp.tools.mathformal.parser

import fr.jhelp.tools.mathformal.AdditionFormal
import fr.jhelp.tools.mathformal.ConstantFormal
import fr.jhelp.tools.mathformal.CosineFormal
import fr.jhelp.tools.mathformal.SineFormal
import fr.jhelp.tools.mathformal.SubtractionFormal
import fr.jhelp.tools.mathformal.UnaryMinusFormal
import fr.jhelp.tools.mathformal.VariableFormal
import fr.jhelp.tools.mathformal.dsl.PI
import fr.jhelp.tools.mathformal.dsl.UNDEFINED
import fr.jhelp.tools.mathformal.dsl.X
import fr.jhelp.tools.mathformal.dsl.Y
import fr.jhelp.tools.mathformal.dsl.Z
import fr.jhelp.tools.mathformal.dsl.constant
import fr.jhelp.tools.mathformal.dsl.cos
import fr.jhelp.tools.mathformal.dsl.minus
import fr.jhelp.tools.mathformal.dsl.plus
import fr.jhelp.tools.mathformal.dsl.sin
import fr.jhelp.tools.test.assertInstance
import fr.jhelp.tools.utilities.math.EPSILON
import org.junit.Assert
import org.junit.Test

class ParserFormalTests
{
    private val parseTest = listOf(
        "PI" to PI,
        "123" to 123.constant,
        "x" to X,
        "y" to Y,
        "x + y" to X + Y,
        "x - y" to X - Y,
        "x + y - z" to (X + Y) - Z,
        "x - cos(y + sin(z))" to X - cos(Y + sin(Z)),
        "x - y + z" to X - (Y + Z)
                                  )

    @Test
    fun parseConstant()
    {
        var formal = parseFormal("PI")
        var constant = formal.assertInstance<ConstantFormal>()
        Assert.assertEquals(Math.PI, constant.value, EPSILON)

        formal = parseFormal("123")
        constant = formal.assertInstance<ConstantFormal>()
        Assert.assertEquals(123.0, constant.value, EPSILON)
    }

    @Test
    fun parseVariable()
    {
        var formal = parseFormal("x")
        var variable = formal.assertInstance<VariableFormal>()
        Assert.assertEquals("x", variable.name)

        formal = parseFormal("y")
        variable = formal.assertInstance<VariableFormal>()
        Assert.assertEquals("y", variable.name)
    }

    @Test
    fun parseUnaryMinus()
    {
        val formal = parseFormal("-X")
        val unaryMinus = formal.assertInstance<UnaryMinusFormal>()
        val variable = unaryMinus.parameter.assertInstance<VariableFormal>()
        Assert.assertEquals("X", variable.name)
    }

    @Test
    fun parseCosine()
    {
        val formal = parseFormal("cos(X)")
        val cosine = formal.assertInstance<CosineFormal>()
        val variable = cosine.parameter.assertInstance<VariableFormal>()
        Assert.assertEquals("X", variable.name)
    }

    @Test
    fun parseSine()
    {
        val formal = parseFormal("sin(X)")
        val sine = formal.assertInstance<SineFormal>()
        val variable = sine.parameter.assertInstance<VariableFormal>()
        Assert.assertEquals("X", variable.name)
    }

    @Test
    fun `parse X + Y`()
    {
        val formal = parseFormal("X + Y")
        val addition = formal.assertInstance<AdditionFormal>()
        val parameter1 = addition.parameter1.assertInstance<VariableFormal>()
        val parameter2 = addition.parameter2.assertInstance<VariableFormal>()
        Assert.assertEquals("X", parameter1.name)
        Assert.assertEquals("Y", parameter2.name)
    }

    @Test
    fun `parse X + Y + cos(a+b) + Z + (T+W)`()
    {
        val formal = parseFormal("X + Y + cos(a+b) + Z + (T+W)")

        val additionXPlusSomething = formal.assertInstance<AdditionFormal>()
        val parameterX = additionXPlusSomething.parameter1.assertInstance<VariableFormal>()
        Assert.assertEquals("X", parameterX.name)

        val additionYPlusSomething = additionXPlusSomething.parameter2.assertInstance<AdditionFormal>()
        val parameterY = additionYPlusSomething.parameter1.assertInstance<VariableFormal>()
        Assert.assertEquals("Y", parameterY.name)

        val additionCosSomething = additionYPlusSomething.parameter2.assertInstance<AdditionFormal>()
        val parameterCos = additionCosSomething.parameter1.assertInstance<CosineFormal>()
        val additionAPlusB = parameterCos.parameter.assertInstance<AdditionFormal>()
        val parameterA = additionAPlusB.parameter1.assertInstance<VariableFormal>()
        Assert.assertEquals("a", parameterA.name)
        val parameterB = additionAPlusB.parameter2.assertInstance<VariableFormal>()
        Assert.assertEquals("b", parameterB.name)

        val additionZPlusSomething = additionCosSomething.parameter2.assertInstance<AdditionFormal>()
        val parameterZ = additionZPlusSomething.parameter1.assertInstance<VariableFormal>()
        Assert.assertEquals("Z", parameterZ.name)

        val additionTPlusW = additionZPlusSomething.parameter2.assertInstance<AdditionFormal>()
        val parameterT = additionTPlusW.parameter1.assertInstance<VariableFormal>()
        Assert.assertEquals("T", parameterT.name)
        val parameterW = additionTPlusW.parameter2.assertInstance<VariableFormal>()
        Assert.assertEquals("W", parameterW.name)
    }

    @Test
    fun `parse X - Y`()
    {
        val formal = parseFormal("X - Y")
        val subtraction = formal.assertInstance<SubtractionFormal>()
        val parameter1 = subtraction.parameter1.assertInstance<VariableFormal>()
        val parameter2 = subtraction.parameter2.assertInstance<VariableFormal>()
        Assert.assertEquals("X", parameter1.name)
        Assert.assertEquals("Y", parameter2.name)
    }

    // TODO other tests

    @Test
    fun parseTests()
    {
        for ((string, formal) in this.parseTest)
        {
            Assert.assertEquals("Wrong parsing for '$string'", formal, parseFormal(string))
        }
    }

    @Test
    fun parseInvalid()
    {
        Assert.assertEquals(UNDEFINED, parseFormal("*"))
    }
}