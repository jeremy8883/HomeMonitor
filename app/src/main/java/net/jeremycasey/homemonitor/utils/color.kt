package net.jeremycasey.homemonitor.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils

fun getReadableTextColor(backgroundColor: Color): Color {
  val hsl = FloatArray(3)
  ColorUtils.colorToHSL(backgroundColor.toArgb(), hsl)
  val l = hsl[2]
  return if (l < 50) Color.White else Color.Black
}
