package fr.jhelp.tools.engine3d.dsl

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.annotation.DrawableRes
import fr.jhelp.tools.engine3d.annotations.AnimationTextureMixerDSL
import fr.jhelp.tools.engine3d.annotations.DrawDSL
import fr.jhelp.tools.engine3d.annotations.TextureDSL
import fr.jhelp.tools.engine3d.resources.texture.TextureSource
import fr.jhelp.tools.engine3d.resources.texture.TextureSourceAnimationMixer
import fr.jhelp.tools.engine3d.resources.texture.TextureSourceAsset
import fr.jhelp.tools.engine3d.resources.texture.TextureSourceCreated
import fr.jhelp.tools.engine3d.resources.texture.TextureSourceDefault
import fr.jhelp.tools.engine3d.resources.texture.TextureSourceDrawable
import fr.jhelp.tools.engine3d.resources.texture.TextureSourceVideo
import fr.jhelp.tools.engine3d.scene.Texture
import fr.jhelp.tools.engine3d.scene.TextureImage
import fr.jhelp.tools.engine3d.scene.TextureVideo

@TextureDSL
fun <T : Texture> TextureReference.source(textureSource: TextureSource<T>): T
{
    this.textureSource = textureSource
    return textureSource.texture
}

@TextureDSL
fun TextureReference.video(): TextureVideo =
    this.source(TextureSourceVideo())

@TextureDSL
fun TextureReference.asset(assetPath: String): TextureImage =
    this.source(TextureSourceAsset(assetPath))

@TextureDSL
fun TextureReference.drawable(@DrawableRes drawableID: Int, sealed: Boolean = true): TextureImage =
    this.source(TextureSourceDrawable(drawableID, sealed))

@TextureDSL
fun TextureReference.default(): TextureImage =
    this.source(TextureSourceDefault)

@TextureDSL
fun TextureReference.create(width: Int, height: Int,
                            draw: @DrawDSL (Bitmap, Canvas, Paint) -> Unit): TextureImage =
    this.source(TextureSourceCreated(width, height, draw))

@TextureDSL
fun TextureReference.animationMixer(startTexture: TextureReference, endTexture: TextureReference,
                                    animationTextureMixer: @AnimationTextureMixerDSL AnimationTextureMixerCreator.() -> Unit): TextureSourceAnimationMixer
{
    val animationTextureMixerCreator = AnimationTextureMixerCreator(startTexture, endTexture)
    animationTextureMixerCreator.animationTextureMixer()
    val animation = TextureSourceAnimationMixer(animationTextureMixerCreator())
    this.source(animation)
    return animation
}