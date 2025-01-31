package fr.jhelp.tools.engine3d.scene

import fr.jhelp.tools.engine3d.annotations.OpenGLThread
import fr.jhelp.tools.engine3d.buffer.intBuffer
import java.nio.ByteBuffer
import javax.microedition.khronos.opengles.GL10

/**
 * Texture for [Material.texture] to decorate an [Object3D] or a [Clone3D]
 */
abstract class Texture
{
    private var videoMemoryId = -1
    private val intBuffer = intBuffer(1)
    abstract val width: Int
    abstract val height: Int

    protected abstract fun pixels(): ByteBuffer?
    protected open fun afterPixelsPushedInVideoMemory() = Unit
    protected open fun afterBindTexture() = Unit

    /**
     * Called when draw the texture for current object
     */
    @OpenGLThread
    internal fun bind(gl: GL10)
    {
        if (this.videoMemoryId < 0)
        {
            this.intBuffer.rewind()
            this.intBuffer.put(1)
            this.intBuffer.rewind()
            gl.glGenTextures(1, this.intBuffer)
            this.intBuffer.rewind()
            this.videoMemoryId = this.intBuffer.get()
        }

        val pixels = this.pixels()

        if (pixels != null)
        {
            pixels.position(0)

            // Push pixels in video memory
            gl.glBindTexture(GL10.GL_TEXTURE_2D, this.videoMemoryId)
            gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR)
            gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR)
            gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT)
            gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT)
            gl.glTexImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGBA, this.width, this.height, 0,
                            GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, pixels)
            this.afterPixelsPushedInVideoMemory()
        }

        gl.glBindTexture(GL10.GL_TEXTURE_2D, this.videoMemoryId)
        this.afterBindTexture()
    }
}