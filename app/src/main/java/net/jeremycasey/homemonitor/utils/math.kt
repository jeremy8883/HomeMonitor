package net.jeremycasey.homemonitor.utils

fun round(value: Double, decimalPlaces: Int): Double {
  val multiplicator = Math.pow(10.0, decimalPlaces.toDouble());

  return Math.round(value * multiplicator) / multiplicator;
}
