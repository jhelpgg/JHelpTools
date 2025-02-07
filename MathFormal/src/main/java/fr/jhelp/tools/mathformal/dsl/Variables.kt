package fr.jhelp.tools.mathformal.dsl

import fr.jhelp.tools.mathformal.VariableFormal
import fr.jhelp.tools.utilities.checkArgument
import fr.jhelp.tools.utilities.collections.cache.CacheSizeLimit
import fr.jhelp.tools.utilities.extensions.onPresentOtherwise
import java.util.regex.Pattern

val X = variable("x")
val Y = variable("y")
val Z = variable("z")
val W = variable("w")
val T = variable("t")

private object VariableManager
{
    val variableCache = CacheSizeLimit<String, VariableFormal>()
    private val variablePattern: Pattern by lazy { Pattern.compile("[A-Za-z][A-Za-z0-9_]*") }
    fun nameValid(name: String): Boolean = this.variablePattern.matcher(name).matches()
}

@Throws(IllegalArgumentException::class)
fun variable(name: String): VariableFormal
{
    VariableManager.nameValid(name).checkArgument("Variable name '$name' is not valid, must start with a letter and contain only letters, digits and underscores")
    return VariableManager.variableCache[name]
        .onPresentOtherwise(
            { variable -> variable },
            {
                val variable = VariableFormal(name)
                VariableManager.variableCache[name] = variable
                variable
            })
}

val String.variable: VariableFormal get() = variable(this)
