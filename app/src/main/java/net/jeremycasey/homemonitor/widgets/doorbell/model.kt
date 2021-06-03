package net.jeremycasey.homemonitor.widgets.doorbell

import android.graphics.Bitmap
import org.joda.time.DateTime

data class DoorbellEvent (
  val eventType: String,
  val picture: Bitmap,
  val title: String,
  val dateTime: DateTime
)
