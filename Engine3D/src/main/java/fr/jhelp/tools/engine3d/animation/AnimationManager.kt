package fr.jhelp.tools.engine3d.animation

internal class AnimationManager
{
    private val animations = ArrayList<Animation>()

    fun play(animation: Animation)
    {
        synchronized(this.animations)
        {
            this.animations.add(animation)
            animation.start()
        }
    }

    fun stop(animation: Animation)
    {
        synchronized(this.animations)
        {
            animation.finished()
            this.animations.remove(animation)
        }
    }

    fun update()
    {
        synchronized(this.animations)
        {
            val iterator = this.animations.iterator()

            while (iterator.hasNext())
            {
                val animation = iterator.next()

                if (animation.animate().not())
                {
                    animation.finished()
                    iterator.remove()
                }
            }
        }
    }
}