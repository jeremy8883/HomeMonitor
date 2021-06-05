package net.jeremycasey.homemonitor.widgets.ptv

import androidx.compose.ui.graphics.Color

// Retrieved from /v3/route_types
enum class RouteType(val value: Int) {
  train(0),
  tram(1),
  bus(2),
  vline(3),
  nightBus(4)
}

data class WatchedStop(
  val routeColor: Color,
  val routeId: Int,
  val stopId: Int,
  val routeType: RouteType,
  val directionId: Int,
)

data class Route(
  val routeType: RouteType,
  val routeId: Int,
  val routeName: String,
  val routeNumber: String,
)

data class Stop(
  val routeType: RouteType,
  val stopId: Int,
  val stopName: String,
)

data class Departure(
  val atPlatform: Boolean,
  val departureSequence: Int,
  val directionId: Int,
  val disruptionIds: List<Any>,
  val estimatedDepartureUtc: String?,
  val flags: String,
  val platformNumber: Any?,
  val routeId: Int,
  val runId: Int,
  val runRef: String,
  val scheduledDepartureUtc: String,
  val stopId: Int
)
