package fr.jhelp.tools.engine3d.dsl

import fr.jhelp.tools.engine3d.animation.interpolation.Interpolation
import fr.jhelp.tools.engine3d.animation.interpolation.LinearInterpolation
import fr.jhelp.tools.engine3d.annotations.KeyFrameDSL
import fr.jhelp.tools.engine3d.annotations.PositionDSL
import fr.jhelp.tools.engine3d.scene.Node3D
import fr.jhelp.tools.engine3d.scene.Position3D
import kotlin.math.max

/**
 * Create an animation node position key frame
 */
@KeyFrameDSL
class KeyFrameCreator
{
    /**
     * Frame number
     */
    @KeyFrameDSL
    var frame : Int = 0
        set(value){ field = max(0,value) }

    /**
     * Interpolation used to reach position
     */
    @KeyFrameDSL
    var interpolation : Interpolation = LinearInterpolation

    internal var position : Position3D = Position3D()

    /**
     * Set position
     */
    @KeyFrameDSL
    fun position(position: @PositionDSL Position3D.() -> Unit)
    {
        this.position.position()
    }
}