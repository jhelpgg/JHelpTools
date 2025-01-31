package fr.jhelp.tools.ui.composables.video

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import fr.jhelp.tools.R
import fr.jhelp.tools.ui.theme.JHelpToolsTheme
import fr.jhelp.tools.utilities.defaultBitmap
import fr.jhelp.tools.utilities.injector.inject
import fr.jhelp.tools.utilities.injector.injected
import fr.jhelp.tools.utilities.source.SourceRaw
import fr.jhelp.tools.viewmodel.action.video.VideoActionCapture
import fr.jhelp.tools.viewmodel.action.video.VideoActionPlay
import fr.jhelp.tools.viewmodel.mock.VideoMock
import fr.jhelp.tools.viewmodel.shared.VideoModel
import fr.jhelp.tools.viewmodel.status.VideoStatus

class VideoComposable
{
    private val videoModel: VideoModel by injected<VideoModel>()
    private val video = SourceRaw(R.raw.roule) //SourceAsset("video/roule.mp4")

    @Composable
    fun Show(modifier: Modifier)
    {
        var bitmap: Bitmap by remember { mutableStateOf(defaultBitmap) }
        this.videoModel.action(VideoActionCapture { image ->
            bitmap = image
        })

        LaunchedEffect(Unit) {
            this@VideoComposable.videoModel.action(VideoActionPlay(this@VideoComposable.video))
        }

        Image(bitmap = bitmap.asImageBitmap(), contentDescription = null, modifier = modifier)
    }
}

@Preview
@Composable
fun VideoComposablePreview()
{
    inject<VideoModel>(VideoMock(VideoStatus.IDLE))
    JHelpToolsTheme {
        VideoComposable().Show(Modifier.fillMaxSize())
    }
}