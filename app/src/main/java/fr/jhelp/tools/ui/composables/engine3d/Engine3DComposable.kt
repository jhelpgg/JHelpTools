package fr.jhelp.tools.ui.composables.engine3d

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import fr.jhelp.tools.R
import fr.jhelp.tools.engine3d.scene.TextureVideo
import fr.jhelp.tools.engine3d.view.View3DComposable
import fr.jhelp.tools.mathformal.dsl.X
import fr.jhelp.tools.mathformal.dsl.Y
import fr.jhelp.tools.mathformal.dsl.cos
import fr.jhelp.tools.mathformal.dsl.sin
import fr.jhelp.tools.mathformal.dsl.times
import fr.jhelp.tools.ui.theme.JHelpToolsTheme
import fr.jhelp.tools.utilities.math.PI_FLOAT
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
            this.scenePosition {
                this.angleX = -32f
                this.z = -6f
            }

            this.root {
                this.field(
                    zFunction = cos(X) * sin(Y),
                    xStart = -PI_FLOAT, xEnd = PI_FLOAT, xNumberSteps = 25,
                    yStart = -PI_FLOAT, yEnd = PI_FLOAT, yNumberSteps = 25) {
                    this.material.texture = textureVideo
                }
            }

            Thread {
                Thread.sleep(1024)
                textureVideo.play(video)
            }.start()
        }
    }
}
/*
this.scenePosition {
                    this.angleX = -32f
                    this.z = -6f
                }
                this.root {
                    this.field(
                        functionZ = cos(X) * sin(Y),
                        xStart = -PI_FLOAT, xEnd = PI_FLOAT, numberPartX = 10,
                        yStart = -PI_FLOAT, yEnd = PI_FLOAT, numberPartY = 10) {
                        this.material(material)
                    }
                }
 */

@Preview
@Composable
fun Engine3DComposablePreview()
{
    val composable = Engine3DComposable()
    JHelpToolsTheme {
        composable.Show(Modifier.fillMaxSize())
    }
}