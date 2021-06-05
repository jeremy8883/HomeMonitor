package net.jeremycasey.homemonitor.widgets.ptv

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import net.jeremycasey.homemonitor.composables.PollEffect
import net.jeremycasey.homemonitor.composables.Scrollable
import net.jeremycasey.homemonitor.ui.theme.HomeMonitorTheme
import net.jeremycasey.homemonitor.composables.WidgetCard
import net.jeremycasey.homemonitor.composables.WithCurrentTime
import net.jeremycasey.homemonitor.private.ptvWatchedStops
import net.jeremycasey.homemonitor.utils.getReadableTextColor
import net.jeremycasey.homemonitor.utils.getTimeRemaining
import net.jeremycasey.homemonitor.utils.minutesToMs
import net.jeremycasey.homemonitor.widgets.ptv.api.fetchDepartures
import net.jeremycasey.homemonitor.widgets.ptv.api.fetchDirectionsForRoute
import net.jeremycasey.homemonitor.widgets.ptv.api.fetchRoute
import net.jeremycasey.homemonitor.widgets.ptv.api.fetchStop
import org.joda.time.DateTime

val mockCurrentPtv = null

class PtvWidgetViewModelFactory(context: Context) :
  ViewModelProvider.Factory {

  private val _context: Context

  init {
    _context = context
  }

  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return PtvWidgetViewModel(_context) as T
  }
}

private fun getDepartureId(watchedStop: WatchedStop): String {
  return "${watchedStop.routeId}|${watchedStop.stopId}|${watchedStop.directionId}"
}

class PtvWidgetViewModel(context: Context) : ViewModel() {
  private val _stops = MutableLiveData<Map<Int, Stop>>(mapOf())
  val stops: LiveData<Map<Int, Stop>> = _stops

  private val _routes = MutableLiveData<Map<Int, Route>>(mapOf())
  val routes: LiveData<Map<Int, Route>> = _routes

  private val _directions = MutableLiveData<Map<Int, Direction>>(mapOf())
  val directions: LiveData<Map<Int, Direction>> = _directions

  private val _departures = MutableLiveData<Map<String, List<Departure>>>(mapOf())
  val departures: LiveData<Map<String, List<Departure>>> = _departures

  private val _context = context

  // TODO
//  private val _fetchError = MutableLiveData<Exception?>(null)
//  val fetchError: LiveData<Exception?> = _fetchError

  fun onPtvDataRequired() {
//    _fetchError.value = null
    ptvWatchedStops.forEach { watchedStop ->
      if (!_stops.value!!.containsKey(watchedStop.stopId)) {
        fetchStop(_context, watchedStop.stopId, watchedStop.routeType,
          { resp ->
            _stops.value = _stops.value!! + Pair(watchedStop.stopId, resp.stop)
          },
          { error -> println(error) } // TODO
        )
      }

      if (!_routes.value!!.containsKey(watchedStop.stopId)) {
        fetchRoute(_context, watchedStop.routeId,
          { resp ->
            _routes.value = _routes.value!! + Pair(watchedStop.routeId, resp.route)
          },
          { error -> println(error) } // TODO
        )

        fetchDirectionsForRoute(_context, watchedStop.routeId,
          { resp ->
            resp.directions.forEach { d ->
              _directions.value = _directions.value!! + Pair(d.directionId, d)
            }
          },
          { error -> println(error) } // TODO
        )
      }

      if (!_departures.value!!.containsKey(getDepartureId(watchedStop))) {
        fetchDepartures(
          _context,
          watchedStop.routeId,
          watchedStop.stopId,
          watchedStop.directionId,
          3,
          { resp ->
            _departures.value = _departures.value!! + Pair(getDepartureId(watchedStop), resp.departures)
          },
          { error -> println(error) } // TODO
        )
      }
    }
  }
}

@Composable
fun PtvWidget(viewModel: PtvWidgetViewModel) {
  val routes by viewModel.routes.observeAsState()
  val stops by viewModel.stops.observeAsState()
  val departures by viewModel.departures.observeAsState()
  val directions by viewModel.directions.observeAsState()

  PollEffect("", minutesToMs(1)) {
    viewModel.onPtvDataRequired()
  }

  WithCurrentTime(1000) {now ->
    PtvWidgetView(
      stops as Map<Int, Stop>,
      routes as Map<Int, Route>,
      directions as Map<Int, Direction>,
      departures as Map<String, List<Departure>>,
      now
    )
  }
}

private fun getDeparturesForStop(stopId: Int, departures: Map<String, List<Departure>>): List<Departure> {
  return departures.values.flatten().filter { d -> d.stopId == stopId }
}

@Composable
private fun BigNumberBox(bgColor: Color, number: String, subText: String? = null) {
  val textColor = getReadableTextColor(bgColor)
  Box (
    contentAlignment = Alignment.Center,
    modifier = Modifier.width(40.dp).height(40.dp).background(bgColor).padding(5.dp)
  ) {
    Text(
      text = number,
      style = MaterialTheme.typography.h5 + TextStyle(
        textAlign = TextAlign.Center,
        color = textColor
      )
    )
    if (subText != null) {
      Text(
        subText,
        style = MaterialTheme.typography.subtitle2 + TextStyle(color = textColor)
      )
    }
  }
}

private fun getRouteColor(routeId: Int): Color {
  val ws = ptvWatchedStops.find { s -> s.routeId == routeId }
  if (ws != null) return ws.routeColor
  return Color.Magenta // Shouldn't happen
}

