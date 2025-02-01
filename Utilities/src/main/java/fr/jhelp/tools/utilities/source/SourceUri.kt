package fr.jhelp.tools.utilities.source

import android.content.Context
import android.net.Uri
import fr.jhelp.tools.utilities.injector.injected
import java.io.InputStream

/**
 * Source of a file with its Uri
 */
data class SourceUri(override val uri : Uri) : Source
{
    companion object
    {
        private val context by injected<Context>()
        private val contentResolver by lazy { SourceUri.context.contentResolver }
    }

    override fun inputStream(): InputStream =
        SourceUri.contentResolver.openInputStream(this.uri) ?: throw IllegalArgumentException("Can't open $this")
}
