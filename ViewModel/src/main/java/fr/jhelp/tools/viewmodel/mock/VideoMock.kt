package fr.jhelp.tools.viewmodel.mock

import fr.jhelp.tools.viewmodel.action.video.VideoAction
import fr.jhelp.tools.viewmodel.shared.VideoModel
import fr.jhelp.tools.viewmodel.status.VideoStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Video mock can be used in tests.
 */
class VideoMock(override val status: StateFlow<VideoStatus>,
                private val doAction: (VideoAction) -> Unit = {}) : VideoModel
{
    constructor(videoStatus: VideoStatus,
                doAction: (VideoAction) -> Unit = {}) : this(MutableStateFlow(videoStatus), doAction)

    override fun action(action: VideoAction)
    {
        this.doAction(action)
    }
}