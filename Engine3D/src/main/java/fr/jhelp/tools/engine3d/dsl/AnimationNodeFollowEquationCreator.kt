package fr.jhelp.tools.engine3d.dsl

import fr.jhelp.tools.engine3d.animation.change.ValueChange
import fr.jhelp.tools.engine3d.animation.change.ValueFollowFunctionOfT
import fr.jhelp.tools.engine3d.animation.change.ValueNotChange
import fr.jhelp.tools.engine3d.animation.node.AnimationNodeFollowEquation
import fr.jhelp.tools.engine3d.annotations.AnimationNodeFollowEquationDSL
import kotlin.math.max

/**
 * Create an animation node follow equation
 * @property node Node to animate
 */
@AnimationNodeFollowEquationDSL
class AnimationNodeFollowEquationCreator(private val node: NodeReference)
{
    private var valueX: ValueChange = ValueNotChange
    private var valueY: ValueChange = ValueNotChange
    private var valueZ: ValueChange = ValueNotChange
    private var valueAngleX: ValueChange = ValueNotChange
    private var valueAngleY: ValueChange = ValueNotChange
    private var valueAngleZ: ValueChange = ValueNotChange
    private var valueScaleX: ValueChange = ValueNotChange
    private var valueScaleY: ValueChange = ValueNotChange
    private var valueScaleZ: ValueChange = ValueNotChange

    /**
     * Duration of animation in milliseconds
     */
    @AnimationNodeFollowEquationDSL
    var durationMilliseconds: Int = 1
        set(value)
        {
            field = max(1, value)
        }

    /**
     * Set equation of T for X coordinate
     */
    @AnimationNodeFollowEquationDSL
    fun valueX(value: ValueFollowFunctionOfTCreator.() -> Unit)
    {
        val creator = ValueFollowFunctionOfTCreator()
        creator.value()
        this.valueX = ValueFollowFunctionOfT(creator.functionOfT, creator.tStart, creator.tEnd)
    }

    /**
     * Set equation of T for Y coordinate
     */
    @AnimationNodeFollowEquationDSL
    fun valueY(value: ValueFollowFunctionOfTCreator.() -> Unit)
    {
        val creator = ValueFollowFunctionOfTCreator()
        creator.value()
        this.valueY = ValueFollowFunctionOfT(creator.functionOfT, creator.tStart, creator.tEnd)
    }

    /**
     * Set equation of T for Z coordinate
     */
    @AnimationNodeFollowEquationDSL
    fun valueZ(value: ValueFollowFunctionOfTCreator.() -> Unit)
    {
        val creator = ValueFollowFunctionOfTCreator()
        creator.value()
        this.valueZ = ValueFollowFunctionOfT(creator.functionOfT, creator.tStart, creator.tEnd)
    }

    /**
     * Set equation of T for angle around X axis
     */
    @AnimationNodeFollowEquationDSL
    fun valueAngleX(value: ValueFollowFunctionOfTCreator.() -> Unit)
    {
        val creator = ValueFollowFunctionOfTCreator()
        creator.value()
        this.valueAngleX = ValueFollowFunctionOfT(creator.functionOfT, creator.tStart, creator.tEnd)
    }

    /**
     * Set equation of T for angle around Y axis
     */
    @AnimationNodeFollowEquationDSL
    fun valueAngleY(value: ValueFollowFunctionOfTCreator.() -> Unit)
    {
        val creator = ValueFollowFunctionOfTCreator()
        creator.value()
        this.valueAngleY = ValueFollowFunctionOfT(creator.functionOfT, creator.tStart, creator.tEnd)
    }

    /**
     * Set equation of T for angle around Z axis
     */
    @AnimationNodeFollowEquationDSL
    fun valueAngleZ(value: ValueFollowFunctionOfTCreator.() -> Unit)
    {
        val creator = ValueFollowFunctionOfTCreator()
        creator.value()
        this.valueAngleZ = ValueFollowFunctionOfT(creator.functionOfT, creator.tStart, creator.tEnd)
    }

    /**
     * Set equation of T for scale along X axis
     */
    @AnimationNodeFollowEquationDSL
    fun valueScaleX(value: ValueFollowFunctionOfTCreator.() -> Unit)
    {
        val creator = ValueFollowFunctionOfTCreator()
        creator.value()
        this.valueScaleX = ValueFollowFunctionOfT(creator.functionOfT, creator.tStart, creator.tEnd)
    }

    /**
     * Set equation of T for scale along Y axis
     */
    @AnimationNodeFollowEquationDSL
    fun valueScaleY(value: ValueFollowFunctionOfTCreator.() -> Unit)
    {
        val creator = ValueFollowFunctionOfTCreator()
        creator.value()
        this.valueScaleY = ValueFollowFunctionOfT(creator.functionOfT, creator.tStart, creator.tEnd)
    }

    /**
     * Set equation of T for scale along Z axis
     */
    @AnimationNodeFollowEquationDSL
    fun valueScaleZ(value: ValueFollowFunctionOfTCreator.() -> Unit)
    {
        val creator = ValueFollowFunctionOfTCreator()
        creator.value()
        this.valueScaleZ = ValueFollowFunctionOfT(creator.functionOfT, creator.tStart, creator.tEnd)
    }

    internal operator fun invoke(): AnimationNodeFollowEquation =
        AnimationNodeFollowEquation(this.node.node,
                                    this.valueX, this.valueY, this.valueZ,
                                    this.valueAngleX, this.valueAngleY, this.valueAngleZ,
                                    this.valueScaleX, this.valueScaleY, this.valueScaleZ,
                                    this.durationMilliseconds)
}
