package fr.jhelp.tools.utilities.source

import android.net.Uri
import java.io.InputStream

/**
 * Source of a file
 */
sealed interface Source
{
    val uri : Uri

    fun inputStream() : InputStream
}