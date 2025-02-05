package fr.jhelp.tools.utilities.image.path.element

import fr.jhelp.tools.utilities.image.path.Segment
import fr.jhelp.tools.utilities.math.quadratic
import java.util.Objects

/**
 * Represents a quadratic element
 */
internal class QuadraticElement(val startX: Float, val startY: Float,
                                val controlX: Float, val controlY: Float,
                                val endX: Float, val endY: Float) : PathElement<QuadraticElement>()
{
    override fun appendSegments(segments: MutableList<Segment>, precision: Int)
    {
        val xs = quadratic(this.startX, this.controlX, this.endX, precision)
        val ys = quadratic(this.startY, this.controlY, this.endY, precision)

        for (index in 1 until precision)
        {
            segments.add(
                Segment(xs[index - 1], ys[index - 1], 0f,
                        xs[index], ys[index], 1f))
        }
    }

    override fun equalsPathElement(pathElement: QuadraticElement): Boolean =
        this.startX == pathElement.startX && this.startY == pathElement.startY &&
                this.controlX == pathElement.controlX && this.controlY == pathElement.controlY &&
                this.endX == pathElement.endX && this.endY == pathElement.endY

    override fun hash(): Int =
        Objects.hash(this.startX, this.startY, this.controlX, this.controlY, this.endX, this.endY)
}
