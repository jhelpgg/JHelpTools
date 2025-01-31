package fr.jhelp.tools.utilities.source

import android.net.Uri
import java.net.URL

/**
 * Source of a file with its URL
 */
class SourceURL(url: URL) : Source
{
    override val uri: Uri = Uri.parse(url.toString())
}
