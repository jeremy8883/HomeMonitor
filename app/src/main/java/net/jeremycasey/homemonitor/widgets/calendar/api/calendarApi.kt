package net.jeremycasey.homemonitor.widgets.calendar.api

import android.database.Cursor
import android.provider.CalendarContract.Instances

import android.provider.CalendarContract.Calendars

import android.content.ContentUris
import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.core.database.getStringOrNull
import net.jeremycasey.homemonitor.widgets.calendar.CalendarEvent
import org.joda.time.DateTime

fun getAllEvents(context: Context): List<CalendarEvent> {
  var events: List<CalendarEvent> = listOf()

  val calendarCursor: Cursor = context.getContentResolver()
    .query(
      Calendars.CONTENT_URI,
      arrayOf(Calendars.ACCOUNT_NAME, Calendars.CALENDAR_DISPLAY_NAME),
      null,
      null,
      null
    ) as Cursor
  while (calendarCursor.moveToNext()) {
    val accountName = calendarCursor.getString(0)
    val displayName = calendarCursor.getString(1)
    events = events + getEventsForCalendar(context, accountName, displayName)
  }
  calendarCursor.close()
  return events
}

private fun getEventsForCalendar(context: Context, accountName: String, calendarDisplayName: String): List<CalendarEvent> {
  val startMillis: Long = DateTime.now().millis
  val endMillis: Long = DateTime.now().plusDays(2).withTimeAtStartOfDay().millis

  val builder = Instances.CONTENT_URI.buildUpon()
  ContentUris.appendId(builder, startMillis)
  ContentUris.appendId(builder, endMillis)
  val cursor: Cursor = context.getContentResolver().query(
    builder.build(),
    // Select
    arrayOf(
      Instances._ID,
      Instances.TITLE,
      Instances.ALL_DAY,
      Instances.CALENDAR_COLOR,
      Instances.BEGIN,
      Instances.END,
    ),
    // I couldn't find any Calendar ID!? As long as we don't have two calendars of the same, name, this is fine.
    "(${Calendars.ACCOUNT_NAME} = ? AND ${Calendars.CALENDAR_DISPLAY_NAME} = ?)",
    arrayOf(accountName, calendarDisplayName),
    "${Instances.BEGIN} ASC"
  ) as Cursor

  val events = mutableListOf<CalendarEvent>()

  while (cursor.moveToNext()) {
    val isAllDay = toBoolean(cursor.getInt(2))
    val startDateTime = DateTime(cursor.getString(4).toLong())
    val endTimeString = cursor.getStringOrNull(5)
    var endDateTime: DateTime? = null
    if (endTimeString != null) {
      endDateTime = DateTime(endTimeString.toLong())
    } else if (endDateTime == null && isAllDay) {
      endDateTime = startDateTime.plusDays(1)
    } else {
      // Not sure if this is possible, but we'll account for it
      endDateTime = startDateTime.plusHours(1)
    }

    events.add(CalendarEvent(
      id = cursor.getString(0),
      title = cursor.getString(1),
      isAllDay = isAllDay,
      calendarColor = Color(cursor.getInt(3)),
      accountName = accountName,
      calendarName = calendarDisplayName,
      startDateTime = startDateTime,
      endDateTime = endDateTime!!,
    ))
  }

  return events
}

fun toBoolean(int: Int): Boolean {
  if (int == 1) {
    return true
  }
  return false
}