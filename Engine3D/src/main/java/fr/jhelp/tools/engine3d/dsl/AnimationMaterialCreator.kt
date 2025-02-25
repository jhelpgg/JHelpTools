package fr.jhelp.tools.engine3d.dsl

import fr.jhelp.tools.engine3d.animation.material.AnimationMaterial
import fr.jhelp.tools.engine3d.annotations.AnimationMaterialAlphaDSL
import fr.jhelp.tools.engine3d.annotations.AnimationMaterialDSL
import fr.jhelp.tools.engine3d.annotations.AnimationMaterialDiffuseDSL

/**
 * Create an animation on material
 */
@AnimationMaterialDSL
class AnimationMaterialCreator(private val materialReference: MaterialReference)
{
    /**
     * Material animation
     */
    @AnimationMaterialDSL
    val animationMaterial = AnimationMaterial(this.materialReference.material)

    /**
     * Set diffuse color at given time
     */
    @AnimationMaterialDSL
    fun diffuse(diffuse: @AnimationMaterialDiffuseDSL AnimationMaterialDiffuseCreator.() -> Unit)
    {
        val creator = AnimationMaterialDiffuseCreator(this.materialReference.material.diffuse)
        creator.diffuse()
        animationMaterial.diffuseAtTime(creator.color3D, creator.timeMilliseconds, creator.interpolation)
    }

    /**
     * Set alpha at given time
     */
    @AnimationMaterialDSL
    fun alpha(alpha: @AnimationMaterialAlphaDSL AnimationMaterialAlphaCreator.() -> Unit)
    {
        val creator = AnimationMaterialAlphaCreator(this.materialReference.material.alpha)
        creator.alpha()
        animationMaterial.alphaAtTime(creator.alpha, creator.timeMilliseconds, creator.interpolation)
    }
}