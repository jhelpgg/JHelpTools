package fr.jhelp.tools.ui.responsive

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

object ArrangementExpand : Arrangement.HorizontalOrVertical
{
    override fun Density.arrange(totalSize: Int, sizes: IntArray, layoutDirection: LayoutDirection, outPositions: IntArray)
    {
        if (layoutDirection == LayoutDirection.Ltr)
        {
            placeLeftOrTop(sizes, outPositions)
        }
        else
        {
            placeRightOrBottom(totalSize, sizes, outPositions)
        }
    }

    override fun Density.arrange(totalSize: Int, sizes: IntArray, outPositions: IntArray)
    {
        placeLeftOrTop(sizes, outPositions)
    }

    override fun toString(): String = "ArrangementExpand"

    private fun placeLeftOrTop(size: IntArray, outPosition: IntArray)
    {
        var current = 0

        for (index in size.indices)
        {
            outPosition[index] = current
            current += size[index]
        }
    }

    private fun placeRightOrBottom(totalSize: Int, size: IntArray, outPosition: IntArray)
    {
        val consumedSize = size.fold(0) { a, b -> a + b }
        var current = totalSize - consumedSize
        for (index in size.indices)
        {
            outPosition[index] = current
            current += size[index]
        }

        outPosition.reverse()
    }
}

val Arrangement.Expand: Arrangement.Horizontal get() = ArrangementExpand