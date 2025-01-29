package fr.jhelp.tools.utilities.source

import android.net.Uri

/**
 * Source of a file stored in assets
 */
class SourceAsset(assetPath: String) : Source
{
    override val uri: Uri = Uri.parse("file:///android_asset/$assetPath")
}