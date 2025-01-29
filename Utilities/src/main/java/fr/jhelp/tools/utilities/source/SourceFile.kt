package fr.jhelp.tools.utilities.source

import android.net.Uri
import java.io.File

/**
 * Source of a file with its File
 */
class SourceFile(file:File) : Source
{
    override val uri: Uri = Uri.fromFile(file)
}