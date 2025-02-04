package fr.jhelp.tools.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import fr.jhelp.tools.utilities.math.COLOR_BLACK

@Composable
fun ColorButton(color: Int,
                selectColor: Int = COLOR_BLACK,
                selected: Boolean = false,
                modifier: Modifier = Modifier,
                onClick: () -> Unit = {})
{
    Canvas(modifier = modifier.clickable { onClick() },
           onDraw = {
               drawOval(color = Color(color), style = Fill)

               if (selected)
               {
                   drawOval(color = Color(selectColor), style = Stroke(width = 13f))
                   drawOval(color = Color(0xFF_00_00_00.toInt() or (selectColor xor 0xFF_FF_FF_FF.toInt())), style = Stroke(width = 5f))
               }
           })
}