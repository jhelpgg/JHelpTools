package fr.jhelp.tools.engine3d.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import fr.jhelp.tools.engine3d.dsl.SceneCreator

/**
 * Call in composable context to draw the 3D
 *
 * @param modifier : Modifier used to layout the 3D view
 * @param scene : Describe how to create the 3D view
 */
@Composable
fun View3DComposable(modifier: Modifier = Modifier,
                     scene: SceneCreator.() -> Unit = {
                         this.scenePosition { this.z = -2f }
                         this.root { this.box {} }
                     })
{
    var view3D by remember { mutableStateOf<View3D?>(null) }

    AndroidView<View3D>(
        modifier = modifier,
        factory = { context ->
            if (view3D == null)
            {
                view3D = View3D(context)
                view3D!!.tree(scene)
            }

            view3D!!
        })
}