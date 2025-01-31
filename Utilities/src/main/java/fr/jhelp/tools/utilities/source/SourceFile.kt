package fr.jhelp.tools.utilities.source

import android.net.Uri
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

/**
 * Source of a file with its File
 */
data class SourceFile(val file: File) : Source
{
    override val uri: Uri = Uri.fromFile(this.file)

    override fun inputStream(): InputStream =
        FileInputStream(this.file)
}