package fr.jhelp.tools.utilities.image.path.element

import fr.jhelp.tools.utilities.image.path.Segment
import java.util.Objects

/**
 * Represents a line element
 */
internal class LineElement(val startX: Float, val startY: Float,
                           val endX: Float, val endY: Float) : PathElement<LineElement>()
{
    override fun appendSegments(segments: MutableList<Segment>, precision: Int)
    {
        segments.add(Segment(this.startX, this.startY, 0f,
                             this.endX, this.endY, 1f))
    }

    override fun equalsPathElement(pathElement: LineElement): Boolean =
        this.startX == pathElement.startX && this.startY == pathElement.startY &&
                this.endX == pathElement.endX && this.endY == pathElement.endY

    override fun hash(): Int = Objects.hash(this.startX, this.startY, this.endX, this.endY)
}
