package fr.jhelp.tools.engine3d.dsl

import fr.jhelp.tools.engine3d.scene.Color3D
import fr.jhelp.tools.engine3d.scene.Material

/**
 * Create a reference on material
 */
class MaterialReference internal constructor(internal val material: Material = Material())
{
    /** Diffuse color */
    val diffuse: Color3D get() = this.material.diffuse

    /** Transparency */
    val alpha: Float get() = this.material.alpha
}
