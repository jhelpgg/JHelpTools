package fr.jhelp.tools.utilities.source

import android.net.Uri

/**
 * Source of a file
 */
sealed interface Source
{
    val uri : Uri
}