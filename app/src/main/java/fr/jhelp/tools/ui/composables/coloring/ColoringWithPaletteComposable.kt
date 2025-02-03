package fr.jhelp.tools.ui.composables.coloring

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.jhelp.tools.coloring.ColoringComposable

class ColoringWithPaletteComposable
{
    private val coloringComposable = ColoringComposable()

    @Composable
    fun Show(modifier: Modifier = Modifier)
    {
        this.coloringComposable.Draw(modifier)
    }
}