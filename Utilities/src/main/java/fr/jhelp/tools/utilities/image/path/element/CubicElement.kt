package fr.jhelp.tools.utilities.image.path.element

import fr.jhelp.tools.utilities.image.path.Segment
import fr.jhelp.tools.utilities.math.cubic
import java.util.Objects

internal class CubicElement(val startX: Float, val startY: Float,
                            val control1X: Float, val control1Y: Float,
                            val control2X: Float, val control2Y: Float,
                            val endX: Float, val endY: Float) : PathElement<CubicElement>()
{
    override fun appendSegments(segments: MutableList<Segment>, precision: Int)
    {
        val xs = cubic(this.startX, this.control1X, this.control2X, this.endX, precision)
        val ys = cubic(this.startY, this.control1Y, this.control2Y, this.endY, precision)

        for (index in 1 until precision)
        {
            segments.add(
                Segment(xs[index - 1], ys[index - 1], 0f,
                        xs[index], ys[index], 1f))
        }
    }

    override fun equalsPathElement(pathElement: CubicElement): Boolean =
        this.startX == pathElement.startX && this.startY == pathElement.startY &&
                this.control1X == pathElement.control1X && this.control1Y == pathElement.control1Y &&
                this.control2X == pathElement.control2X && this.control2Y == pathElement.control2Y &&
                this.endX == pathElement.endX && this.endY == pathElement.endY

    override fun hash(): Int =
        Objects.hash(this.startX, this.startY,
                     this.control1X, this.control1Y,
                     this.control2X, this.control2Y,
                     this.endX, this.endY)
}
