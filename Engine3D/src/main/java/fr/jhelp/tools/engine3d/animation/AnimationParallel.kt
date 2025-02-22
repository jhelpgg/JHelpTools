package fr.jhelp.tools.engine3d.animation

/**
 * Launch several animations in "same time"
 */
class AnimationParallel : Animation()
{
    private val animations = ArrayList<AnimationAlive>()
    private var length = 0

    /**
     * Add animation to do in "same time"
     */
    fun add(animation: Animation)
    {
        synchronized(this.animations)
        {
            this.animations.add(AnimationAlive(animation))
        }
    }

    override fun initialize()
    {
        synchronized(this.animations)
        {
            this.length = this.animations.size

            for (index in 0 until this.length)
            {
                this.animations[index].start()
            }
        }
    }

    override fun animate(frame: Float): Boolean
    {
        var animate = false

        for (index in 0 until this.length)
        {
            animate = this.animations[index].animate() || animate
        }

        return animate
    }
}