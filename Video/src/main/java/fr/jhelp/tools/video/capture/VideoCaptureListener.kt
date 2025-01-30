package fr.jhelp.tools.video.capture

import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import fr.jhelp.tools.video.capture.status.VideoCaptureClosed
import fr.jhelp.tools.video.capture.status.VideoCaptureIdle
import fr.jhelp.tools.video.capture.status.VideoCaptureLoading
import fr.jhelp.tools.video.capture.status.VideoCapturePlay
import fr.jhelp.tools.video.capture.status.VideoCaptureStatus
import fr.jhelp.tools.video.capture.status.VideoCaptureStopPlay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class VideoCaptureListener : Player.Listener
{
    private var currentVideoId = ""
    private val videoCaptureStatusMutable = MutableStateFlow<VideoCaptureStatus>(VideoCaptureIdle)
    val videoCaptureStatus = this.videoCaptureStatusMutable.asStateFlow()

    fun idle()
    {
        this.videoCaptureStatusMutable.value = VideoCaptureIdle
    }

    fun close()
    {
        this.videoCaptureStatusMutable.value = VideoCaptureClosed
    }

    override fun onIsLoadingChanged(isLoading: Boolean)
    {
        if (isLoading && this.videoCaptureStatusMutable.value !is VideoCapturePlay)
        {
            this.videoCaptureStatusMutable.value = VideoCaptureLoading(this.currentVideoId)
        }
    }

    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int)
    {
        mediaItem?.mediaId?.let { soundId -> this.currentVideoId = soundId }
    }

    override fun onIsPlayingChanged(isPlaying: Boolean)
    {
        if (isPlaying)
        {
            this.videoCaptureStatusMutable.value = VideoCapturePlay(this.currentVideoId)
        }
        else
        {
            this.videoCaptureStatusMutable.value = VideoCaptureStopPlay(this.currentVideoId)
        }
    }
}