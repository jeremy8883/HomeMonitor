import android.graphics.Color
import net.jeremycasey.homemonitor.widgets.lights.Light

private fun max(a: Double, b: Double, c: Double): Double {
  return Math.max(a, Math.max(b, c))
}

// Taken from: https://stackoverflow.com/a/22918909
private fun xyBriToRgb(x: Double, y: Double, bri: Int): Int {
  val z: Double = 1.0 - x - y;
  val Y: Double = bri.toDouble() / 255.0; // Brightness of lamp
  val X: Double = (Y / y) * x;
  val Z: Double = (Y / y) * z;
  var r: Double = X * 1.612 - Y * 0.203 - Z * 0.302;
  var g: Double = -X * 0.509 + Y * 1.412 + Z * 0.066;
  var b: Double = X * 0.026 - Y * 0.072 + Z * 0.962;
  if (r <= 0.0031308) {
    r *= 12.92
  } else {
    r = (1.0+0.055) * Math.pow(r, (1.0 / 2.4))-0.055
  }
  if (g <= 0.0031308) {
    g *= 12.92
  } else {
    g = (1.0+0.055) * Math.pow(g, (1.0 / 2.4))-0.055
  }
  if (b <= 0.0031308) {
    b *= 12.92
  } else {
    b = (1.0+0.055) * Math.pow(b, (1.0 / 2.4))-0.055
  }
  val maxValue = max(r, g, b);
  r /= maxValue;
  g /= maxValue;
  b /= maxValue;
  r *= 255;
  if (r < 0) { r = 255.0 };
  g *= 255;
  if (g < 0) { g = 255.0 };
  b *= 255;
  if (b < 0) { b = 255.0 };

  return Color.rgb(r.toInt(), g.toInt(), b.toInt())
}

fun getLightColor(light: Light): Int {
  val xy = light.state.xy
  val bri = light.state.bri
  if (xy == null) {
    return Color.argb(1, bri, bri, bri)
  } else {
    return xyBriToRgb(xy[0], xy[1], bri)
  }
}
