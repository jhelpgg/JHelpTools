package fr.jhelp.tools.utilities.image.path.element

import fr.jhelp.tools.utilities.image.crawler.EllipticArcCrawler
import fr.jhelp.tools.utilities.image.path.Segment
import fr.jhelp.tools.utilities.math.AngleFloat
import fr.jhelp.tools.utilities.math.Point2D
import java.util.Objects

internal class EllipticArcElement(val startX: Float, val startY: Float,
                                  val radiusX: Float, val radiusY: Float,
                                  val rotationAxisX: AngleFloat,
                                  val largeArc: Boolean, val sweep: Boolean,
                                  val endX: Float, val endY: Float) :
    PathElement<EllipticArcElement>()
{
    override fun appendSegments(segments: MutableList<Segment>, precision: Int)
    {
        val ellipticArcToCrawler =
            EllipticArcCrawler(this.startX, this.startY,
                               this.radiusX, this.radiusY,
                               this.rotationAxisX,
                               this.largeArc, this.sweep,
                               this.endX, this.endY,
                               precision)
        var first = ellipticArcToCrawler[0]
        var second: Point2D

        for (index in 1..ellipticArcToCrawler.numberStep)
        {
            second = ellipticArcToCrawler[index]
            segments.add(Segment(first, 0f, second, 1f))
            first = second
        }
    }

    override fun equalsPathElement(pathElement: EllipticArcElement): Boolean =
        this.startX == pathElement.startX && this.startY == pathElement.startY &&
                this.radiusX == pathElement.radiusX && this.radiusY == pathElement.radiusY &&
                this.rotationAxisX == pathElement.rotationAxisX &&
                this.largeArc == pathElement.largeArc && this.sweep == pathElement.sweep &&
                this.endX == pathElement.endX && this.endY == pathElement.endY

    override fun hash(): Int =
        Objects.hash(this.startX, this.startY,
                     this.radiusX, this.radiusY,
                     this.rotationAxisX,
                     this.largeArc, this.sweep,
                     this.endX, this.endY)
}
