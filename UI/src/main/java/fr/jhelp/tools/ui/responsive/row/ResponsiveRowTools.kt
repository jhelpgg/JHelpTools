package fr.jhelp.tools.ui.responsive.row

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import kotlin.math.max

/**
 * Adjust the number of lines and number element per lines depends on the [maxWidth].
 *
 * Elements can be expand at request.
 *
 * @param initialRow Elements to adjust if need
 * @param maxWidth Max width of the row
 * @param isExpand If true, elements have to be expand
 * @return List of adjusted rows
 */
internal fun adjustRowsOfResponsiveRow(initialRow: RowInResponsiveRow, maxWidth: Int, isExpand: Boolean): List<RowInResponsiveRow>
{
    var width = initialRow.width
    val rows = ArrayList<RowInResponsiveRow>()
    rows.add(initialRow)
    var divide = 1
    var modulo = 0

    while ((width > maxWidth || modulo > 0) && divide < initialRow.size)
    {
        width = 0
        divide++
        rows.clear()
        val size = initialRow.size / divide
        modulo = initialRow.size % divide
        var index = 0

        for (count in 0 until divide)
        {
            val row = RowInResponsiveRow()

            for (number in 0 until size)
            {
                row.add(initialRow.elements[index])
                index++
            }

            if (modulo > 0 && index < initialRow.size && row.width + initialRow.elements[index].width <= maxWidth)
            {
                row.add(initialRow.elements[index])
                index++
                modulo--
            }

            rows.add(row)
            width = max(width, row.width)
        }
    }

    if (isExpand)
    {
        for (row in rows)
        {
            var totalWidth = row.elements.sumOf { element -> element.width }

            while (totalWidth < maxWidth)
            {
                val minimum = row.elements.minOf { element -> element.width }

                for (element in row.elements)
                {
                    if (element.width == minimum)
                    {
                        element.width++
                        totalWidth++

                        if (totalWidth >= maxWidth)
                        {
                            break
                        }
                    }
                }
            }

            row.width = maxWidth
        }
    }

    return rows
}

/**
 * Measure number of pixels to add before and after a row of elements.
 *
 * It also computes the space to put between elements.
 *
 * @param freeSpace Space available to put elements
 * @param numberComponent Number of component in the row
 * @param horizontal Arrangement of the horizontal way
 * @param isRtl If true, the row is in right to left
 * @return Space to add before and after the row. And space between elements
 */
internal fun spaceMeasured(freeSpace: Int, numberComponent: Int, horizontal: Arrangement.Horizontal, isRtl: Boolean): SpaceMeasured =
    when (horizontal)
    {
        Arrangement.Start        ->
            if (isRtl) SpaceMeasured(freeSpace, 0, 0)
            else SpaceMeasured(0, 0, freeSpace)

        Arrangement.End          ->
            if (isRtl) SpaceMeasured(0, 0, freeSpace)
            else SpaceMeasured(freeSpace, 0, 0)

        Arrangement.Center       ->
        {
            val before = freeSpace / 2
            SpaceMeasured(before, 0, freeSpace - before)
        }

        Arrangement.SpaceBetween ->
        {
            if (numberComponent <= 1)
            {
                SpaceMeasured(freeSpace / 2, 0, freeSpace - freeSpace / 2)
            }
            else
            {
                val between = freeSpace / (numberComponent - 1)
                val left = freeSpace % (numberComponent - 1)
                val before = left / 2
                SpaceMeasured(before, between, left - before)
            }
        }

        Arrangement.SpaceAround  ->
        {
            val between = freeSpace / numberComponent
            val left = between + freeSpace % numberComponent
            val before = left / 2
            SpaceMeasured(before, between, left - before)
        }

        Arrangement.SpaceEvenly  ->
        {
            val between = freeSpace / (numberComponent + 1)
            val left = freeSpace % (numberComponent + 1)
            val before = left / 2
            SpaceMeasured(between + before, between, between + left - before)
        }

        else                     ->
            SpaceMeasured(0, 0, 0)
    }

/**
 * Compute the vertical translation to put the component in the right place.
 * @param componentHeight Height of the component
 * @param maxHeight Max height of the row
 * @param vertical Arrangement of the vertical way
 * @return Vertical translation to apply
 */
internal fun deltaY(componentHeight: Int, maxHeight: Int, vertical: Alignment.Vertical): Int =
    when (vertical)
    {
        Alignment.Top              -> 0
        Alignment.CenterVertically -> (maxHeight - componentHeight) / 2
        Alignment.Bottom           -> maxHeight - componentHeight
        else                       -> 0
    }