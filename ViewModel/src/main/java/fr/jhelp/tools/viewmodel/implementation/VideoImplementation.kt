package fr.jhelp.tools.viewmodel.implementation

import fr.jhelp.tools.utilities.coroutine.whenValueMatchDo
import fr.jhelp.tools.utilities.injector.injected
import fr.jhelp.tools.video.capture.VideoCapture
import fr.jhelp.tools.video.capture.status.VideoCaptureIdle
import fr.jhelp.tools.viewmodel.action.video.VideoAction
import fr.jhelp.tools.viewmodel.action.video.VideoActionCapture
import fr.jhelp.tools.viewmodel.action.video.VideoActionPlay
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
import kotlinx.coroutines.launch

internal class VideoImplementation : VideoModel
{
    private val mainApplicationModel: MainApplicationModel by injected<MainApplicationModel>()
    private val statusMutable = MutableStateFlow<VideoStatus>(VideoStatus(VideoCaptureIdle))
    private val videoCapture = VideoCapture()
    private val jobCapture: Job
    private var jobApplication: Job? = null
    override val status: StateFlow<VideoStatus> = this.statusMutable.asStateFlow()

    init
    {
        this.jobCapture = CoroutineScope(Dispatchers.Default).launch {
            this@VideoImplementation.videoCapture.status.collect { status ->
                this@VideoImplementation.statusMutable.value = VideoStatus(status)
            }
        }

        this.jobApplication = CoroutineScope(Dispatchers.Default).launch {
            this@VideoImplementation.mainApplicationModel.status.collect { status ->
                when (status)
                {
                    MainApplicationStatus.PAUSE  -> this@VideoImplementation.videoCapture.pause()
                    MainApplicationStatus.ACTIVE -> this@VideoImplementation.videoCapture.resume()
                    MainApplicationStatus.EXIT   ->
                    {
                        this@VideoImplementation.jobCapture.cancel("Exit")
                        this@VideoImplementation.jobApplication?.cancel("Exit")
                        this@VideoImplementation.videoCapture.close()
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
            is VideoActionCapture -> this.videoCapture.capture(action.capture)
            is VideoActionPlay    -> this.videoCapture.play(action.video)
        }
    }
}