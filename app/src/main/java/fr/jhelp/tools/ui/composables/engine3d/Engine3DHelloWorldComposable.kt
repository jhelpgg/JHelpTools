package fr.jhelp.tools.ui.composables.engine3d

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.jhelp.tools.engine3d.view.View3DComposable

/**
 * Hello world in 3D.
 *
 * A grey box
 */
@Composable
fun  Engine3DHelloWorldComposable(modifier: Modifier = Modifier)
{
    View3DComposable(modifier) {
        this.scenePosition {
            this.z = -2f
        }

        this.root {
            this.box {}
        }
    }
}