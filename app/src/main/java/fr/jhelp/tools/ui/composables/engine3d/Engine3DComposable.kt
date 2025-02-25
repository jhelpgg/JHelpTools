package fr.jhelp.tools.ui.composables.engine3d

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import fr.jhelp.tools.R
import fr.jhelp.tools.engine3d.animation.AnimationPlayer
import fr.jhelp.tools.engine3d.dsl.material
import fr.jhelp.tools.engine3d.dsl.materialReference
import fr.jhelp.tools.engine3d.scene.GREY
import fr.jhelp.tools.engine3d.scene.LIGHT_RED
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Engine3DComposable
{
    private val video = SourceRaw(R.raw.roule)
    private lateinit var textureVideo: TextureVideo
    private lateinit var animationPlayerMaterial: AnimationPlayer

    @Composable
    fun Show(modifier: Modifier = Modifier)
    {
        val materialReference = materialReference()

        View3DComposable(modifier) {
            this.scenePosition {
                this.angleX = -32f
                this.z = -6f
            }

            this.root {
                this.field(
                    zFunction = cos(X) * sin(Y),
                    xStart = -PI_FLOAT, xEnd = PI_FLOAT, xNumberSteps = 25,
                    yStart = -PI_FLOAT, yEnd = PI_FLOAT, yNumberSteps = 25) {
                    this.material(materialReference) {
                        this@Engine3DComposable.textureVideo = this.textureVideo()
                    }
                }
            }

            this@Engine3DComposable.animationPlayerMaterial = this.animationPlayer {
                this.material(materialReference) {
                    this.diffuse {
                        this.timeMilliseconds = 4096
                        this.color3D = LIGHT_RED
                    }

                    this.diffuse {
                        this.timeMilliseconds = 8192
                        this.color3D = GREY
                    }
                }
            }
        }

        LaunchedEffect(Unit) {
            CoroutineScope(Dispatchers.Default).launch {
                delay(1024)
                this@Engine3DComposable.textureVideo.play(this@Engine3DComposable.video)
                delay(4096)
                this@Engine3DComposable.animationPlayerMaterial.play()
            }
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