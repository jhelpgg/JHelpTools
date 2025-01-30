package fr.jhelp.tools.ui.composables

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.constraintlayout.compose.ConstraintLayout
import fr.jhelp.tools.ui.composables.names.FeatureListNames

class FeatureListComposable
{
    @Composable
    fun Show(modifier: Modifier = Modifier)
    {
        ConstraintLayout(modifier=modifier) {
            val (title, list) = this.createRefs()

            LazyColumn(modifier= Modifier
                .testTag(FeatureListNames.LIST)
                .semantics(mergeDescendants = true) {}) {
                // TODO: Add features
            }
        }
    }
}