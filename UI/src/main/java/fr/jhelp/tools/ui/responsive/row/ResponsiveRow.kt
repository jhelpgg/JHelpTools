package fr.jhelp.tools.ui.responsive.row

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import fr.jhelp.tools.ui.responsive.Expand

/**
 * Row that act in responsive way.
 *
 * If possible it will place components horizontally in same line.
 *
 * If their no enough room in the parent's width, they will be placed in different lines.
 * The reparations of number component per line is equilibrate to keep harmony as best as possible.
 *
 * The spaces applied on each lines depends on the [horizontalArrangement].
 *
 * The vertical position of each component is aligned with [verticalAlignment] per each lines.
 *
 * For [horizontalArrangement] it can be used the special [Arrangement.Expand] that will expand the elements.
 * The algorithm will do is best to make all elements the largest is possible to have an harmony width.
 *
 * Example:
 * ```kotlin
 *            ResponsiveRow(horizontalArrangement = Arrangement.Expand,
 *                           verticalAlignment = Alignment.CenterVertically,
 *                           modifier = Modifier.fillMaxWidth()) {
 *                 Text("Short", modifier = Modifier.background(LIGHT_RED))
 *                 Text("Normal text", modifier = Modifier.background(LIGHT_GREEN))
 *                 Text("This is a long text\nSecond line", modifier = Modifier.background(LIGHT_BLUE))
 *                 Text("A text again", modifier = Modifier.background(LIGHT_GRAY))
 *                 Text("Text, text and more", modifier = Modifier.background(YELLOW))
 *                 Text("Last text", modifier = Modifier.background(LIGHT_RED))
 *             }
 * ```
 */
@Composable
fun ResponsiveRow(modifier: Modifier = Modifier,
                  horizontalArrangement: Arrangement.Horizontal = Arrangement.Expand,
                  verticalAlignment: Alignment.Vertical = Alignment.Top,
                  verticalSpace: Dp = 0.dp,
                  content: @Composable () -> Unit)
{
    Layout(modifier = modifier, content = content)
    { measurableList, constraints ->
        val rowInResponsiveRow = RowInResponsiveRow()
        val width = constraints.maxWidth
        val height = constraints.maxHeight
        val placeableInfoList = ArrayList<PlaceableInfo>()

        for (measurable in measurableList)
        {
            val itemWidth = try
            {
                measurable.maxIntrinsicWidth(width)
            }
            catch (_: Exception)
            {
                width
            }

            val itemHeight = try
            {
                measurable.maxIntrinsicHeight(height)
            }
            catch (_: Exception)
            {
                height
            }

            val placeableInfo = PlaceableInfo(itemWidth, itemHeight)
            rowInResponsiveRow.add(placeableInfo)
            placeableInfoList.add(placeableInfo)
        }

        val rows = adjustRowsOfResponsiveRow(rowInResponsiveRow, width, horizontalArrangement == Arrangement.Expand)

        for ((index, measurable) in measurableList.withIndex())
        {
            val row = placeableInfoList[index]
            val itemConstraints = constraints.copy(
                minWidth = row.width, minHeight = row.height,
                maxWidth = row.width, maxHeight = row.height
                                                  )
            row.placeable = measurable.measure(itemConstraints)
        }

        val verticalSeparator = verticalSpace.toPx().toInt()

        layout(width, rows.sumOf { row -> row.height } + verticalSeparator * (rows.size - 1)) {
            var y = 0
            val isRTL = layoutDirection == LayoutDirection.Rtl

            for (row in rows)
            {
                val freeSpace = width - row.width
                val space = spaceMeasured(freeSpace, row.size, horizontalArrangement, isRTL)
                var x = if (isRTL) width - space.after else space.before
                val sign = if (isRTL) -1 else 1

                for (element in row.elements)
                {
                    val placeable = element.placeable ?: continue
                    val xx = if (isRTL) x - placeable.width else x
                    placeable.place(xx, y + deltaY(placeable.height, row.height, verticalAlignment))
                    x += sign * (placeable.width + space.between)
                }

                y += row.height + verticalSeparator
            }
        }
    }
}