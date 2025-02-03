package fr.jhelp.tools.ui.composables.coloring

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.jhelp.tools.R
import fr.jhelp.tools.coloring.ColoringComposable
import fr.jhelp.tools.utilities.source.SourceRaw

class ColoringWithPaletteComposable
{
    private val coloringComposable = ColoringComposable()

    init
    {
        this.coloringComposable.image(SourceRaw(R.raw.draw_fish))
    }

    @Composable
    fun Show(modifier: Modifier = Modifier)
    {
        this.coloringComposable.Draw(modifier)
    }
}