package fr.jhelp.tools.engine3d.dsl

import fr.jhelp.tools.engine3d.scene.Object3D

/**
 * Apply material referred by a material reference to this object 3D
 */
fun Object3D.material(materialReference: MaterialReference)
{
    this.material = materialReference.material
}
