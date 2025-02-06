package fr.jhelp.tools.utilities.image.path.element

import fr.jhelp.tools.utilities.image.path.Segment
import java.util.Objects

/**
 * Represents a move to element
 */
internal class MoveToElement(val startX: Float, val startY: Float) : PathElement<MoveToElement>()
{
    override fun appendSegments(segments: MutableList<Segment>, precision: Int) = Unit

    override fun equalsPathElement(pathElement: MoveToElement): Boolean =
        this.startX == pathElement.startX && this.startY == pathElement.startY

    override fun hash(): Int = Objects.hash(this.startX, this.startY)
}
