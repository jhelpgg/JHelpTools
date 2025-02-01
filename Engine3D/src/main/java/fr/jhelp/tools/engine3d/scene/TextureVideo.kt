package fr.jhelp.tools.engine3d.scene

import android.graphics.Bitmap
import fr.jhelp.tools.engine3d.buffer.byteBuffer
import fr.jhelp.tools.utilities.source.Source
import fr.jhelp.tools.video.capture.VideoCaptureManager
import java.nio.ByteBuffer

class TextureVideo : Texture()
{
    override val width: Int = 512
    override val height: Int = 512
    private val videoCaptureManager = VideoCaptureManager.create(512,512)
    private var videoFrame: ByteBuffer = byteBuffer(512 * 512 * 4)

    init
    {
        this.videoCaptureManager.capture = this::capture
    }

    fun play(video:Source)
    {
        this.videoCaptureManager.play(video)
    }

    fun stop()
    {
        this.videoCaptureManager.stop()
    }

    fun pause()
    {
        this.videoCaptureManager.pause()
    }

    fun resume()
    {
        this.videoCaptureManager.resume()
    }

    override fun pixels(): ByteBuffer = this.videoFrame

    private fun capture(image:Bitmap)
    {
        image.copyPixelsToBuffer(this.videoFrame)
        this.videoFrame.position(0)
    }
}