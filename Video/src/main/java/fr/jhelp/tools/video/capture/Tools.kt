package fr.jhelp.tools.video.capture

import android.graphics.Bitmap
import fr.jhelp.tools.video.capture.configuration.VideoCaptureConfiguration
import fr.jhelp.tools.video.capture.configuration.VideoCaptureConfigurationAsSource
import fr.jhelp.tools.video.capture.configuration.VideoCaptureConfigurationFix

/**
 * Configure a bitmap for a video capture.
 */
fun configure(bitmap: Bitmap, videoCaptureConfiguration: VideoCaptureConfiguration) : Bitmap =
    when(videoCaptureConfiguration) {
        VideoCaptureConfigurationAsSource -> bitmap
        is VideoCaptureConfigurationFix -> transform(bitmap, videoCaptureConfiguration)
    }

private fun transform(bitmap: Bitmap, videoCaptureConfiguration: VideoCaptureConfigurationFix) : Bitmap
{
    var result = bitmap

    if(result.config != videoCaptureConfiguration.bitmapConfig)
    {
        result = result.copy(videoCaptureConfiguration.bitmapConfig, true)
    }

    if(result.width != videoCaptureConfiguration.width || result.height != videoCaptureConfiguration.height)
    {
        result = Bitmap.createScaledBitmap(result, videoCaptureConfiguration.width, videoCaptureConfiguration.height, false)
    }

    return result
}
