package fr.jhelp.tools.utilities.image.path.element

import fr.jhelp.tools.utilities.image.path.Segment

/**
 * Represents a path element
 */
internal sealed class PathElement<PE : PathElement<PE>>
{
    /**
     * Compute the segments associated to the path element and add them to the given list
     */
    abstract fun appendSegments(segments: MutableList<Segment>, precision: Int)
    abstract fun equalsPathElement(pathElement: PE): Boolean
    abstract fun hash(): Int

    final override fun equals(other: Any?): Boolean
    {
        if (this === other)
        {
            return true
        }

        if (other == null || this.javaClass != other.javaClass)
        {
            return false
        }

        return this.equalsPathElement(other as PE)
    }

    final override fun hashCode(): Int
    {
        return this.hash() * 31 + this.javaClass.name.hashCode()
    }
}
