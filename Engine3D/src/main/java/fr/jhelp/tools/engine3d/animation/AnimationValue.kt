package fr.jhelp.tools.engine3d.animation

import fr.jhelp.tools.engine3d.animation.keyFrame.AnimationKeyFrame

/**
 * Animation of a value
 */
class AnimationValue(private val initialValue:Float, fps: Int, animated: (Float)->Unit) : AnimationKeyFrame<(Float)->Unit, Float>(animated, fps)
{
    constructor(initialValue:Float, animated: (Float)->Unit) : this(initialValue, 25, animated)

    constructor(fps:Int, animated: (Float)->Unit) : this(0f, fps, animated)

    constructor(animated: (Float)->Unit) : this(0f, 25, animated)

    override fun finished()
    {
        super.finished()
    }

    override fun interpolateValue(animated: (Float) -> Unit, before: Float, after: Float, percent: Float)
    {
        animated(before + (after - before) * percent)
    }

    override fun obtainValue(animated: (Float) -> Unit): Float =
        this.initialValue

    override fun setValue(animated: (Float) -> Unit, value: Float)
    {
        animated(value)
    }
}