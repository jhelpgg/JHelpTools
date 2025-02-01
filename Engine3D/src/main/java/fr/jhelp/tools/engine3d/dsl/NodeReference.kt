package fr.jhelp.tools.engine3d.dsl

import fr.jhelp.tools.engine3d.scene.Node3D

/**
 * Reference to a node
 */
class NodeReference internal constructor(internal var node: Node3D = Node3D()) {
    override fun toString(): String =
        "NodeReference : ${this.node}"
}
