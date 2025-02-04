package fr.jhelp.tools.ui.composables.coloring

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import fr.jhelp.tools.R
import fr.jhelp.tools.coloring.ColoringComposable
import fr.jhelp.tools.coloring.ColoringPalette
import fr.jhelp.tools.ui.theme.SPACE_SMALL
import fr.jhelp.tools.ui.theme.START_END_NORMAL
import fr.jhelp.tools.ui.theme.TOP_BOTTOM_NORMAL
import fr.jhelp.tools.utilities.math.COLOR_BLACK
import fr.jhelp.tools.utilities.source.SourceRaw

class ColoringWithPaletteComposable
{
    private val coloringComposable = ColoringComposable()

    init
    {
        this.coloringComposable.image(SourceRaw(R.raw.draw_fish))
        this.coloringComposable.precision = 5
    }

    @Composable
    fun Show(modifier: Modifier = Modifier)
    {
        ConstraintLayout(modifier) {
            val (draw, palette) = createRefs()
            this@ColoringWithPaletteComposable.coloringComposable.Draw(Modifier.constrainAs(draw) {
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints

                top.linkTo(parent.top)
                bottom.linkTo(palette.top, SPACE_SMALL)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })

            ColoringPalette(selectColor = COLOR_BLACK,
                            onColorChoose = { color -> this@ColoringWithPaletteComposable.coloringComposable.color = color },
                            modifier = Modifier.constrainAs(palette) {
                                width = Dimension.fillToConstraints
                                height = Dimension.wrapContent

                                // top free
                                start.linkTo(parent.start, START_END_NORMAL)
                                end.linkTo(parent.end, START_END_NORMAL)
                                bottom.linkTo(parent.bottom, TOP_BOTTOM_NORMAL)
                            }
                           )
        }
    }
}