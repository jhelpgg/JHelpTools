package fr.jhelp.tools.engine3d.view.touch

import android.os.SystemClock
import fr.jhelp.tools.engine3d.view.CLICK_TIME
import fr.jhelp.tools.engine3d.view.View3D
import fr.jhelp.tools.utilities.math.bounds
import fr.jhelp.tools.utilities.math.distance

/**
 * Action to touch on [View3D] for classic manipulation of a node for show case
 */
object View3DTouchManipulation : View3DTouchAction()
{
    private var lastDownTime = 0L
    private lateinit var view3D: View3D

    /**
     * Called when reaction to touch is attach to the view 3D
     */
    override fun attachTo(view3D: View3D)
    {
        this.view3D = view3D
    }

    /**
     * Called when reaction to touch is detach from the view 3D
     */
    override fun detachFrom(view3D: View3D): Unit = Unit

    /**
     * Called when one finger touch the 3D view
     */
    override fun oneFingerDown(x: Float, y: Float)
    {
        this.lastDownTime = SystemClock.elapsedRealtime()
    }

    /**
     * Returned boolean indicates if have to simulate click if available
     */
    override fun oneFingerUp(x: Float, y: Float): Boolean
    {
        return SystemClock.elapsedRealtime() - this.lastDownTime <= CLICK_TIME
    }

    /**
     * Called when on finger slide on 3D view
     */
    override fun oneFingerMove(previousX: Float, previousY: Float, x: Float, y: Float)
    {
        this.view3D.manipulateNode.position.angleY =
            (this.view3D.manipulateNode.position.angleY + (x - previousX) * 0.25f)
                .bounds(this.view3D.minimumAngleY, this.view3D.maximumAngleY)
        this.view3D.manipulateNode.position.angleX =
            (this.view3D.manipulateNode.position.angleX + (y - previousY) * 0.25f)
                .bounds(this.view3D.minimumAngleX, this.view3D.maximumAngleX)
    }

    /**
     * Called when two finger are down in 3D view
     */
    override fun twoFingersDown(x1: Float, y1: Float,
                                x2: Float, y2: Float): Unit = Unit

    /**
     * Called when two finger removed from 3D view
     */
    override fun twoFingersUp(x1: Float, y1: Float,
                              x2: Float, y2: Float): Unit = Unit

    /**
     * Called when two fingers slides on 3D view
     */
    override fun twoFingersMove(previousX1: Float, previousY1: Float, x1: Float, y1: Float,
                                previousX2: Float, previousY2: Float, x2: Float, y2: Float)
    {
        val previousDistance = distance(previousX1, previousY1, previousX2, previousY2)
        val currentDistance = distance(x1, y1, x2, y2)
        this.view3D.manipulateNode.position.z =
            (this.view3D.manipulateNode.position.z + 0.01f * (currentDistance - previousDistance))
                .bounds(this.view3D.minimumZ, this.view3D.maximumZ)

    }
}