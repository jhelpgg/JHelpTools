package fr.jhelp.tools.mathformal

data class VariableFormal internal constructor(val name:String) : FunctionFormal<VariableFormal>()
{
    override fun equalsIntern(functionFormal: VariableFormal): Boolean =
        this.name == functionFormal.name

    override fun hasCodeIntern(): Int =
        this.name.hashCode()

    override fun compareToIntern(functionFormal: VariableFormal): Int =
        this.name.compareTo(functionFormal.name)
}