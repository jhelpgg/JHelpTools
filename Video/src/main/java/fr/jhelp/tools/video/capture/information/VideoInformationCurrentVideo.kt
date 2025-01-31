package fr.jhelp.tools.video.capture.information

import fr.jhelp.tools.utilities.source.Source

data class VideoInformationCurrentVideo(val source: Source,
                                        val width: Int, val height: Int,
                                        val duration: Long) : VideoInformation
