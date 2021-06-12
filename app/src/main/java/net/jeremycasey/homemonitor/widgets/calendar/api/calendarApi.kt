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

data class Calendar (
  val accountName: String,
  val displayName: String,
)

private fun getCalendarList(context: Context): List<Calendar> {
  val calendars = mutableListOf<Calendar>()
  val calendarCursor: Cursor = context.getContentResolver()
    .query(
      Calendars.CONTENT_URI,
      arrayOf(Calendars.ACCOUNT_NAME, Calendars.CALENDAR_DISPLAY_NAME),
      null,
      null,
      null
    ) as Cursor
  while (calendarCursor.moveToNext()) {
    calendars.add(Calendar(
      accountName = calendarCursor.getString(0),
      displayName = calendarCursor.getString(1),
    ))
  }
  calendarCursor.close()

  // I'm not sure why, but some calendars were showing twice
  return calendars.distinctBy { "${it.accountName}|||${it.displayName}" }
}

fun getAllEvents(context: Context): List<CalendarEvent> {
  val calendars = getCalendarList(context)

  return calendars.flatMap {
    getEventsForCalendar(context, it)
  }
}

private fun getForcedEndDate(
  endTimeString: String?,
  isAllDay: Boolean,
  startDateTime: DateTime
): DateTime {
  if (endTimeString != null) {
    return DateTime(endTimeString.toLong())
  } else if (isAllDay) {
    return startDateTime.plusDays(1)
  }
  // Not sure if this is possible, but we'll account for it
  return startDateTime.plusHours(1)
}

private fun getEventsForCalendar(context: Context, calendar: Calendar): List<CalendarEvent> {
  val startMillis: Long = DateTime.now().millis
  val endMillis: Long = DateTime.now().plusDays(7).withTimeAtStartOfDay().millis

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
    arrayOf(calendar.accountName, calendar.displayName),
    "${Instances.BEGIN} ASC"
  ) as Cursor

  val events = mutableListOf<CalendarEvent>()

  while (cursor.moveToNext()) {
    val isAllDay = toBoolean(cursor.getInt(2))
    val startDateTime = DateTime(cursor.getString(4).toLong())
    val endTimeString = cursor.getStringOrNull(5)
    val endDateTime = getForcedEndDate(endTimeString, isAllDay, startDateTime)

    events.add(CalendarEvent(
      id = cursor.getString(0),
      title = cursor.getString(1),
      isAllDay = isAllDay,
      calendarColor = Color(cursor.getInt(3)),
      accountName = calendar.accountName,
      calendarName = calendar.displayName,
      startDateTime = startDateTime,
      endDateTime = endDateTime,
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
