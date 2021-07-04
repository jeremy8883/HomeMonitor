package net.jeremycasey.homemonitor.utils

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Color
import androidx.core.graphics.set
import getLightColor
import net.jeremycasey.homemonitor.widgets.lights.Light
import java.util.*

var imageCache = WeakHashMap<String, Bitmap>()

val emptyImage = createEmptyImage(Color.BLACK)

/**
 * As the lights change, we also change the background to match
 */
fun generateBackgroundImage(activity: Activity, lights: List<Light>): Bitmap {
  val size = getScreenSize(activity)
  val colors = getColorsFromLights(lights)
  val colorCount = colors.size

  if (colorCount == 0) {
    return emptyImage
  }

  val cacheId = getCacheId(colors)
  if (imageCache.containsKey(cacheId)) {
    return imageCache.get(cacheId) as Bitmap
  }

  val bitmap = Bitmap.createBitmap(size.width, size.height, Bitmap.Config.ARGB_8888)
  for (y in 0 until bitmap.height) {
    val index = Math.floor(y.toDouble() / (size.height.toDouble() / colorCount.toDouble())).toInt()
    val color = colors[index]
    for (x in 0 until bitmap.width) {
      bitmap.set(x, y, color)
    }
  }
  val blurredBitmap = addGaussianBlur(bitmap, 1f, 400)

  imageCache.set(cacheId, blurredBitmap)

  return blurredBitmap
}

private fun getColorsFromLights(lights: List<Light>): List<Int> {
  return lights
    // This just happened to match how my lamp was set up. Not sure how best to sort this.
    .sortedByDescending { it.id }
    .map { getLightColor(it) }
}

private fun getCacheId(colors: List<Int>): String {
  return colors.joinToString("|")
}
