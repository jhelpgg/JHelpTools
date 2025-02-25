package fr.jhelp.tools.engine3d.dsl

import fr.jhelp.tools.engine3d.animation.AnimationPlayer
import fr.jhelp.tools.engine3d.annotations.AnimationCreatorDSL
import fr.jhelp.tools.engine3d.annotations.AnimationDSL
import fr.jhelp.tools.engine3d.annotations.NodeTreeDSL
import fr.jhelp.tools.engine3d.annotations.PositionDSL
import fr.jhelp.tools.engine3d.annotations.SceneDSL
import fr.jhelp.tools.engine3d.scene.Color3D
import fr.jhelp.tools.engine3d.scene.Position3D
import fr.jhelp.tools.engine3d.scene.Scene3D
import fr.jhelp.tools.engine3d.view.View3D

/**
 * Create a 3D scene
 *
 * @property view3D View that show the 3D
 */
@SceneDSL
class SceneCreator internal constructor(val view3D: View3D)
{
    /** The 3D scene */
    @SceneDSL
    val scene3D: Scene3D = this.view3D.scene3D

    /** Background color */
    @SceneDSL
    var backgroundColor: Color3D
        get() = this.scene3D.backgroundColor
        set(value)
        {
            this.scene3D.backgroundColor = value
        }

    /**
     * Change/define scene position
     */
    @SceneDSL
    fun scenePosition(position: @PositionDSL Position3D.() -> Unit)
    {
        this.scene3D.root.position(position)
    }

    /**
     * Create scene node hierarchy
     */
    @SceneDSL
    fun root(nodeTree: @NodeTreeDSL NodeTreeCreator.() -> Unit)
    {
        this.scene3D.root.children(nodeTree)
    }

    /**
     * Create an animation player
     *
     * Call this method after have placed node in the scene (Node, texture and material references must be resolved)
     *
     * @param animationCreator Animation creator
     * @return Animation player
     */
    @SceneDSL
    fun animationPlayer(animationCreator: @AnimationCreatorDSL AnimationCreator.() -> Unit): AnimationPlayer
    {
        val creator = AnimationCreator()
        creator.animationCreator()
        return this.scene3D.animationPlayer(creator.animation)
    }
}