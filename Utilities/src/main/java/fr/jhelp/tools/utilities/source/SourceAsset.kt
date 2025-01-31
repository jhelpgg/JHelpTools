package fr.jhelp.tools.utilities.source

import android.content.Context
import android.content.res.AssetManager
import android.net.Uri
import fr.jhelp.tools.utilities.injector.injected

/**
 * Source of a file stored in assets
 */
class SourceAsset(assetPath: String) : Source
{
    override val uri: Uri = Uri.parse("file:///android_asset/$assetPath")
}