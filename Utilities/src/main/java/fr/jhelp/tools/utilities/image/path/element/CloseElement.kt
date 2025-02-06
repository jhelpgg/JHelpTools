package fr.jhelp.tools.utilities.image.path.element

import fr.jhelp.tools.utilities.image.path.Segment
import java.util.Objects

/**
 * Represent the close of a path element list
 */
internal class CloseElement(val ignoreNext: Boolean) : PathElement<CloseElement>()
{
    override fun appendSegments(segments: MutableList<Segment>, precision: Int) = Unit

    override fun equalsPathElement(pathElement: CloseElement): Boolean =
        this.ignoreNext == pathElement.ignoreNext

    override fun hash(): Int = Objects.hash(this.ignoreNext)
}
