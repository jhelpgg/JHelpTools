package fr.jhelp.tools.utilities.source

import android.content.Context
import android.content.res.AssetManager
import android.net.Uri
import fr.jhelp.tools.utilities.injector.injected
import java.io.InputStream

/**
 * Source of a file stored in assets
 */
data class SourceAsset(val assetPath: String) : Source
{
    companion object
    {
        private val context: Context by injected<Context>()
        private val assetManager: AssetManager by lazy { SourceAsset.context.assets }
    }

    override val uri: Uri = Uri.parse("file:///android_asset/${this.assetPath}")

    override fun inputStream(): InputStream =
        SourceAsset.assetManager.open(this.assetPath)
}