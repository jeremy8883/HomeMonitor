package net.jeremycasey.homemonitor.utils

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Color
import androidx.core.graphics.set
import getLightColor
import net.jeremycasey.homemonitor.widgets.lights.Light

/**
 * As the lights change, we also change the background to match
 */
fun generateBackgroundImage(activity: Activity, lights: List<Light>): Bitmap {
  val size = getScreenSize(activity)
  val colors = getColorsFromLights(lights)
  val colorCount = colors.size

  val bitmap = Bitmap.createBitmap(size.width, size.height, Bitmap.Config.ARGB_8888)
  for (y in 0 until bitmap.height) {
    val index = Math.floor(y.toDouble() / (size.height.toDouble() / colorCount.toDouble())).toInt()
    val color = colors[index]
    for (x in 0 until bitmap.width) {
      bitmap.set(x, y, color)
    }
  }
  addGaussianBlur(bitmap)
  return bitmap
}

private fun getColorsFromLights(lights: List<Light>): List<Int> {
  if (lights.size == 0) {
    return listOf(Color.BLACK)
  }
  return lights.map { getLightColor(it) }
}
