package fr.jhelp.tools.engine3d.scene.morphing

import fr.jhelp.tools.engine3d.math.Triangle3D
import fr.jhelp.tools.utilities.math.compare

/**
 * Permits to sort triangles for morphing preparation
 */
object MorphingTriangleComparator : Comparator<Triangle3D>
{
    /**
     * Compare two given triangles to sort them
     */
    override fun compare(first: Triangle3D, second: Triangle3D): Int
    {
        val firstBarycenter = first.barycenter
        val secondBaryCenter = second.barycenter
        var comparison = firstBarycenter.z.compare(secondBaryCenter.z)

        if (comparison != 0)
        {
            return comparison
        }

        comparison = firstBarycenter.y.compare(secondBaryCenter.y)


        if (comparison != 0)
        {
            return comparison
        }

        return firstBarycenter.x.compare(secondBaryCenter.x)
    }
}
