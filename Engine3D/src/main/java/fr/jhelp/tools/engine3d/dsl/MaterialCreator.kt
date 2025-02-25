package fr.jhelp.tools.engine3d.dsl

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.annotation.DrawableRes
import fr.jhelp.tools.engine3d.animation.texture.AnimationTextureMixer
import fr.jhelp.tools.engine3d.annotations.AnimationTextureMixerDSL
import fr.jhelp.tools.engine3d.annotations.DrawDSL
import fr.jhelp.tools.engine3d.annotations.MaterialDSL
import fr.jhelp.tools.engine3d.resources.texture.TextureSource
import fr.jhelp.tools.engine3d.resources.texture.TextureSourceAnimationMixer
import fr.jhelp.tools.engine3d.resources.texture.TextureSourceAsset
import fr.jhelp.tools.engine3d.resources.texture.TextureSourceCreated
import fr.jhelp.tools.engine3d.resources.texture.TextureSourceDefault
import fr.jhelp.tools.engine3d.resources.texture.TextureSourceDrawable
import fr.jhelp.tools.engine3d.resources.texture.TextureSourceVideo
import fr.jhelp.tools.engine3d.scene.Color3D
import fr.jhelp.tools.engine3d.scene.Material
import fr.jhelp.tools.engine3d.scene.TextureImage
import fr.jhelp.tools.engine3d.scene.TextureVideo
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
        private set

    @MaterialDSL
    fun noTexture()
    {
        this.textureReference = null
    }

    @MaterialDSL
    fun textureVideo(): TextureVideo
    {
        val video = TextureSourceVideo()
        this.source(video)
        return video.texture
    }

    @MaterialDSL
    fun textureAsset(assetPath: String): TextureImage
    {
        val asset = TextureSourceAsset(assetPath)
        this.source(asset)
        return asset.texture
    }

    @MaterialDSL
    fun textureDefault(): TextureImage
    {
        val default = TextureSourceDefault
        this.source(default)
        return default.texture
    }

    @MaterialDSL
    fun textureDrawable(@DrawableRes drawableID: Int, sealed: Boolean = true): TextureImage
    {
        val drawable = TextureSourceDrawable(drawableID, sealed)
        this.source(drawable)
        return drawable.texture
    }

    @MaterialDSL
    fun textureCreate(width: Int, height: Int,
                      draw: @DrawDSL (Bitmap, Canvas, Paint) -> Unit): TextureImage
    {
        val created = TextureSourceCreated(width, height, draw)
        this.source(created)
        return created.texture
    }

    @MaterialDSL
    fun textureAnimationMixer(startTexture: TextureReference, endTexture: TextureReference,
                              animationTextureMixer: @AnimationTextureMixerDSL AnimationTextureMixerCreator.() -> Unit): AnimationTextureMixer
    {
        val animationTextureMixerCreator = AnimationTextureMixerCreator(startTexture, endTexture)
        animationTextureMixerCreator.animationTextureMixer()
        val animation = TextureSourceAnimationMixer(animationTextureMixerCreator())
        this.source(animation)
        return animation.animationTextureMixer
    }

    internal fun resolveTexture()
    {
        this.material.texture = this.textureReference?.textureSource?.texture
    }

    private fun source(textureSource: TextureSource<*>)
    {
        val textureReference = this.textureReference

        this.textureReference =
            when (textureReference)
            {
                null -> TextureReference(textureSource)
                else ->
                {
                    textureReference.textureSource = textureSource
                    textureReference
                }
            }
    }
}