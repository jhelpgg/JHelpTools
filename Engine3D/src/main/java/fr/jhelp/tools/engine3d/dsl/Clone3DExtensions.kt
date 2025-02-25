package fr.jhelp.tools.engine3d.dsl

import fr.jhelp.tools.engine3d.annotations.MaterialDSL
import fr.jhelp.tools.engine3d.annotations.NodeDSL
import fr.jhelp.tools.engine3d.scene.Clone3D

/**
 * Apply material reference to a clone
 */
@NodeDSL
fun Clone3D.material(materialReference: MaterialReference, material: @MaterialDSL MaterialCreator.() -> Unit)
{
    val materialCreator = MaterialCreator(materialReference.material)
    materialCreator.material()
    materialCreator.resolveTexture()
    this.material = materialReference.material
}
