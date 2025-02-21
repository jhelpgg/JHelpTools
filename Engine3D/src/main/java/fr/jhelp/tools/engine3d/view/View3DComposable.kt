package fr.jhelp.tools.engine3d.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import fr.jhelp.tools.engine3d.dsl.SceneCreator
import java.util.concurrent.atomic.AtomicBoolean

class View3DComposable
{
    private val viewCreated = AtomicBoolean(false)
    private lateinit var view: View3D

    /**
     * Call in composable context to draw the 3D
     *
     * @param modifier : Modifier used to layout the 3D view
     * @param scene : Describe how to create the 3D view
     */
    @Composable
    fun Draw(modifier: Modifier = Modifier,
             scene: SceneCreator.() -> Unit = {
                 this.scenePosition { this.z = -2f }
                 this.root { this.box {} }
             })
    {
        AndroidView<View3D>(
            modifier = modifier,
            factory = { context ->
                this.view = View3D(context)
                this.view.tree(scene)
                this.view
            })
    }
}