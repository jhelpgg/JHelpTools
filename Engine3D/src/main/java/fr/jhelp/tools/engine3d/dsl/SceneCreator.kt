package fr.jhelp.tools.engine3d.dsl

import fr.jhelp.tools.engine3d.scene.Color3D
import fr.jhelp.tools.engine3d.scene.Position3D
import fr.jhelp.tools.engine3d.scene.Scene3D
import fr.jhelp.tools.engine3d.view.View3D
import fr.jhelp.tools.engine3d.view.touch.View3DTouchAction

/**
 * Create a 3D scene
 *
 * @property view3D View that show the 3D
 */
class SceneCreator internal constructor(val view3D: View3D)
{
    /** The 3D scene */
    val scene3D: Scene3D = this.view3D.scene3D

    /** Background color */
    var backgroundColor: Color3D
        get() = this.scene3D.backgroundColor
        set(value)
        {
            this.scene3D.backgroundColor = value
        }

    /**
     * Change/define scene position
     */
    fun scenePosition(position: Position3D.() -> Unit)
    {
        this.scene3D.root.position(position)
    }

    /**
     * Create scene node hierarchy
     */
    fun root(nodeTree: NodeTreeCreator.() -> Unit)
    {
        this.scene3D.root.children(nodeTree)
    }
}