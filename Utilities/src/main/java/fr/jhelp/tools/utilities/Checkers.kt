package fr.jhelp.tools.utilities

/**
 * Check if a condition on an argument is valid.
 * @throws IllegalArgumentException If the condition is not valid
 */
@Throws(IllegalArgumentException::class)
fun Boolean.checkArgument(message : String)
{
    if (this.not())
    {
        throw IllegalArgumentException(message)
    }
}

/**
 * Check if a condition on a state is valid.
 * @throws IllegalStateException If the condition is not valid
 */
@Throws(IllegalStateException::class)
fun Boolean.checkState(message : String)
{
    if (this.not())
    {
        throw IllegalStateException(message)
    }
}

/**
 * Check if a condition that represents the presence of something is valid.
 * @throws NoSuchElementException If the condition is not valid
 */
@Throws(NoSuchElementException::class)
fun Boolean.checkPresence(message : String)
{
    if (this.not())
    {
        throw NoSuchElementException(message)
    }
}

/**
 * Check if a value inside an interval
 * @throws IndexOutOfBoundsException If the value not inside the interval
 */
@Throws(IndexOutOfBoundsException::class)
infix fun Long.checkIn(range : LongRange)
{
    if (this !in range)
    {
        throw IndexOutOfBoundsException("value must be in [${range.first}, ${range.last}], not $this")
    }
}

/**
 * Check if a value inside an interval
 * @throws IndexOutOfBoundsException If the value not inside the interval
 */
@Throws(IndexOutOfBoundsException::class)
infix fun Int.checkIn(range : IntRange)
{
    if (this !in range)
    {
        throw IndexOutOfBoundsException("value must be in [${range.first}, ${range.last}], not $this")
    }
}

/**
 * Check if all array's element are unique, no duplicates
 * @param messageFooter Message to add on exception message in case of duplicates
 * @param equals Indicates what is same value means. By default, it uses `==`
 * @throws IllegalArgumentException If their at least one duplicate
 */
@Throws(IllegalArgumentException::class)
fun <T : Any> Array<T>.assertAllUnique(messageFooter : String,
                                       equals : (T, T) -> Boolean = { element1, element2 -> element1 == element2 })
{
    for (index in this.size - 1 downTo 1)
    {
        val elementSearched = this[index]
        val foundIndex = this.indexOfFirst { element -> equals(elementSearched, element) }

        (foundIndex == index).checkArgument("Element $elementSearched is duplicate at $foundIndex and $index. $messageFooter")
    }
}

/**
 * Check if a value is a specified type
 *
 * @param messageFooter Message to add in the exception message if throw
 * @return Cast instance in desired type
 * @throws IllegalArgumentException If the instance is not correct type
 */
@Throws(IllegalArgumentException::class)
inline fun <reified S : Any> Any?.checkInstance(messageFooter : String) : S =
    if (this is S) this else throw IllegalArgumentException("Argument is ${if (this == null) "null" else this::class.java.name} but we required ${S::class.java} : $messageFooter")

/**
 * Check if a value is a specified type
 *
 * @param messageFooter Message to add in the exception message if throw
 * @param defaultIfNull Value to return if the instance is null
 * @return Cast instance in desired type or default value
 * @throws IllegalArgumentException If the instance is not correct type
 */
@Throws(IllegalArgumentException::class)
inline fun <reified S : Any> Any?.checkInstance(messageFooter : String, defaultIfNull : S) : S =
    when (this)
    {
        null -> defaultIfNull
        is S -> this
        else -> throw IllegalArgumentException("Argument is ${this::class.java.name} but we required ${S::class.java} : $messageFooter")
    }