package net.jeremycasey.homemonitor.widgets.doorbell

import android.graphics.Bitmap

data class DoorbellEvent (
  val eventType: String,
  val picture: Bitmap,
  val title: String,
)
