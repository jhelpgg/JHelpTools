package fr.jhelp.tools.video.capture

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import fr.jhelp.tools.utilities.defaultBitmap
import fr.jhelp.tools.utilities.extensions.int
import fr.jhelp.tools.utilities.extensions.long
import fr.jhelp.tools.utilities.injector.injected
import fr.jhelp.tools.utilities.source.Source
import fr.jhelp.tools.video.capture.size.ElementSize
import fr.jhelp.tools.video.capture.size.ElementSizeAsSource
import fr.jhelp.tools.video.capture.size.ElementSizeFix
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.max

/**
 * Represents a video capture frames.
 * @property source Video source
 */
class VideoCapture private constructor(val source: Source, width: ElementSize, height: ElementSize)
{
    companion object
    {
        /**
         * Creates a new video capture with desired width and height
         */
        fun create(source: Source, width: Int, height: Int): Deferred<VideoCapture> =
            CoroutineScope(Dispatchers.IO).async { VideoCapture(source, ElementSizeFix(max(32, width)), ElementSizeFix(max(32, height))) }

        /**
         * Creates a new video capture with size define by the video source
         */
        fun create(source: Source): Deferred<VideoCapture> =
            CoroutineScope(Dispatchers.IO).async { VideoCapture(source, ElementSizeAsSource, ElementSizeAsSource) }
    }

    private val closedStatus = AtomicBoolean(false)
    private val context: Context by injected<Context>()
    private val exoPlayer: ExoPlayer = this.createExoPlayer()
    private val mediaMetadataRetriever = this.createMediaMetadataRetriever(width, height)

    /**
     * Width of the video frame
     */
    var videoWidth: Int = 0
        private set

    /**
     * Height of the video frame
     */
    var videoHeight: Int = 0
        private set

    /**
     * Duration of the video in milliseconds
     */
    var videoDuration: Long = 0L
        private set

    /**
     * Whether the video capture is closed
     */
    val closed: Boolean get() = this.closedStatus.get()

    /**
     * Gets the video frame at the specified time
     */
    fun imageAtTime(timeMilliseconds: Long): Bitmap
    {
        if (this.closedStatus.get() || timeMilliseconds < 0L || timeMilliseconds > this.videoDuration)
        {
            return defaultBitmap
        }

        val bitmap = this.mediaMetadataRetriever.getFrameAtTime(timeMilliseconds * 1000L, MediaMetadataRetriever.OPTION_CLOSEST) ?: return defaultBitmap

        if (this.videoWidth != bitmap.width || this.videoHeight != bitmap.height || bitmap.config != Bitmap.Config.ARGB_8888)
        {
            val newBitmap =
                if (bitmap.config == Bitmap.Config.ARGB_8888)
                {
                    Bitmap.createScaledBitmap(bitmap, this.videoWidth, this.videoHeight, true)
                }
                else
                {
                    val intermediateBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
                    val result = Bitmap.createScaledBitmap(intermediateBitmap, this.videoWidth, this.videoHeight, true)
                    intermediateBitmap.recycle()
                    result
                }

            bitmap.recycle()
            return newBitmap
        }

        return bitmap
    }

    fun play()
    {
        MainScope().launch { this@VideoCapture.exoPlayer.play() }
    }

    fun stop()
    {
        MainScope().launch { this@VideoCapture.exoPlayer.stop() }
    }

    fun pause()
    {
        MainScope().launch { this@VideoCapture.exoPlayer.pause() }
    }

    /**
     * Closes the video capture
     */
    fun close()
    {
        if (this.closedStatus.compareAndSet(false, true))
        {
            MainScope().launch { this@VideoCapture.exoPlayer.release() }
            this.mediaMetadataRetriever.release()
        }
    }

    @Throws(IllegalArgumentException::class, SecurityException::class)
    private fun createMediaMetadataRetriever(width: ElementSize, height: ElementSize): MediaMetadataRetriever
    {
        val mediaMetadataRetriever = MediaMetadataRetriever()
        mediaMetadataRetriever.setDataSource(this.context, this.source.uri)

        this.videoWidth =
            if (width is ElementSizeFix)
            {
                width.size
            }
            else
            {
                mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH).int(32)
            }


        this.videoHeight =
            if (height is ElementSizeFix)
            {
                height.size
            }
            else
            {
                mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT).int(32)
            }

        this.videoDuration = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION).long()
        return mediaMetadataRetriever
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun createExoPlayer(): ExoPlayer
    {
        val exoPlayer = ExoPlayer.Builder(this.context).build()
        runBlocking {
            withContext(Dispatchers.Main) {
                exoPlayer.prepare()
                exoPlayer.setMediaItem(MediaItem.fromUri(this@VideoCapture.source.uri))
            }
        }
        return exoPlayer
    }
}