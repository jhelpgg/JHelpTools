package fr.jhelp.tools.ui.responsive.row

import kotlin.math.max

internal class RowInResponsiveRow()
{
    val elements = ArrayList<PlaceableInfo>()
    val size: Int get() = this.elements.size
    var width = 0
    var height = 0

    fun add(placeable: PlaceableInfo)
    {
        this.elements.add(placeable)
        this.width += placeable.width
        this.height = max(this.height, placeable.height)
    }

    override fun toString(): String =
        "${this.width}x${this.height} : ${this.elements}"
}