package fr.jhelp.tools.ui.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import fr.jhelp.tools.utilities.injector.injected
import fr.jhelp.tools.viewmodel.shared.NavigationModel
import fr.jhelp.tools.viewmodel.shared.Screen

class MainScreenComposable
{
    private val navigationModel by injected<NavigationModel>()
    private val featureListComposable = FeatureListComposable()

    @Composable
    fun Show(modifier: Modifier = Modifier)
    {
        val navigationStatus by navigationModel.status.collectAsState()

        when (navigationStatus.currentScreen)
        {
            Screen.FEATURES_LIST -> this.featureListComposable.Show(modifier)
            Screen.EXIT          -> Unit
        }
    }
}