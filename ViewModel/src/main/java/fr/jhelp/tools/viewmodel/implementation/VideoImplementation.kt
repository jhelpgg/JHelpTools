package fr.jhelp.tools.viewmodel.implementation

import android.graphics.Bitmap
import android.os.SystemClock
import android.util.Log
import fr.jhelp.tools.utilities.coroutine.Locker
import fr.jhelp.tools.utilities.coroutine.onFailure
import fr.jhelp.tools.utilities.coroutine.onSuccess
import fr.jhelp.tools.utilities.injector.injected
import fr.jhelp.tools.utilities.source.Source
import fr.jhelp.tools.video.capture.VideoCapture
import fr.jhelp.tools.viewmodel.action.video.VideoAction
import fr.jhelp.tools.viewmodel.action.video.VideoActionCapture
import fr.jhelp.tools.viewmodel.action.video.VideoActionPlay
import fr.jhelp.tools.viewmodel.action.video.VideoActionPlayAgain
import fr.jhelp.tools.viewmodel.action.video.VideoActionStop
import fr.jhelp.tools.viewmodel.shared.MainApplicationModel
import fr.jhelp.tools.viewmodel.shared.VideoModel
import fr.jhelp.tools.viewmodel.status.MainApplicationStatus
import fr.jhelp.tools.viewmodel.status.VideoStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.max

internal class VideoImplementation : VideoModel
{
    private val mainApplicationModel: MainApplicationModel by injected<MainApplicationModel>()
    private val statusMutable = MutableStateFlow<VideoStatus>(VideoStatus.IDLE)
    private var jobApplication: Job? = null
    private var videoCapture: VideoCapture? = null
    private var capture: (Bitmap) -> Unit = {}
    private var startTime: Long = 0L
    private var totalTime = 0L
    private var jobPlaying: Job? = null
    private val locker = Locker()
    private var paused = false
    override val status: StateFlow<VideoStatus> = this.statusMutable.asStateFlow()

    init
    {
        this.jobApplication = CoroutineScope(Dispatchers.Default).launch {
            this@VideoImplementation.mainApplicationModel.status.collect { status ->
                when (status)
                {
                    MainApplicationStatus.PAUSE  -> this@VideoImplementation.pause(true)
                    MainApplicationStatus.ACTIVE -> this@VideoImplementation.resume()
                    MainApplicationStatus.EXIT   ->
                    {
                        this@VideoImplementation.jobApplication?.cancel("Exit")
                        this@VideoImplementation.videoCapture?.close()
                    }

                    MainApplicationStatus.IDLE   -> Unit
                }
            }
        }
    }

    override fun action(action: VideoAction)
    {
        when (action)
        {
            is VideoActionCapture -> this.capture = action.capture
            is VideoActionPlay    -> this.play(action.video)
            VideoActionStop       -> this.pause(false)
            VideoActionPlayAgain  -> this.resume()
        }
    }

    private fun play(video: Source)
    {
        this.jobPlaying?.cancel("Play")
        this.videoCapture?.close()
        val deferred = VideoCapture.create(video)
        deferred.onFailure { exception -> exception.printStackTrace() }
        deferred.onSuccess { videoCapture ->
            this.videoCapture = videoCapture
            this.statusMutable.value = VideoStatus.PLAYING
            this.startTime = System.currentTimeMillis()
            this.totalTime = videoCapture.videoDuration
            this.jobPlaying = CoroutineScope(Dispatchers.Default).launch {
                this@VideoImplementation.playing()
            }
        }
    }

    private fun pause(paused: Boolean)
    {
        if (this.statusMutable.value == VideoStatus.PLAYING)
        {
            this.paused = paused
            this.statusMutable.value = VideoStatus.PAUSED
        }
    }

    private fun resume()
    {
        if (this.statusMutable.value == VideoStatus.PAUSED)
        {
            this.statusMutable.value = VideoStatus.PLAYING
            this.locker.unlock()
        }
    }

    private suspend fun playing()
    {
        var time = System.currentTimeMillis() - this.startTime
        this.videoCapture?.play()

        while (time <= this.totalTime)
        {
            val tick = SystemClock.elapsedRealtime()
            this.videoCapture?.imageAtTime(time)?.let { image ->
                this.capture(image)
            }
            val wait = max(1L, 40L + tick - SystemClock.elapsedRealtime())

            if (this.statusMutable.value == VideoStatus.PAUSED)
            {
                this.videoCapture?.pause()
                this.locker.lock()
                this.videoCapture?.play()
                this.startTime = System.currentTimeMillis() - if (this.paused) time else 0L
            }

            delay(wait)
            time = System.currentTimeMillis() - this.startTime
        }

        this.statusMutable.value = VideoStatus.STOPPED
        this.jobPlaying?.cancel("Finished")
    }
}