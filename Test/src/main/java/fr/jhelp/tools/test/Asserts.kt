package fr.jhelp.tools.test

import org.junit.Assert
import java.util.Optional

inline fun <reified T : Any> assertOptionalEmpty(optional: Optional<T>, message: String = "Optional must be empty")
{
    Assert.assertTrue(message, optional.isEmpty)
}

inline fun <reified T : Any> assertOptionalValue(optional: Optional<T>,
                                                 message: String = "Optional must have a value"): T
{
    Assert.assertTrue(message, optional.isPresent)
    return optional.get()
}

inline fun <reified T : Any> assertResultFailure(result: Result<T>,
                                                 message: String = "Result must be a failure not $result"): Throwable
{
    Assert.assertTrue(message, result.isFailure)
    return result.exceptionOrNull()!!
}

inline fun <reified T : Any> assertResultSuccess(result: Result<T>,
                                                 message: String = "Result must be a result not $result"): T
{
    Assert.assertTrue(message, result.isSuccess)
    return result.getOrNull()!!
}

inline fun <reified I:Any> Any.assertInstance(message:String = "Instance must be a ${I::class.java.name} not a ${this::class.java.name}") : I
{
    Assert.assertTrue(message, this is I)
    return this as I
}