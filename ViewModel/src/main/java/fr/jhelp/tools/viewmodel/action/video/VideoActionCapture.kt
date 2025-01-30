package fr.jhelp.tools.viewmodel.action.video

import android.graphics.Bitmap

class VideoActionCapture(val capture:(image:Bitmap) -> Unit) : VideoAction
