package fr.jhelp.tools.utilities.source

import android.net.Uri
import java.io.InputStream
import java.net.URL

/**
 * Source of a file with its URL
 */
data class SourceURL(val url: URL) : Source
{
    override val uri: Uri = Uri.parse(this.url.toString())

    override fun inputStream(): InputStream =
        this.url.openStream()
}
