package net.jeremycasey.homemonitor.widgets.calendar

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import net.jeremycasey.homemonitor.composables.*
import net.jeremycasey.homemonitor.ui.theme.HomeMonitorTheme
import net.jeremycasey.homemonitor.utils.minutesToMs
import net.jeremycasey.homemonitor.utils.openApp
import net.jeremycasey.homemonitor.widgets.calendar.api.getAllEvents
import org.joda.time.DateTime

val mockTodaysEvents: List<CalendarEvent> = listOf(
  CalendarEvent(
    id="61",
    calendarName="buckinghamtablet@gmail.com",
    accountName="buckinghamtablet@gmail.com",
    title="Event today",
    isAllDay=false,
    calendarColor=Color.Blue,
    startDateTime=DateTime("2021-06-04T14:30:00.000+10:00"),
    endDateTime=DateTime("2021-06-04T15:30:00.000+10:00"),
  ),
  CalendarEvent(
    id="59",
    calendarName="buckinghamtablet@gmail.com",
    accountName="buckinghamtablet@gmail.com",
    title="Event tomorrow",
    isAllDay=false,
    calendarColor=Color.Blue,
    startDateTime=DateTime("2021-06-05T12:00:00.000+10:00"),
    endDateTime=DateTime("2021-06-05T13:00:00.000+10:00"),
  ),
  CalendarEvent(
    id="60",
    calendarName="buckinghamtablet@gmail.com",
    accountName="buckinghamtablet@gmail.com",
    title="Event full day",
    isAllDay=true,
    calendarColor=Color.Blue,
    startDateTime=DateTime("2021-06-05T10:00:00.000+10:00"),
    endDateTime=DateTime("2021-06-06T10:00:00.000+10:00"),
  ),
)

class CalendarWidgetViewModelFactory(context: Context) :
  ViewModelProvider.Factory {

  private val _context: Context

  init {
    _context = context
  }

  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return CalendarWidgetViewModel(_context) as T
  }
}

class CalendarWidgetViewModel(context: Context) : ViewModel() {
  private val _upcomingEvents = MutableLiveData<List<CalendarEvent>?>(null)
  val upcomingEvents: LiveData<List<CalendarEvent>?> = _upcomingEvents

  private val _context = context

  private val _fetchError = MutableLiveData<Exception?>(null)
  val fetchError: LiveData<Exception?> = _fetchError

  fun onCalendarRequired() {
    _fetchError.value = null
    _upcomingEvents.value = getAllEvents(_context)
  }

  fun onCalendarTouch() {
    openApp(_context, "com.google.android.calendar")
  }
}

@Composable
fun CalendarWidget(viewModel: CalendarWidgetViewModel) {
  val currentCalendar by viewModel.upcomingEvents.observeAsState()
  val fetchError by viewModel.fetchError.observeAsState()

  PollEffect("", minutesToMs(5)) {
    viewModel.onCalendarRequired()
  }
  WithCurrentTime { now ->
    CalendarWidgetView(
      now,
      currentCalendar,
      fetchError,
      { viewModel.onCalendarRequired() },
      { viewModel.onCalendarTouch() }
    )
  }
}

private fun formatTime(event: CalendarEvent, now: DateTime): String {
  if (event.isAllDay) {
    return "All day"
  } else if (event.startDateTime < now) {
    if (event.endDateTime.toDate() != now.toDate()) {
      return "Until ${event.endDateTime.toString("d MMM HH:mm")}"
    }
    return "Until ${event.endDateTime.toString("HH:mm")}"
  }

  return event.startDateTime.toString("HH:mm")
}

private fun groupEventsByDay(events: List<CalendarEvent>): List<GroupedEvent> {
  val grouped = events.groupBy {
    it.startDateTime.toLocalDate().toString("yyyy-MM-dd")
  }

  return grouped.map {
    GroupedEvent(
      date = DateTime(it.key),
      events = it.value
        .sortedBy { it.endDateTime.millis }
        .sortedBy { if (it.isAllDay) {
          it.startDateTime.withTimeAtStartOfDay().millis
        } else {
          it.startDateTime.millis
        } }
    )
  }
}

private fun getGroupName(date: DateTime, now: DateTime): String {
  if (date.toLocalDate() == now.toLocalDate()) {
    return "Today"
  } else if (date.toLocalDate() == now.plusDays(1).toLocalDate()) {
    return "Tomorrow"
  } else {
    return date.toString("d MMM")
  }
}

@Composable
fun CalendarWidgetView(
  now: DateTime,
  upcomingEvents: List<CalendarEvent>?,
  fetchError: Exception?,
  onRetryClick: () -> Any,
  onClick: () -> Unit,
) {
  if (fetchError != null) {
    WidgetCard(onClick = onClick) {
      ErrorPanel(fetchError, onRetryClick)
    }
    return
  }
  if (upcomingEvents == null) {
    WidgetCard(onClick = onClick) {
      LoadingPanel()
    }
    return
  }
  if (upcomingEvents.isEmpty()) {
    WidgetCard(onClick = onClick) {
      Text("No upcoming events")
    }
    return
  }

  val groupedEvents = groupEventsByDay(upcomingEvents)

  WidgetCard(onClick = onClick, scrollable = Scrollable.vertical) {
    groupedEvents.forEach { group ->
      Text(getGroupName(group.date, now), style = MaterialTheme.typography.h6)
      group.events.forEach { event ->
        Row(modifier = Modifier.padding(0.dp, 5.dp)) {
          Box(
            modifier = Modifier
              .width(20.dp)
              .height(30.dp)
              .background(event.calendarColor)
          )
          Column {
            Text(
              event.title,
              modifier = Modifier.padding(10.dp, 0.dp, 0.dp, 0.dp)
            )
            Text(
              formatTime(event, now),
              modifier = Modifier.padding(10.dp, 0.dp, 0.dp, 0.dp)
            )
          }
        }
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
  HomeMonitorTheme {
    CalendarWidgetView(DateTime("2021-06-04T14:20:00+10:00"), mockTodaysEvents, null, {}, {})
  }
}

@Preview(showBackground = true)
@Composable
fun LoadingPreview() {
  HomeMonitorTheme {
    CalendarWidgetView(DateTime("2021-06-04T14:20:00+10:00"), null, null, {}, {})
  }
}

@Preview(showBackground = true)
@Composable
fun ErrorPreview() {
  HomeMonitorTheme {
    CalendarWidgetView(DateTime("2021-06-04T14:20:00+10:00"), null, Exception("This is an error"), { }, {})
  }
}
