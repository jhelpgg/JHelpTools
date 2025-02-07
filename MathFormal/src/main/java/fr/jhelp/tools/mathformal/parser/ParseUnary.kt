package fr.jhelp.tools.mathformal.parser

import fr.jhelp.tools.mathformal.CosineFormal
import fr.jhelp.tools.mathformal.SineFormal
import fr.jhelp.tools.mathformal.UnaryMinusFormal
import fr.jhelp.tools.mathformal.UnaryOperatorFormal
import java.util.Optional

internal fun parseUnary(string: String): Optional<UnaryOperatorFormal<*>> =
    when
    {
        string.startsWith("-")   ->
            if (string[1] == '(')
            {
                Optional.of(UnaryMinusFormal(parseFormal(extractInsideParenthesis(string, 1).first)))
            }
            else
            {
                Optional.of(UnaryMinusFormal(parseFormal(string.substring(1))))
            }

        string.startsWith("cos") ->
            Optional.of(CosineFormal(parseFormal(extractInsideParenthesis(string, 3).first)))

        string.startsWith("sin") ->
            Optional.of(SineFormal(parseFormal(extractInsideParenthesis(string, 3).first)))

        else                     -> Optional.empty()
    }