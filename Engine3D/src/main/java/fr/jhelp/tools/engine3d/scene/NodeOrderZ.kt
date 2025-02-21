package fr.jhelp.tools.engine3d.scene

import fr.jhelp.tools.utilities.math.sign

/**
 * Order nodes by their 'Z'
 */
object NodeOrderZ : Comparator<Node3D>
{
    /**
     * Compare two node to sort them by Z
     */
    override fun compare(node1: Node3D, node2: Node3D): Int =
        (node2.zOrder - node1.zOrder).sign()
}