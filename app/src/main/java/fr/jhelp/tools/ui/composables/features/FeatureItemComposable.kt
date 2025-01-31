package fr.jhelp.tools.ui.composables.features

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import fr.jhelp.tools.R
import fr.jhelp.tools.ui.theme.JHelpToolsTheme
import fr.jhelp.tools.ui.theme.SPACE_NORMAL
import fr.jhelp.tools.ui.theme.SPACE_SMALL
import fr.jhelp.tools.ui.theme.START_END_NORMAL
import fr.jhelp.tools.ui.theme.TOP_BOTTOM_NORMAL
import fr.jhelp.tools.ui.theme.Typography

@Composable
fun FeatureItemComposable(featureDescription: FeatureDescription, clickAction: () -> Unit)
{
    Card(onClick = clickAction,
         modifier = Modifier.padding(top = SPACE_SMALL)) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (icon, title, description) = createRefs()

            Image(painter = painterResource(id = featureDescription.icon),
                  contentDescription = null, // By purpose this image is just decorative
                  modifier = Modifier.constrainAs(icon) {
                      width = Dimension.wrapContent
                      height = Dimension.wrapContent

                      top.linkTo(parent.top, TOP_BOTTOM_NORMAL)
                      start.linkTo(parent.start, START_END_NORMAL)
                      // end free
                      bottom.linkTo(parent.bottom, TOP_BOTTOM_NORMAL)
                  })

            Text(text = stringResource(id = featureDescription.title),
                 style = Typography.titleMedium,
                 modifier = Modifier.constrainAs(title) {
                     width = Dimension.fillToConstraints
                     height = Dimension.wrapContent

                     top.linkTo(parent.top, TOP_BOTTOM_NORMAL)
                     start.linkTo(icon.end, SPACE_NORMAL)
                     end.linkTo(parent.end, START_END_NORMAL)
                     // bottom free
                 })

            Text(text = stringResource(id = featureDescription.description),
                 style = Typography.bodyLarge,
                 modifier = Modifier.constrainAs(description) {
                     width = Dimension.fillToConstraints
                     height = Dimension.wrapContent

                     top.linkTo(title.bottom, SPACE_SMALL)
                     start.linkTo(icon.end, SPACE_NORMAL)
                     end.linkTo(parent.end, START_END_NORMAL)
                     bottom.linkTo(parent.bottom, TOP_BOTTOM_NORMAL)
                 })
        }
    }
}

@Preview
@Composable
fun FeatureItemComposablePreview()
{
    val description = FeatureDescription(title = R.string.features_list_title, description = R.string.features_list_title)

    JHelpToolsTheme {
        FeatureItemComposable(description) { }
    }
}