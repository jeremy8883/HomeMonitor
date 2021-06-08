package net.jeremycasey.homemonitor.widgets.ptv

import androidx.compose.ui.graphics.Color
import org.joda.time.LocalTime

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

data class Direction(
  val directionId: Int,
  val directionName: String,
  val routeDirectionDescription: String,
  val routeId: Int,
  val routeType: RouteType
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

data class WatchPeriod(
  val dayOfWeek: Int, // 0 = Sunday
  val startTime: LocalTime,
  val endTime: LocalTime,
)
