package fr.jhelp.tools.ui.responsive.row

import androidx.compose.ui.layout.Placeable

internal class PlaceableInfo(var width: Int, var height: Int, var placeable: Placeable? = null)
{
    override fun toString(): String =
        "${this.width}x${this.height} : ${this.placeable}"
}
