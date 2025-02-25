package fr.jhelp.tools.engine3d.scene

import fr.jhelp.tools.engine3d.animation.Animation
import fr.jhelp.tools.engine3d.animation.AnimationManager
import fr.jhelp.tools.engine3d.animation.AnimationPlayer
import fr.jhelp.tools.engine3d.annotations.OpenGLThread
import fr.jhelp.tools.engine3d.math.antiProjection
import fr.jhelp.tools.engine3d.view.View3DBounds
import fr.jhelp.tools.utilities.collections.SortedArray
import fr.jhelp.tools.utilities.math.Point3D
import kotlinx.coroutines.flow.StateFlow
import java.util.Stack
import javax.microedition.khronos.opengles.GL10

class Scene3D internal constructor(viewBoundsState: StateFlow<View3DBounds>)
{
    /** manage scene animations */
    private val animationManager = AnimationManager()

    var backgroundColor: Color3D = WHITE
    var root: Node3D = Node3D()

    fun play(animation: Animation)
    {
        this.animationManager.play(animation)
    }

    fun stop(animation: Animation)
    {
        this.animationManager.stop(animation)
    }

    fun animationPlayer(animation: Animation): AnimationPlayer =
        AnimationPlayer(animation, this.animationManager)

    @OpenGLThread
    fun render(gl: GL10)
    {
        this.background(gl)
        this.nodes(gl)
        this.animationManager.update()
    }

    private fun background(gl: GL10)
    {
        gl.glClearColor(this.backgroundColor.red, this.backgroundColor.green, this.backgroundColor.blue, 1f)
    }

    private fun nodes(gl: GL10)
    {
        val nodes = SortedArray<Node3D>(NodeOrderZ)
        val stack = Stack<Node3D>()
        stack.push(this.root)
        var current: Node3D

        while (stack.isNotEmpty())
        {
            current = stack.pop()

            if (current.visible)
            {
                if (current.hasSomethingToDraw())
                {
                    current.zOrder = antiProjection(current, current.center()).z
                    nodes.add(current)
                }

                for (child in current)
                {
                    stack.push(child)
                }
            }
        }

        for (node in nodes)
        {
            var explore: Node3D? = node

            while (explore != null)
            {
                stack.push(explore)
                explore = explore.parent
            }

            var position = Point3D()

            gl.glPushMatrix()

            while (stack.isNotEmpty())
            {
                val nodeForPosition = stack.pop()
                position = nodeForPosition.position.projection(position)
                nodeForPosition.position.locate(gl)
            }

            node.render(gl)
            gl.glPopMatrix()
        }
    }
}