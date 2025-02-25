package fr.jhelp.tools.ui.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import fr.jhelp.tools.ui.composables.coloring.ColoringWithPaletteComposable
import fr.jhelp.tools.ui.composables.engine3d.Engin3DMaterialComposable
import fr.jhelp.tools.ui.composables.engine3d.Engine3DComposable
import fr.jhelp.tools.ui.composables.engine3d.Engine3DHelloWorldComposable
import fr.jhelp.tools.ui.composables.features.FeatureListComposable
import fr.jhelp.tools.ui.composables.video.VideoComposable
import fr.jhelp.tools.utilities.injector.injected
import fr.jhelp.tools.viewmodel.shared.NavigationModel
import fr.jhelp.tools.viewmodel.shared.Screen

class MainScreenComposable
{
    private val navigationModel by injected<NavigationModel>()
    private val featureListComposable by lazy { FeatureListComposable() }
    private val videoComposable by lazy { VideoComposable() }
    private val engine3DComposable by lazy { Engine3DComposable() }
    private val coloringWithPaletteComposable by lazy { ColoringWithPaletteComposable() }

    @Composable
    fun Show(modifier: Modifier = Modifier)
    {
        val navigationStatus by navigationModel.status.collectAsState()

        when (navigationStatus.currentScreen)
        {
            Screen.FEATURES_LIST    -> this.featureListComposable.Show(modifier)
            Screen.VIDEO            -> this.videoComposable.Show(modifier)
            Screen.ENGINE3D         -> this.engine3DComposable.Show(modifier)
            Screen.COLORING         -> this.coloringWithPaletteComposable.Show(modifier)
            Screen.HELLO_WORLD_3D   -> Engine3DHelloWorldComposable(modifier)
            Screen.MATERIAL_TEXTURE -> Engin3DMaterialComposable(modifier)
            Screen.EXIT             -> Unit
        }
    }
}