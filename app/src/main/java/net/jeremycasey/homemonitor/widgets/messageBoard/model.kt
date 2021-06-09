package net.jeremycasey.homemonitor.widgets.messageBoard

import android.graphics.Bitmap

data class PostedItem (
  val code: String,
  val type: String,
  val message: String,
  val image: Bitmap
)
