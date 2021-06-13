package net.jeremycasey.homemonitor.utils

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.core.graphics.set
import net.jeremycasey.homemonitor.widgets.lights.Light

private fun blurBitmap() {

}

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
  return bitmap
}

private fun getColorsFromLights(lights: List<Light>): List<Int> {
  if (lights.size == 0) {
    return listOf(Color.BLACK)
  }

  return lights.map { light ->
    val hue = light.state.hue
    val sat = light.state.sat
    val bri = light.state.bri
    if (hue == null || sat == null) {
      Color.argb(1, bri, bri, bri)
    } else {
//      3280, 252, 254
//      53522, 253, 254
//      14655, 252, 254
      println("${hue}, ${sat}, ${bri}") // TODO fix
      Color.HSVToColor(floatArrayOf(hue.toFloat(), sat.toFloat(), bri.toFloat()))
    }
  }
}