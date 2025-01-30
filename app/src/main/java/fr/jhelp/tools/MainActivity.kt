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
import fr.jhelp.tools.viewmodel.action.main.MainApplicationAction
import fr.jhelp.tools.viewmodel.action.navigation.NavigationBack
import fr.jhelp.tools.viewmodel.shared.MainApplicationModel
import fr.jhelp.tools.viewmodel.shared.NavigationModel
import fr.jhelp.tools.viewmodel.shared.Screen

class MainActivity : ComponentActivity()
{
    private val navigationModel by injected<NavigationModel>()
    private val mainApplicationModel by injected<MainApplicationModel>()
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
        this.navigationModel.status.whenValueMatchDo({ status -> status.currentScreen == Screen.EXIT }) { _ ->
            this.mainApplicationModel.action(MainApplicationAction.EXIT)
            this.finish()
        }
    }

    override fun onPause()
    {
        this.mainApplicationModel.action(MainApplicationAction.PAUSE)
        super.onPause()
    }

    override fun onResume()
    {
        super.onResume()
        this.mainApplicationModel.action(MainApplicationAction.RESUME)
    }
}

