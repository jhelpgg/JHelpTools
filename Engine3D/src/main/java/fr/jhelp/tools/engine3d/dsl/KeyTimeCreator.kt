package fr.jhelp.tools.engine3d.dsl

import fr.jhelp.tools.engine3d.animation.interpolation.Interpolation
import fr.jhelp.tools.engine3d.animation.interpolation.LinearInterpolation
import fr.jhelp.tools.engine3d.annotations.KeyTimeDSL
import fr.jhelp.tools.engine3d.scene.Position3D
import kotlin.math.max

/**
 * Create an animation node position for specified time after animation started
 */
@KeyTimeDSL
class KeyTimeCreator
{
    /**
     * Key time in milliseconds
     */
    @KeyTimeDSL
    var timeMillisecond : Int = 0
        set(value){ field = max(0,value) }

    /**
     * Interpolation used to reach position
     */
    @KeyTimeDSL
    var interpolation : Interpolation = LinearInterpolation

    internal var position : Position3D = Position3D()

    /**
     * Set position
     */
    @KeyTimeDSL
    fun position(position: Position3D.() -> Unit)
    {
        this.position.position()
    }
}