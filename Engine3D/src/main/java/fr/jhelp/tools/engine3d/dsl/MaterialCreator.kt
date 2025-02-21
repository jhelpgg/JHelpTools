package fr.jhelp.tools.engine3d.dsl

import fr.jhelp.tools.engine3d.annotations.MaterialDSL
import fr.jhelp.tools.engine3d.scene.Color3D
import fr.jhelp.tools.engine3d.scene.Material
import fr.jhelp.tools.utilities.math.bounds

/**
 * Create a material
 */
@MaterialDSL
class MaterialCreator internal constructor(private val material: Material)
{
    /** Material opacity */
    @MaterialDSL
    var alpha: Float
        get() = this.material.alpha
        set(value)
        {
            this.material.alpha = value.bounds(0f, 1f)
        }

    /** Material diffuse color */
    @MaterialDSL
    var diffuse: Color3D
        get() = this.material.diffuse
        set(value)
        {
            this.material.diffuse = value
        }

    /** Reference on texture to apply */
    @MaterialDSL
    var textureReference: TextureReference? = null

    internal fun resolveTexture()
    {
        this.material.texture = this.textureReference?.textureSource?.texture
    }
}