package fr.jhelp.tools.video.capture

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import androidx.media3.exoplayer.ExoPlayer
import fr.jhelp.tools.utilities.injector.injected
import fr.jhelp.tools.video.capture.configuration.VideoCaptureConfiguration
import fr.jhelp.tools.video.capture.configuration.VideoCaptureConfigurationAsSource
import fr.jhelp.tools.video.capture.status.VideoCaptureIdle
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Represents a video capture frames.
 * @property configuration Video output configuration
 */
@SuppressLint("UnsafeOptInUsageError")
class VideoCapture(val configuration: VideoCaptureConfiguration = VideoCaptureConfigurationAsSource)
{
    private val closedStatus = AtomicBoolean(false)
    private val context: Context by injected<Context>()
    private val player = ExoPlayer.Builder(this.context).build()
    private val listener = VideoCaptureListener()
    private val closedState = AtomicBoolean(false)
    private var wasPlayingBeforePause = false
    private val output: VideoCaptureOutput = VideoCaptureOutput(this.configuration)
    val status = this.listener.videoCaptureStatus

    init
    {
        this.player.addListener(this.listener)
        this.player.prepare()
        this.player.setImageOutput(this.output)
    }

    fun capture(capture: (image: Bitmap) -> Unit)
    {
        this.output.capture = capture
    }

    fun play(video: Video)
    {
        if (this.closedState.get())
        {
            return
        }

        this.player.setMediaItem(video.mediaItem)
        this.player.play()
    }

    fun pause()
    {
        if (this.closedStatus.get())
        {
            return
        }

        this.wasPlayingBeforePause = this.player.isPlaying

        if (this.player.isPlaying)
        {
            this.player.pause()
        }

        this.listener.idle()
    }

    fun resume()
    {
        if (this.closedStatus.get())
        {
            return
        }

        if (this.wasPlayingBeforePause && this.status.value == VideoCaptureIdle)
        {
            this.player.play()
        }
    }

    fun close()
    {
        if (this.closedState.compareAndSet(false, true))
        {
            this.player.removeListener(this.listener)
            this.listener.close()
            this.player.release()
        }
    }
}