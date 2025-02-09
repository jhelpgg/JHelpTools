package fr.jhelp.tools.mathformal.parser

import fr.jhelp.tools.mathformal.UnaryOperatorFormal
import fr.jhelp.tools.mathformal.dsl.cos
import fr.jhelp.tools.mathformal.dsl.sin
import fr.jhelp.tools.mathformal.dsl.unaryMinus
import java.util.Optional

internal fun parseUnary(string: String): Optional<UnaryOperatorFormal<*>> =
    when
    {
        string.startsWith("-")   ->
            if (string[1] == '(')
            {
                Optional.of(-(parseFormal(extractInsideParenthesis(string, 1).first)))
            }
            else
            {
                Optional.of(-(parseFormal(string.substring(1))))
            }

        string.startsWith("cos") ->
            Optional.of(cos(parseFormal(extractInsideParenthesis(string, 3).first)))

        string.startsWith("sin") ->
            Optional.of(sin(parseFormal(extractInsideParenthesis(string, 3).first)))

        else                     -> Optional.empty()
    }