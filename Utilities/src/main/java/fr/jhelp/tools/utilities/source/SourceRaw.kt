package fr.jhelp.tools.utilities.source

import android.content.Context
import android.net.Uri
import androidx.annotation.RawRes
import fr.jhelp.tools.utilities.injector.injected
import java.io.InputStream

/**
 * Source of a file stored in raw resources
 */
data class SourceRaw(@RawRes val resourceRaw: Int) : Source
{
    companion object
    {
        private val context: Context by injected<Context>()
        private val applicationId: String by lazy { SourceRaw.context.packageName }
        private val resources by lazy { SourceRaw.context.resources }
    }

    override val uri: Uri = Uri.parse("android.resource://${SourceRaw.applicationId}/${this.resourceRaw}")

    override fun inputStream(): InputStream =
        SourceRaw.resources.openRawResource(this.resourceRaw)
}