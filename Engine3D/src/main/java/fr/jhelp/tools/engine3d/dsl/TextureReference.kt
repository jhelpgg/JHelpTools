package fr.jhelp.tools.engine3d.dsl

import fr.jhelp.tools.engine3d.resources.texture.TextureSource
import fr.jhelp.tools.engine3d.resources.texture.TextureSourceDefault

/**
 * Reference of texture
 */
class TextureReference internal constructor(internal var textureSource: TextureSource<*> = TextureSourceDefault)
