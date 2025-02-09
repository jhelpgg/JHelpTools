package fr.jhelp.tools.mathformal.parser

import org.junit.Assert
import org.junit.Test
import kotlin.jvm.Throws

class ParserToolsTests
{
    @Test
    fun removeSpacesTest()
    {
        Assert.assertEquals("Hello", removeSpaces("Hello"))
        Assert.assertEquals("Hello", removeSpaces(" \t \n \r Hel \t \n \r lo \t \n \r "))
    }

    @Test
    fun extractInsideParenthesisTest()
    {
        val string ="5(X/(Y-Z))"
        var parameter = extractInsideParenthesis(string, 1)
        Assert.assertEquals("X/(Y-Z)", parameter.first)
        Assert.assertEquals(9, parameter.second)

        try
        {
            extractInsideParenthesis(string, 2)
            Assert.fail("First character index should not be an open parenthesis")
        }
        catch(e:IllegalArgumentException)
        {
            // Is what we expect
        }

        parameter = extractInsideParenthesis(string, 4)
        Assert.assertEquals("Y-Z", parameter.first)
        Assert.assertEquals(8, parameter.second)

        try
        {
            extractInsideParenthesis("5(X/(Y-Z)", 1)
            Assert.fail("Missing a close parenthesis")
        }
        catch(e:IllegalArgumentException)
        {
            // Is what we expect
        }
    }

    @Test
    fun addMultiplicationTests()
    {
        Assert.assertEquals("5*X", addMultiplication("5X"))
        Assert.assertEquals("5.23*X+85*Y+7*cos(4*PI)", addMultiplication("5.23X+85Y+7cos(4PI)"))
    }
}