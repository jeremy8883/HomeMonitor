package net.jeremycasey.homemonitor.widgets.ptv

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import net.jeremycasey.homemonitor.composables.LoadingPanel
import net.jeremycasey.homemonitor.ui.theme.HomeMonitorTheme
import net.jeremycasey.homemonitor.utils.round
import net.jeremycasey.homemonitor.composables.WidgetCard
import net.jeremycasey.homemonitor.private.ptvWatchedStops
import net.jeremycasey.homemonitor.widgets.ptv.api.fetchDepartures
import net.jeremycasey.homemonitor.widgets.ptv.api.fetchRoute
import net.jeremycasey.homemonitor.widgets.ptv.api.fetchStop
import net.jeremycasey.homemonitor.widgets.weather.api.*
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

  LaunchedEffect("") {
    viewModel.onPtvDataRequired()
  }

  PtvWidgetView(stops as Map<Int, Stop>, routes as Map<Int, Route>, departures as Map<String, List<Departure>>)
}

@Composable
fun PtvWidgetView(stops: Map<Int, Stop>, routes: Map<Int, Route>, departures: Map<String, List<Departure>>) {
  WidgetCard {
    ptvWatchedStops.map {watchedStop ->
      val stop = stops.get(watchedStop.stopId)
      val route = routes.get(watchedStop.routeId)
      val departure = departures.get(getDepartureId(watchedStop))

      Column (modifier = Modifier.padding(0.dp, 0.dp, 5.dp, 0.dp)) {
        if (stop == null || route == null) {
          LoadingPanel()
        } else {
          Text(
            text = route.routeNumber,
            style = MaterialTheme.typography.subtitle2,
          )
          if (departure == null) {
            Text("...")
          } else {
            departure.map { d ->
              if (d.estimatedDepartureUtc != null) {
                Text(DateTime(d.estimatedDepartureUtc).toLocalTime().toString())
              } else {
                Text(DateTime(d.scheduledDepartureUtc).toLocalTime().toString())
              }
            }
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
    ), mapOf(
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
    ))
  }
}

@Preview(showBackground = true)
@Composable
fun LoadingPreview() {
  HomeMonitorTheme {
    PtvWidgetView(mapOf(), mapOf(), mapOf())
  }
}

//@Preview(showBackground = true)
//@Composable
//fun ErrorPreview() {
//  HomeMonitorTheme {
//    PtvWidgetView(null, Exception("This is an error"), { })
//  }
//}
