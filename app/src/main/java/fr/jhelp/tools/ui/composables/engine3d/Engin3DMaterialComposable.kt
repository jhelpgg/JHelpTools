package fr.jhelp.tools.ui.composables.engine3d

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import fr.jhelp.tools.R
import fr.jhelp.tools.coloring.ColoringPalette
import fr.jhelp.tools.engine3d.resources.ResourcesAccess
import fr.jhelp.tools.engine3d.scene.Color3D
import fr.jhelp.tools.engine3d.scene.Material
import fr.jhelp.tools.engine3d.view.View3DComposable
import fr.jhelp.tools.ui.theme.TOP_BOTTOM_NORMAL

/**
 * Show 3D cube with material based on texture
 *
 * We can change the material diffuse color on change selected color on palette
 */
@Composable
fun Engin3DMaterialComposable(modifier: Modifier = Modifier)
{
    val materialDuck = Material()
    materialDuck.texture = ResourcesAccess.obtainTexture(R.drawable.background_duck)

    ConstraintLayout(modifier) {
        val (view3D, colorChooser) = createRefs()

        View3DComposable(modifier =
                         Modifier.constrainAs(view3D) {
                             this.width = Dimension.fillToConstraints
                             this.height = Dimension.fillToConstraints

                             this.top.linkTo(parent.top)
                             this.bottom.linkTo(colorChooser.top)
                             this.start.linkTo(parent.start)
                             this.end.linkTo(parent.end)
                         }) {
            this.scenePosition {
                this.z = -2f
            }

            this.root {
                this.box {
                    this.material = materialDuck
                }
            }
        }

        ColoringPalette(modifier =
                        Modifier.constrainAs(colorChooser) {
                            this.width = Dimension.fillToConstraints
                            this.height = Dimension.wrapContent

                            // top free
                            this.start.linkTo(parent.start)
                            this.end.linkTo(parent.end)
                            this.bottom.linkTo(parent.bottom, TOP_BOTTOM_NORMAL)
                        }) { color -> materialDuck.diffuse = Color3D(color = color) }
    }
}