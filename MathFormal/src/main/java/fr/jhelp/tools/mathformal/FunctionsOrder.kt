package fr.jhelp.tools.mathformal

private val functionsOrder: Array<Class<out FunctionFormal<*>>> =
    arrayOf(ConstantFormal::class.java,
            VariableFormal::class.java,
            UnaryMinusFormal::class.java,
            CosineFormal::class.java,
            SineFormal::class.java,
            MultiplicationFormal::class.java,
            AdditionFormal::class.java,
            SubtractionFormal::class.java)

internal val FunctionFormal<*>.order: Int
    get() =
        functionsOrder.indexOf(this::class.java)