@Composable
private fun StopHeading(stopName: String) {
  Text(stopName, style = MaterialTheme.typography.h6, modifier = Modifier.padding(0.dp, 5.dp))
}

@Composable
fun PtvWidgetView(
  stops: Map<Int, Stop>,
  routes: Map<Int, Route>,
  directions: Map<Int, Direction>,
  departures: Map<String, List<Departure>>,
  now: DateTime
) {
  WidgetCard(scrollable = Scrollable.vertical) {
    stops.values.forEach {stop ->
      val departuresForStop = getDeparturesForStop(stop.stopId, departures)

      StopHeading(stop.stopName)

      departuresForStop.forEach { d ->
        Row (Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
          val route = routes.get(d.routeId)
          if (route != null) {
            BigNumberBox(getRouteColor(route.routeId), route.routeNumber)
          } else {
            Text("?")
          }

          val direction = directions.get(d.directionId)
          if (direction != null) {
            Box(
              modifier = Modifier.height(40.dp).padding(5.dp),
              contentAlignment = Alignment.CenterStart
            ) {
              Text(direction.directionName)
            }
          } else {
            Text("?")
          }

          if (d.estimatedDepartureUtc != null) {
            BigNumberBox(Color.Black, getTimeRemaining(DateTime(d.estimatedDepartureUtc), now))
          } else {
            BigNumberBox(Color.Black, getTimeRemaining(DateTime(d.scheduledDepartureUtc), now), "Scheduled")
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
    PtvWidgetView(mapOf(
      ptvWatchedStops.get(0).stopId to Stop(
        routeType = ptvWatchedStops.get(0).routeType,
        stopId = ptvWatchedStops.get(0).stopId,
        stopName = "My fancy stop",
      ),
      ptvWatchedStops.get(1).stopId to Stop(
        routeType = ptvWatchedStops.get(1).routeType,
        stopId = ptvWatchedStops.get(1).stopId,
        stopName = "Another stop",
      ),
    ), mapOf(
      ptvWatchedStops.get(0).routeId to Route(
        routeType = ptvWatchedStops.get(0).routeType,
        routeId = ptvWatchedStops.get(1).routeId,
        routeName = "Melbourne - Frankston",
        routeNumber = "111",
      ),
      ptvWatchedStops.get(1).routeId to Route(
        routeType = ptvWatchedStops.get(1).routeType,
        routeId = ptvWatchedStops.get(1).routeId,
        routeName = "Foo - Bar",
        routeNumber = "222",
      ),
      ptvWatchedStops.get(2).routeId to Route(
        routeType = ptvWatchedStops.get(2).routeType,
        routeId = ptvWatchedStops.get(2).routeId,
        routeName = "Hello - World",
        routeNumber = "333",
      ),
    ),
    mapOf(
      ptvWatchedStops.get(0).directionId to Direction(
        directionId = ptvWatchedStops.get(0).directionId,
        directionName = "Frankston",
        routeDirectionDescription = "Goes to Frankston",
        routeId = ptvWatchedStops.get(0).routeId,
        routeType = ptvWatchedStops.get(0).routeType
      ),
      ptvWatchedStops.get(1).directionId to Direction(
        directionId = ptvWatchedStops.get(1).directionId,
        directionName = "Bar",
        routeDirectionDescription = "Goes to Bar",
        routeId = ptvWatchedStops.get(1).routeId,
        routeType = ptvWatchedStops.get(1).routeType
      ),
      ptvWatchedStops.get(2).directionId to Direction(
        directionId = ptvWatchedStops.get(2).directionId,
        directionName = "World",
        routeDirectionDescription = "Goes to World",
        routeId = ptvWatchedStops.get(2).routeId,
        routeType = ptvWatchedStops.get(2).routeType
      ),
    ),
    mapOf(
      getDepartureId(ptvWatchedStops.get(0)) to listOf(Departure(
        stopId = ptvWatchedStops.get(0).stopId,
        routeId = ptvWatchedStops.get(0).routeId,
        runId = 8871,
        runRef = "8871",
        directionId = ptvWatchedStops.get(0).directionId,
        disruptionIds = listOf(),
        scheduledDepartureUtc = "2021-06-01T10:33:00Z",
        estimatedDepartureUtc = "2021-06-01T10:33:00Z",
        atPlatform = false,
        platformNumber = null,
        flags = "RUN_97",
        departureSequence = 0
      ), Departure(
        stopId = ptvWatchedStops.get(0).stopId,
        routeId = ptvWatchedStops.get(0).routeId,
        runId = 8871,
        runRef = "8871",
        directionId = ptvWatchedStops.get(0).directionId,
        disruptionIds = listOf(),
        scheduledDepartureUtc = "2021-06-01T10:49:00Z",
        estimatedDepartureUtc = "2021-06-01T10:49:00Z",
        atPlatform = false,
        platformNumber = null,
        flags = "RUN_98",
        departureSequence = 0
      )),
    ), DateTime("2021-06-01T10:32:30Z"))
  }
}

@Preview(showBackground = true)
@Composable
fun LoadingPreview() {
  HomeMonitorTheme {
    PtvWidgetView(mapOf(), mapOf(), mapOf(), mapOf(), DateTime("2021-06-01T10:45:00Z"))
  }
}

//@Preview(showBackground = true)
//@Composable
//fun ErrorPreview() {
//  HomeMonitorTheme {
//    PtvWidgetView(null, Exception("This is an error"), { })
//  }
//}
