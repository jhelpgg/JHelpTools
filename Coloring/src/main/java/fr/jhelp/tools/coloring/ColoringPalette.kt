package fr.jhelp.tools.coloring

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.jhelp.tools.ui.ColorButton
import fr.jhelp.tools.ui.responsive.row.ResponsiveRow
import fr.jhelp.tools.utilities.math.COLOR_AMBER_0500
import fr.jhelp.tools.utilities.math.COLOR_BLUE_0500
import fr.jhelp.tools.utilities.math.COLOR_BLUE_GREY_0500
import fr.jhelp.tools.utilities.math.COLOR_BROWN_0500
import fr.jhelp.tools.utilities.math.COLOR_CYAN_0500
import fr.jhelp.tools.utilities.math.COLOR_DEEP_ORANGE_0500
import fr.jhelp.tools.utilities.math.COLOR_DEEP_PURPLE_0500
import fr.jhelp.tools.utilities.math.COLOR_GREEN_0500
import fr.jhelp.tools.utilities.math.COLOR_GREY_0500
import fr.jhelp.tools.utilities.math.COLOR_INDIGO_0500
import fr.jhelp.tools.utilities.math.COLOR_LIGHT_BLUE_0500
import fr.jhelp.tools.utilities.math.COLOR_LIGHT_GREEN_0500
import fr.jhelp.tools.utilities.math.COLOR_LIME_0500
import fr.jhelp.tools.utilities.math.COLOR_ORANGE_0500
import fr.jhelp.tools.utilities.math.COLOR_PINK_0500
import fr.jhelp.tools.utilities.math.COLOR_PURPLE_0500
import fr.jhelp.tools.utilities.math.COLOR_RED_0500
import fr.jhelp.tools.utilities.math.COLOR_TEAL_0500
import fr.jhelp.tools.utilities.math.COLOR_WHITE
import fr.jhelp.tools.utilities.math.COLOR_YELLOW_0500
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

val colors = intArrayOf(COLOR_BLUE_0500, COLOR_DEEP_ORANGE_0500, COLOR_DEEP_PURPLE_0500, COLOR_GREEN_0500, COLOR_GREY_0500, COLOR_INDIGO_0500, COLOR_LIGHT_BLUE_0500, COLOR_LIGHT_GREEN_0500, COLOR_LIME_0500, COLOR_ORANGE_0500, COLOR_PINK_0500, COLOR_PURPLE_0500, COLOR_RED_0500, COLOR_TEAL_0500, COLOR_YELLOW_0500, COLOR_BROWN_0500, COLOR_CYAN_0500, COLOR_AMBER_0500, COLOR_BLUE_GREY_0500)

@Composable
fun ColoringPalette(selectColor: Int = COLOR_WHITE,
                    colorsChoice: IntArray = colors,
                    modifier: Modifier = Modifier,
                    onColorChoose: (Int) -> Unit = {})
{
    val choose: (Int) -> Unit = { choice -> CoroutineScope(Dispatchers.Default).launch { onColorChoose(choice) } }
    var selectedIndex: Int by remember { mutableIntStateOf(0) }
    val buttonClick: (Int) -> Unit = { colorIndex ->
        selectedIndex = colorIndex
        choose(colorsChoice[colorIndex])
    }

    ResponsiveRow(horizontalArrangement = Arrangement.SpaceEvenly, modifier = modifier)
    {
        for ((index, color) in colorsChoice.withIndex())
        {
            ColorButton(color = color, selectColor = selectColor,
                        selected = selectedIndex == index, onClick = { buttonClick(index) },
                        modifier = Modifier.size(32.dp, 32.dp).padding(all=1.dp))
        }
    }

    LaunchedEffect(Unit) { choose(colorsChoice[selectedIndex]) }
}