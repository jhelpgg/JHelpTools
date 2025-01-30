package fr.jhelp.tools.video.capture.configuration

import android.graphics.Bitmap

data class VideoCaptureConfigurationFix(val width: Int,
                                        val height: Int,
                                        val bitmapConfig: Bitmap.Config = Bitmap.Config.ARGB_8888) : VideoCaptureConfiguration
