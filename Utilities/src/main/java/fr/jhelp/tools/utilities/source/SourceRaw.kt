package fr.jhelp.tools.utilities.source

import android.content.Context
import android.net.Uri
import androidx.annotation.RawRes
import fr.jhelp.tools.utilities.injector.injected

/**
 * Source of a file stored in raw resources
 */
class SourceRaw(@RawRes resourceRaw: Int) : Source
{
    companion object
    {
        private val context: Context by injected<Context>()
        private val applicationId: String by lazy { SourceRaw.context.packageName }
    }

    override val uri: Uri = Uri.parse("android.resource://${SourceRaw.applicationId}/$resourceRaw")
}