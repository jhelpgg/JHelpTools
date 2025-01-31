package fr.jhelp.tools.ui.composables.engine3d

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import fr.jhelp.tools.R
import fr.jhelp.tools.engine3d.scene.TextureVideo
import fr.jhelp.tools.engine3d.scene.geometry.Box
import fr.jhelp.tools.engine3d.view.View3DComposable
import fr.jhelp.tools.ui.theme.JHelpToolsTheme
import fr.jhelp.tools.utilities.source.SourceRaw

class Engine3DComposable
{
    private val view3DComposable = View3DComposable()
    private val textureVideo = TextureVideo()
    private val video = SourceRaw(R.raw.roule)

    @Composable
    fun Show(modifier: Modifier = Modifier)
    {
        this.view3DComposable.Draw(modifier) {
            this.scenePosition { this.z = -2f }
            val box = Box()
            box.material.texture = textureVideo
            this.scene3D.root.add(box)

            Thread{
                Thread.sleep(1024)
                textureVideo.play(video)
            }.start()
        }
    }
}

@Preview
@Composable
fun Engine3DComposablePreview()
{
    val composable = Engine3DComposable()
    JHelpToolsTheme {
        composable.Show(Modifier.fillMaxSize())
    }
}