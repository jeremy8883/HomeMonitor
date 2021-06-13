package net.jeremycasey.homemonitor.utils

import android.graphics.Bitmap
import android.graphics.Color
import androidx.core.graphics.set

private fun getAverageColor(bitmap: Bitmap, size: Int, x: Int, y: Int): Int {
  val yStart = Math.max(y - (size / 2), 0)
  val yEnd = Math.min(yStart + size, bitmap.height)

  val xStart = Math.max(x - (size / 2), 0)
  val xEnd = Math.min(xStart + size, bitmap.width)

  var sumR: Long = 0
  var sumG: Long = 0
  var sumB: Long = 0
  val pixelCount: Long = ((xEnd - xStart) * (yEnd - yStart)).toLong()
  for (y2 in yStart until yEnd) {
    for (x2 in xStart until xEnd) {
      val c = bitmap.getPixel(x2, y2)
      sumR += Color.red(c)
      sumG += Color.green(c)
      sumB += Color.blue(c)
    }
  }
  return Color.rgb(
    (sumR / pixelCount).toInt(),
    (sumG / pixelCount).toInt(),
    (sumB / pixelCount).toInt(),
  )
}

fun addGaussianBlur(bitmap: Bitmap, size: Int = 4) {
  for (y in 0 until bitmap.height) {
    for (x in 0 until bitmap.width) {
      bitmap.set(x, y, getAverageColor(bitmap, size, x, y))
    }
  }
}
