package fr.jhelp.tools.engine3d.animation

internal class AnimationAlive(val animation: Animation)
{
    private var alive: Boolean = true

    fun start()
    {
        this.alive = true
        this.animation.start()
    }

    fun animate(): Boolean
    {
        this.alive = this.alive and this.animation.animate()
        return this.alive
    }
}
