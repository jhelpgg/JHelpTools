package fr.jhelp.tools.engine3d.dsl

import fr.jhelp.tools.engine3d.annotations.NodeDSL
import fr.jhelp.tools.engine3d.annotations.NodeTreeDSL
import fr.jhelp.tools.engine3d.annotations.PositionDSL
import fr.jhelp.tools.engine3d.scene.Node3D
import fr.jhelp.tools.engine3d.scene.Position3D

/**
 * Change node position
 */
@NodeDSL
fun Node3D.position(position: @PositionDSL Position3D.() -> Unit)
{
    position(this.position)
}

/***
 * Create nde hierarchy
 */
@NodeDSL
fun Node3D.children(nodeTree: @NodeTreeDSL NodeTreeCreator.() -> Unit)
{
    nodeTree(NodeTreeCreator(this))
}
