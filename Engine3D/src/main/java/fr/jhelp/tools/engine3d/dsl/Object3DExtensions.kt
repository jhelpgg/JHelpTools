package fr.jhelp.tools.engine3d.dsl

import fr.jhelp.tools.engine3d.annotations.MaterialDSL
import fr.jhelp.tools.engine3d.annotations.NodeDSL
import fr.jhelp.tools.engine3d.scene.Object3D

/**
 * Apply material referred by a material reference to this object 3D
 */
@NodeDSL
fun Object3D.material(materialReference: MaterialReference, material: @MaterialDSL MaterialCreator.() -> Unit)
{
    val materialCreator = MaterialCreator(materialReference.material)
    materialCreator.material()
    materialCreator.resolveTexture()
    this.material = materialReference.material
}
