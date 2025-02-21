package fr.jhelp.tools.viewmodel.implementation

import fr.jhelp.tools.utilities.injector.injected
import fr.jhelp.tools.viewmodel.action.navigation.NavigateTo
import fr.jhelp.tools.viewmodel.action.navigation.NavigationAction
import fr.jhelp.tools.viewmodel.action.navigation.NavigationBack
import fr.jhelp.tools.viewmodel.action.video.VideoActionPlayAgain
import fr.jhelp.tools.viewmodel.action.video.VideoActionStop
import fr.jhelp.tools.viewmodel.shared.NavigationModel
import fr.jhelp.tools.viewmodel.shared.Screen
import fr.jhelp.tools.viewmodel.shared.VideoModel
import fr.jhelp.tools.viewmodel.status.NavigationStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Stack

/**
 * Navigation model implementation
 */
internal class NavigationImplementation : NavigationModel
{
    private val videoModel by injected<VideoModel>()
    private val statusMutable = MutableStateFlow(NavigationStatus(Screen.FEATURES_LIST))

    /**
     * Status of navigation.
     */
    override val status: StateFlow<NavigationStatus> = this.statusMutable.asStateFlow()
    private val screensStack = Stack<Screen>()

    /**
     * Ask navigation to do an action
     */
    override fun action(action: NavigationAction)
    {
        when (action)
        {
            is NavigateTo  -> this.navigateTo(action.screen)
            NavigationBack -> this.navigateBack()
        }
    }

    private fun navigateTo(screen: Screen)
    {
        this.screensStack.push(this.statusMutable.value.currentScreen)
        this.statusMutable.value = NavigationStatus(screen)

        if (screen == Screen.VIDEO)
        {
            this.videoModel.action(VideoActionPlayAgain)
        }
    }

    private fun navigateBack()
    {
        if (this.screensStack.isEmpty())
        {
            this.statusMutable.value = NavigationStatus(Screen.EXIT)
        }
        else
        {
            if (this.statusMutable.value.currentScreen == Screen.VIDEO)
            {
                this.videoModel.action(VideoActionStop)
            }

            this.statusMutable.value = this.statusMutable.value.copy(currentScreen = this.screensStack.pop())
        }
    }
}