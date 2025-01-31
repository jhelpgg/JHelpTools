package fr.jhelp.tools.utilities

import android.graphics.Bitmap

val defaultBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)

fun defaultBitmapBlackWhite(): Bitmap
{
    val bitmap = Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888)
    val pixels = IntArray(4)
    pixels[0] = 0xFFFFFFFF.toInt()
    pixels[1] = 0xFF000000.toInt()
    pixels[2] = 0xFF000000.toInt()
    pixels[3] = 0xFFFFFFFF.toInt()
    bitmap.setPixels(pixels, 0, 2, 0, 0, 2, 2)
    return bitmap
}
