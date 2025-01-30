package fr.jhelp.tools.video.capture

import androidx.media3.common.MediaItem
import fr.jhelp.tools.utilities.source.Source

data class Video(val videoId: String,
                 val source: Source)
{
    internal val mediaItem: MediaItem by lazy {
        MediaItem.Builder()
            .setMediaId(this.videoId)
            .setUri(this.source.uri)
            .build()
    }
}
