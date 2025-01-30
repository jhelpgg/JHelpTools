package fr.jhelp.tools.video.capture

import android.graphics.Bitmap
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.image.ImageOutput
import fr.jhelp.tools.video.capture.configuration.VideoCaptureConfiguration

@UnstableApi
internal class VideoCaptureOutput(private val videoCaptureConfiguration: VideoCaptureConfiguration) : ImageOutput
{
    var capture: (image: Bitmap) -> Unit = {}

    override fun onImageAvailable(presentationTimeUs: Long, bitmap: Bitmap)
    {
        this.capture(configure(bitmap, this.videoCaptureConfiguration))
    }

    override fun onDisabled() = Unit
}