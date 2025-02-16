package fr.jhelp.tools.engine3d.view

import fr.jhelp.tools.utilities.math.Point2D
import fr.jhelp.tools.utilities.math.Point3D

/**
 * View bounds in 3D and 2D
 * @property topLeftNear Position of top left near corner point of 3D box
 * @property bottomRightFar Position of bottom right far corner point of 3D box
 * @property topLeft Position of top left point of 2D view that show the 3D
 * @property bottomRight Position of bottom right point of 2D view that show the 3D
 */
data class View3DBounds(val topLeftNear: Point3D = Point3D(), val bottomRightFar: Point3D = Point3D(),
                        val topLeft: Point2D = Point2D(), val bottomRight: Point2D = Point2D())