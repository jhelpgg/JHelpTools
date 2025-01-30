package fr.jhelp.tools.ui.composables

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import fr.jhelp.tools.MainActivity
import fr.jhelp.tools.ui.composables.features.FeatureListNames
import fr.jhelp.tools.utilities.injector.inject
import fr.jhelp.tools.viewmodel.action.navigation.NavigateTo
import fr.jhelp.tools.viewmodel.action.navigation.NavigationBack
import fr.jhelp.tools.viewmodel.mock.NavigationMock
import fr.jhelp.tools.viewmodel.shared.NavigationModel
import fr.jhelp.tools.viewmodel.shared.Screen
import fr.jhelp.tools.viewmodel.status.NavigationStatus
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test

class MainScreenComposableTests
{
    companion object
    {
        val navigationStatus = MutableStateFlow<NavigationStatus>(NavigationStatus(Screen.FEATURES_LIST))
        val navigationMock = NavigationMock(MainScreenComposableTests.navigationStatus) { action ->
            when (action)
            {
                NavigationBack -> navigationStatus.value = NavigationStatus(Screen.EXIT)
                is NavigateTo  -> navigationStatus.value = NavigationStatus(action.screen)
            }
        }

        @JvmStatic
        @BeforeClass
        fun initialize()
        {
          inject<NavigationModel>(MainScreenComposableTests.navigationMock)
        }
    }

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testNavigation()
    {
        this.composeTestRule
            .onNodeWithTag(FeatureListNames.TITLE)
            .assertExists()
            .assertIsDisplayed()

        this.composeTestRule
            .onNodeWithTag(FeatureListNames.LIST)
            .assertExists()
            .assertIsDisplayed()

        // TODO click on item
    }
}