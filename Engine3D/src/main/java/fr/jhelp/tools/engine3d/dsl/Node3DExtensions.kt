package fr.jhelp.tools.engine3d.dsl

import fr.jhelp.tools.engine3d.scene.Node3D
import fr.jhelp.tools.engine3d.scene.Position3D

/**
 * Change node position
 */
fun Node3D.position(position: Position3D.() -> Unit)
{
    position(this.position)
}

/***
 * Create nde hierarchy
 */
fun Node3D.children(nodeTree: NodeTreeCreator.() -> Unit)
{
    nodeTree(NodeTreeCreator(this))
}
