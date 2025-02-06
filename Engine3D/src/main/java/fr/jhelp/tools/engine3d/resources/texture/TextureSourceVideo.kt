package fr.jhelp.tools.engine3d.resources.texture

import fr.jhelp.tools.engine3d.scene.TextureVideo

class TextureSourceVideo : TextureSource<TextureVideo>()
{
    private val textureVideo = TextureVideo()

    override fun createTexture(): TextureVideo = this.textureVideo
}