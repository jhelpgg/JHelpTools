package fr.jhelp.tools.ui.composables.features

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import fr.jhelp.tools.R
import fr.jhelp.tools.ui.theme.JHelpToolsTheme
import fr.jhelp.tools.ui.theme.SPACE_NORMAL
import fr.jhelp.tools.ui.theme.START_END_NORMAL
import fr.jhelp.tools.ui.theme.TOP_BOTTOM_NORMAL
import fr.jhelp.tools.ui.theme.Typography
import fr.jhelp.tools.utilities.injector.inject
import fr.jhelp.tools.utilities.injector.injected
import fr.jhelp.tools.viewmodel.action.navigation.NavigateTo
import fr.jhelp.tools.viewmodel.mock.NavigationMock
import fr.jhelp.tools.viewmodel.shared.NavigationModel
import fr.jhelp.tools.viewmodel.shared.Screen
import fr.jhelp.tools.viewmodel.status.NavigationStatus

class FeatureListComposable
{
    private val navigationModel by injected<NavigationModel>()

    @Composable
    fun Show(modifier: Modifier = Modifier)
    {
        ConstraintLayout(modifier = modifier) {
            val (title, list) = this.createRefs()
            Text(text = stringResource(id = R.string.features_list_title),
                 textAlign = TextAlign.Center,
                 style = Typography.titleLarge,
                 modifier = Modifier
                     .testTag(FeatureListNames.TITLE)
                     // Indicates to text to speech this is header
                     .semantics {
                         traversalIndex = 0f
                         heading()
                     }
                     .constrainAs(title) {
                         width = Dimension.fillToConstraints
                         height = Dimension.wrapContent

                         top.linkTo(parent.top, TOP_BOTTOM_NORMAL)
                         start.linkTo(parent.start, START_END_NORMAL)
                         end.linkTo(parent.end, START_END_NORMAL)
                         // bottom free
                     }
                )

            LazyColumn(modifier = Modifier
                .testTag(FeatureListNames.LIST)
                // Indicates to text to speech each element are grouped
                .semantics(mergeDescendants = true) {
                    traversalIndex = 1f
                }
                .constrainAs(list) {
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints

                    top.linkTo(title.bottom, SPACE_NORMAL)
                    start.linkTo(parent.start, START_END_NORMAL)
                    end.linkTo(parent.end, START_END_NORMAL)
                    bottom.linkTo(parent.bottom, TOP_BOTTOM_NORMAL)
                }) {

                for (screen in Screen.entries)
                {
                    val description = screen.description ?: continue

                    item {
                        FeatureItemComposable(description) { this@FeatureListComposable.navigationModel.action(NavigateTo(screen)) }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun FeatureListComposablePreview()
{
    inject<NavigationModel>(NavigationMock(NavigationStatus(Screen.FEATURES_LIST)))
    JHelpToolsTheme() {
        val composable = FeatureListComposable()
        composable.Show(Modifier.fillMaxSize())
    }
}