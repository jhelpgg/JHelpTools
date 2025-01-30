package fr.jhelp.tools.viewmodel.implementation

import fr.jhelp.tools.viewmodel.action.navigation.NavigateTo
import fr.jhelp.tools.viewmodel.action.navigation.NavigationBack
import fr.jhelp.tools.viewmodel.shared.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.junit.Assert
import org.junit.Test
import java.util.concurrent.atomic.AtomicReference

class NavigationImplementationTests
{
    @Test
    fun currentScreen()
    {
        val navigation = NavigationImplementation()
        val currentScreenAtomic = AtomicReference<Screen>(Screen.EXIT)
        val job = CoroutineScope(Dispatchers.Default).launch {
            navigation.status.collect { status -> currentScreenAtomic.set(status.currentScreen) }
        }
        val currentScreen: () -> Screen = {
            Thread.sleep(32)
            currentScreenAtomic.get()
        }

        Assert.assertEquals(Screen.FEATURES_LIST, currentScreen())
        navigation.action(NavigationBack)
        Assert.assertEquals(Screen.EXIT, currentScreen())

        navigation.action(NavigateTo(Screen.FEATURES_LIST))
        Assert.assertEquals(Screen.FEATURES_LIST, currentScreen())
        navigation.action(NavigateTo(Screen.FEATURES_LIST))
        Assert.assertEquals(Screen.FEATURES_LIST, currentScreen())
        navigation.action(NavigationBack)
        Assert.assertEquals(Screen.FEATURES_LIST, currentScreen())
        navigation.action(NavigationBack)
        Assert.assertEquals(Screen.EXIT, currentScreen())

        navigation.action(NavigationBack)
        Assert.assertEquals(Screen.EXIT, currentScreen())

        job.cancel()
    }
}