package fr.jhelp.tools.video.capture

import android.graphics.Bitmap
import android.os.SystemClock
import fr.jhelp.tools.utilities.coroutine.Locker
import fr.jhelp.tools.utilities.coroutine.onFailure
import fr.jhelp.tools.utilities.coroutine.onSuccess
import fr.jhelp.tools.utilities.source.Source
import fr.jhelp.tools.video.capture.information.VideoInformation
import fr.jhelp.tools.video.capture.information.VideoInformationCurrentVideo
import fr.jhelp.tools.video.capture.information.VideoInformationNoVideo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicReference
import kotlin.math.max

class VideoCaptureManager private constructor(private val widthConstraint: Int = -1, val heightConstraint: Int = -1)
{
    companion object
    {
        fun create(): VideoCaptureManager =
            VideoCaptureManager()

        fun create(width: Int, height: Int): VideoCaptureManager =
            VideoCaptureManager(max(32, width), max(32, height))
    }

    private var videoCapture: VideoCapture? = null
    private var startTime: Long = 0L
    private var totalTime = 0L
    private var jobPlaying: Job? = null
    private val locker = Locker()
    private val videoInformationMutable = MutableStateFlow<VideoInformation>(VideoInformationNoVideo)
    private var status = AtomicReference<VideoCaptureManagerStatus>(VideoCaptureManagerStatus.IDLE)
    val videoInformation: StateFlow<VideoInformation> = this.videoInformationMutable.asStateFlow()
    var capture: (Bitmap) -> Unit = {}

    fun play(video: Source, onReady: (result: Result<Unit>) -> Unit = {})
    {
        if (this.status.get() == VideoCaptureManagerStatus.CLOSE)
        {
            onReady(Result.failure(Exception("Video capture manager closed")))
            return
        }

        this.jobPlaying?.cancel("Play")
        this.videoCapture?.close()
        val deferred =
            if (this.widthConstraint == -1 || this.heightConstraint == -1)
            {
                VideoCapture.create(video)
            }
            else
            {
                VideoCapture.create(video, this.widthConstraint, this.heightConstraint)
            }

        deferred.onFailure { exception -> onReady(Result.failure(exception)) }
        deferred.onSuccess { videoCapture ->
            this.videoCapture = videoCapture
            this.startTime = System.currentTimeMillis()
            this.totalTime = videoCapture.videoDuration
            this.status.set(VideoCaptureManagerStatus.PLAYING)
            this.jobPlaying = CoroutineScope(Dispatchers.Default).launch {
                this@VideoCaptureManager.playing()
            }
            onReady(Result.success(Unit))
            this.videoInformationMutable.value =
                VideoInformationCurrentVideo(source = video,
                                             width = videoCapture.videoWidth, height = videoCapture.videoHeight,
                                             duration = videoCapture.videoDuration)
        }
    }

    fun pause()
    {
        this.status.compareAndSet(VideoCaptureManagerStatus.PLAYING, VideoCaptureManagerStatus.PAUSE)
    }

    fun stop()
    {
        this.status.compareAndSet(VideoCaptureManagerStatus.PLAYING, VideoCaptureManagerStatus.STOP)
    }

    fun resume()
    {
        this.status.compareAndSet(VideoCaptureManagerStatus.PAUSE, VideoCaptureManagerStatus.PLAYING)
        this.status.compareAndSet(VideoCaptureManagerStatus.STOP, VideoCaptureManagerStatus.PLAYING)
        this.locker.unlock()
    }

    fun close()
    {
        this.jobPlaying?.cancel("Close")
        this.videoCapture?.close()
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

            val status = this.status.get()
            if (status == VideoCaptureManagerStatus.PAUSE || status == VideoCaptureManagerStatus.STOP)
            {
                if (status == VideoCaptureManagerStatus.PAUSE)
                {
                    this.videoCapture?.pause()
                }
                else
                {
                    this.videoCapture?.stop()
                }
                this.locker.lock()
                this.videoCapture?.play()
                this.startTime = System.currentTimeMillis() - if (status == VideoCaptureManagerStatus.PAUSE) time else 0L
            }

            delay(wait)
            time = System.currentTimeMillis() - this.startTime
        }

        this.status.set(VideoCaptureManagerStatus.STOP)
        this.videoInformationMutable.value = VideoInformationNoVideo
        this.jobPlaying?.cancel("Finished")
    }
}