package fr.jhelp.tools.engine3d.animation.node

import fr.jhelp.tools.engine3d.animation.Animation
import fr.jhelp.tools.engine3d.animation.change.ValueChange
import fr.jhelp.tools.engine3d.animation.change.ValueFollowFunctionOfT
import fr.jhelp.tools.engine3d.animation.change.ValueNotChange
import fr.jhelp.tools.engine3d.scene.Node3D
import fr.jhelp.tools.mathformal.ConstantFormal
import fr.jhelp.tools.mathformal.dsl.T
import fr.jhelp.tools.mathformal.dsl.constant
import fr.jhelp.tools.mathformal.replaceAll
import fr.jhelp.tools.mathformal.simplifier.simplified
import kotlin.math.max

/**
 * Node animation to follow an equation on each position component of a node
 * @property node Node that follow the changes
 * @property valueX How X value change
 * @property valueY How Y value change
 * @property valueZ How Z value change
 * @property valueAngleX How angleX value change
 * @property valueAngleY How angleY value change
 * @property valueAngleZ How angleZ value change
 * @property valueScaleX How scaleX value change
 * @property valueScaleY How scaleY value change
 * @property valueScaleZ How scaleZ value change
 * @param durationMilliseconds Duration of the animation in milliseconds
 * @param fps Animation FPS
 */
class AnimationNodeFollowEquation(private val node: Node3D,
                                  private val valueX: ValueChange = ValueNotChange,
                                  private val valueY: ValueChange = ValueNotChange,
                                  private val valueZ: ValueChange = ValueNotChange,
                                  private val valueAngleX: ValueChange = ValueNotChange,
                                  private val valueAngleY: ValueChange = ValueNotChange,
                                  private val valueAngleZ: ValueChange = ValueNotChange,
                                  private val valueScaleX: ValueChange = ValueNotChange,
                                  private val valueScaleY: ValueChange = ValueNotChange,
                                  private val valueScaleZ: ValueChange = ValueNotChange,
                                  durationMilliseconds: Int,
                                  fps: Int = 25) : Animation(fps)
{
    private val numberFrames = max(1, this.millisecondsToFrame(durationMilliseconds))

    override fun animate(frame: Float): Boolean
    {
        if (frame >= this.numberFrames)
        {
            this.positionNode(1f)
            return false
        }

        this.positionNode(frame / this.numberFrames)
        return true
    }

    override fun initialize()
    {
        this.positionNode(0f)
    }

    override fun finished()
    {
        this.positionNode(1f)
    }

    private fun positionNode(percent: Float)
    {
        val position = this.node.position
        position.x = this.function(this.valueX, percent, position.x)
        position.y = this.function(this.valueY, percent, position.y)
        position.z = this.function(this.valueZ, percent, position.z)
        position.angleX = this.function(this.valueAngleX, percent, position.angleX)
        position.angleY = this.function(this.valueAngleY, percent, position.angleY)
        position.angleZ = this.function(this.valueAngleZ, percent, position.angleZ)
        position.scaleX = this.function(this.valueScaleX, percent, position.scaleX)
        position.scaleY = this.function(this.valueScaleY, percent, position.scaleY)
        position.scaleZ = this.function(this.valueScaleZ, percent, position.scaleZ)
    }

    private fun function(value: ValueChange, percent: Float, defaultValue: Float): Float =
        if (value is ValueFollowFunctionOfT)
        {
            (value.function.replaceAll(T, value(percent).constant).simplified as ConstantFormal).value.toFloat()
        }
        else
        {
            defaultValue
        }
}