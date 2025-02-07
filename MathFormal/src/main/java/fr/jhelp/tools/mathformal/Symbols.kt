package fr.jhelp.tools.mathformal

import fr.jhelp.tools.mathformal.dsl.E
import fr.jhelp.tools.mathformal.dsl.PI
import fr.jhelp.tools.mathformal.dsl.UNDEFINED
import java.util.Optional

const val SYMBOL_UNDEFINED = "UNDEFINED"
const val SYMBOL_PI = "PI"
const val SYMBOL_E = "E"

private val symbols :  HashMap<String, FunctionFormal<*>> =
    hashMapOf(SYMBOL_UNDEFINED to UNDEFINED,
              SYMBOL_PI to PI,
              SYMBOL_E to E)

fun associate(symbol:String, functionFormal:FunctionFormal<*>)
{
    if(symbol in arrayOf(SYMBOL_UNDEFINED, SYMBOL_PI, SYMBOL_E))
    {
        return
    }

    symbols[symbol] = functionFormal
}

fun symbol(symbol:String) : Optional<FunctionFormal<*>> =
    Optional.ofNullable(symbols[symbol])

fun symbol(symbol:String, default:FunctionFormal<*>) : FunctionFormal<*> =
    symbols.getOrPut(symbol) { default }

fun obtainSymbolKeyOf(function:FunctionFormal<*>) : Optional<String>
{
    when(function)
    {
        UNDEFINED -> return Optional.of(SYMBOL_UNDEFINED)
        PI        -> return Optional.of(SYMBOL_PI)
        E         -> return Optional.of(SYMBOL_E)
        else       -> Unit
    }

    for((key, value) in symbols)
    {
        if (value == function)
        {
            return Optional.of(key)
        }
    }

    return Optional.empty()
}

