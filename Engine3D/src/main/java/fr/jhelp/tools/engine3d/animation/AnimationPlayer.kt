package fr.jhelp.tools.engine3d.animation

class AnimationPlayer internal constructor(private val animation:Animation, private val animationManager: AnimationManager)
{
    fun play()
    {
        this.animationManager.play(this.animation)
    }

    fun stop()
    {
        this.animationManager.stop(this.animation)
    }
}