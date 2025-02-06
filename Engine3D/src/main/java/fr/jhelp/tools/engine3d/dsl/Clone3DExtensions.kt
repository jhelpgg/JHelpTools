package fr.jhelp.tools.engine3d.dsl

import fr.jhelp.tools.engine3d.scene.Clone3D

/**
 * Apply material reference to a clone
 */
fun Clone3D.material(materialReference: MaterialReference)
{
    this.material = materialReference.material
}
