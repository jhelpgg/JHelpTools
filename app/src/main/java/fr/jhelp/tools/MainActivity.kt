package fr.jhelp.tools

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import fr.jhelp.tools.ui.composables.MainScreenComposable
import fr.jhelp.tools.ui.theme.JHelpToolsTheme
import fr.jhelp.tools.utilities.coroutine.whenValueMatchDo
import fr.jhelp.tools.utilities.injector.injected
import fr.jhelp.tools.viewmodel.action.navigation.NavigationBack
import fr.jhelp.tools.viewmodel.shared.NavigationModel
import fr.jhelp.tools.viewmodel.shared.Screen
import kotlinx.coroutines.flow.filter

class MainActivity : ComponentActivity()
{
    private val navigationModel by injected<NavigationModel>()
    private val mainScreenComposable = MainScreenComposable()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JHelpToolsTheme {
                this.mainScreenComposable.Show(Modifier.fillMaxSize())
            }
        }

        this.onBackPressedDispatcher.addCallback { this@MainActivity.navigationModel.action(NavigationBack) }
        this.navigationModel.status.whenValueMatchDo({ status -> status.currentScreen == Screen.EXIT }) { _ -> this.finish() }
    }
}

