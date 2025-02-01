package fr.jhelp.tools.engine3d.dsl

import fr.jhelp.tools.engine3d.scene.Clone3D
import fr.jhelp.tools.engine3d.scene.GREEN
import fr.jhelp.tools.engine3d.scene.Node3D
import fr.jhelp.tools.engine3d.scene.Object3D
import fr.jhelp.tools.engine3d.scene.geometry.Box
import fr.jhelp.tools.engine3d.scene.geometry.Plane

/**
 * Create a node tree
 */
class NodeTreeCreator internal constructor(private val root: Node3D)
{
    /**
     * Add node to the node tree
     */
    fun node(reference: NodeReference = junkReference, node: Node3D.() -> Unit)
    {
        val node3D = Node3D()
        reference.node = node3D
        node(node3D)
        this.root.add(node3D)
    }

    /**
     * Add an object 3D to the tree
     */
    fun object3D(reference: NodeReference = junkReference,
                 object3D: Object3D.() -> Unit)
    {
        val object3DReal = Object3D()
        reference.node = object3DReal
        object3D(object3DReal)
        this.root.add(object3DReal)
    }

    /**
     * Add a clone to the node tree
     */
    fun clone(reference: NodeReference = junkReference,
              referenceObjectOrClone: NodeReference,
              clone3D: Clone3D.() -> Unit)
    {
        val node = referenceObjectOrClone.node
        val clone =
            when (node)
            {
                is Object3D -> Clone3D(node)
                is Clone3D  -> Clone3D(node.object3D)
                else        -> throw IllegalArgumentException("referenceObjectOrClone not reference to clone or objet 3D, but to ${node::class.java.name}")
            }

        reference.node = clone
        clone3D(clone)
        this.root.add(clone)
    }

    /**
     * Add plane to the node tree
     */
    fun plane(reference: NodeReference = junkReference,
              startU: Float = 0f, endU: Float = 1f,
              startV: Float = 0f, endV: Float = 1f,
              seal: Boolean = true,
              plane: Plane.() -> Unit)
    {
        val planeReal = Plane(startU, endU, startV, endV, seal)
        reference.node = planeReal
        plane(planeReal)
        this.root.add(planeReal)
    }

    /**
     * Add a box to the node tree
     */
    fun box(reference: NodeReference = junkReference,
            boxUV: BoxUVCreator.() -> Unit = {},
            seal: Boolean = true,
            box: Box.() -> Unit)
    {
        val boxUVCreator = BoxUVCreator()
        boxUV(boxUVCreator)
        val boxReal = Box(boxUVCreator.boxUV, seal)
        reference.node = boxReal
        box(boxReal)
        this.root.add(boxReal)
    }
}