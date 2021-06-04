package net.jeremycasey.homemonitor.widgets.calendar

import androidx.compose.ui.graphics.Color
import org.joda.time.DateTime

data class CalendarEvent(
  val id: String,
  val calendarName: String,
  val accountName: String,
  val title: String,
  val isAllDay: Boolean,
  val calendarColor: Color,
  val startDateTime: DateTime,
  val endDateTime: DateTime,
)