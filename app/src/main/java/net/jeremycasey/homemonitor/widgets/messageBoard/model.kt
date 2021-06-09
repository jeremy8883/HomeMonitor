package net.jeremycasey.homemonitor.widgets.messageBoard

import android.graphics.Bitmap

data class MessageItemConfig (
  val code: String,
  val type: String,
  val message: String,
  val imageResource: Int
)

data class MessageItem (
  val code: String,
  val type: String,
  val message: String,
  val image: Bitmap
)
