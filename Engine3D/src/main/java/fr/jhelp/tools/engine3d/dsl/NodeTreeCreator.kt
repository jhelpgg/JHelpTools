package fr.jhelp.tools.engine3d.dsl

import fr.jhelp.tools.engine3d.scene.Clone3D
import fr.jhelp.tools.engine3d.scene.GREEN
import fr.jhelp.tools.engine3d.scene.Node3D
import fr.jhelp.tools.engine3d.scene.Object3D
import fr.jhelp.tools.engine3d.scene.geometry.Box
import fr.jhelp.tools.engine3d.scene.geometry.ObjectPath
import fr.jhelp.tools.engine3d.scene.geometry.Plane
import fr.jhelp.tools.engine3d.scene.geometry.Revolution
import fr.jhelp.tools.engine3d.scene.geometry.Sphere
import fr.jhelp.tools.utilities.image.path.Path

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
    /**
     * Add a revolution
     */
    fun revolution(reference: NodeReference = junkReference,
                   angle: Float = 360f,
                   multiplierU: Float = 1f, startV: Float = 0f, endV: Float = 1f,
                   pathPrecision: Int = 5, rotationPrecision: Int = 12,
                   path: Path.() -> Unit,
                   seal: Boolean = true,
                   revolution: Revolution.() -> Unit)
    {
        val pathReal = Path()
        path(pathReal)
        val revolutionReal = Revolution(pathReal,
                                        angle,
                                        multiplierU, startV, endV,
                                        pathPrecision, rotationPrecision,
                                        seal)
        reference.node = revolutionReal
        revolution(revolutionReal)
        this.root.add(revolutionReal)
    }

    /**
     * Add a sphere to the node tree
     */
    fun sphere(reference: NodeReference = junkReference,
               multiplierU: Float = 1f, multiplierV: Float = 1f, slice: Int = 16, slack: Int = 16,
               seal: Boolean = true,
               sphere: Sphere.() -> Unit)
    {
        val sphereReal = Sphere(multiplierU, multiplierV, slice, slack, seal)
        reference.node = sphereReal
        sphere(sphereReal)
        this.root.add(sphereReal)
    }

    /** Create object that represents a path along an other one */
    fun objectPath(reference: NodeReference = junkReference,
                   mainPath: Path.() -> Unit, mainPathPrecision: Int = 5,
                   followPath: Path.() -> Unit, followPathPrecision: Int = 5,
                   startU: Float = 0f, endU: Float = 1f,
                   startV: Float = 0f, endV: Float = 1f,
                   seal: Boolean = true,
                   objectPath: ObjectPath.() -> Unit)
    {
        val mainPathReal = Path()
        mainPath(mainPathReal)
        val followPathReal = Path()
        followPath(followPathReal)
        val objectPathReal = ObjectPath(mainPathReal, mainPathPrecision,
                                        followPathReal, followPathPrecision,
                                        startU, endU,
                                        startV, endV,
                                        seal)
        reference.node = objectPathReal
        objectPath(objectPathReal)
        this.root.add(objectPathReal)
    }
}