package fr.jhelp.tools.viewmodel.implementation

import fr.jhelp.tools.utilities.injector.injected
import fr.jhelp.tools.utilities.source.Source
import fr.jhelp.tools.video.capture.VideoCaptureManager
import fr.jhelp.tools.video.capture.information.VideoInformationNoVideo
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

internal class VideoImplementation : VideoModel
{
    private val mainApplicationModel: MainApplicationModel by injected<MainApplicationModel>()
    private val statusMutable = MutableStateFlow<VideoStatus>(VideoStatus.IDLE)
    private var jobApplication: Job? = null
    private var jobVideoInformation: Job? = null
    private val videoCaptureManager = VideoCaptureManager.create()
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
                        this@VideoImplementation.jobVideoInformation?.cancel("Exit")
                        this@VideoImplementation.videoCaptureManager.close()
                    }

                    MainApplicationStatus.IDLE   -> Unit
                }
            }
        }

        this.jobVideoInformation = CoroutineScope(Dispatchers.Default).launch {
            this@VideoImplementation.videoCaptureManager.videoInformation.filter { videoInformation -> videoInformation is VideoInformationNoVideo }.collect { videoInformation ->
                if (this@VideoImplementation.statusMutable.value != VideoStatus.IDLE)
                {
                    this@VideoImplementation.statusMutable.value = VideoStatus.STOPPED
                }
            }
        }
    }

    override fun action(action: VideoAction)
    {
        when (action)
        {
            is VideoActionCapture -> this.videoCaptureManager.capture = action.capture
            is VideoActionPlay    -> this.play(action.video)
            VideoActionStop       -> this.pause(false)
            VideoActionPlayAgain  -> this.resume()
        }
    }

    private fun play(video: Source)
    {
        this.videoCaptureManager.play(video) { result ->
            result.onSuccess { this.statusMutable.value = VideoStatus.PLAYING }
                .onFailure { exception -> exception.printStackTrace() }
        }
    }

    private fun pause(paused: Boolean)
    {
        if (this.statusMutable.value == VideoStatus.PLAYING)
        {

            if (paused)
            {
                this.videoCaptureManager.pause()
            }
            else
            {
                this.videoCaptureManager.stop()
            }

            this.statusMutable.value = VideoStatus.PAUSED
        }
    }

    private fun resume()
    {
        if (this.statusMutable.value == VideoStatus.PAUSED)
        {
            this.statusMutable.value = VideoStatus.PLAYING
            this.videoCaptureManager.resume()
        }
    }
}