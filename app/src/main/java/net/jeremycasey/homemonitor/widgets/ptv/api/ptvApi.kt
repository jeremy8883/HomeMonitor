package net.jeremycasey.homemonitor.widgets.ptv.api

import GsonRequest
import VolleySingleton
import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import net.jeremycasey.homemonitor.private.ptvApiKey
import net.jeremycasey.homemonitor.private.ptvDevId
import net.jeremycasey.homemonitor.widgets.ptv.*

fun _buildTtapiUrl(path: String, developerId: String, privateKey: String): String {
  return TtApiUrl.buildTTAPIURL("https://timetableapi.ptv.vic.gov.au", privateKey, path, developerId)
}

private fun getPtvUrl(path: String): String {
  return _buildTtapiUrl(path, ptvDevId, ptvApiKey)
}

data class Status(
  val health: Int,
  val version: String
)

data class StopResponse(
  // val disruptions: Disruptions,
  val status: Status,
  val stop: Stop
)

// GET /v3/stops/{stop_id}/route_type/{route_type}
// http://timetableapi.ptv.vic.gov.au/swagger/ui/index#!/Stops/Stops_StopDetails
fun fetchStop(context: Context, stopId: Int, routeType: RouteType, listener: Response.Listener<StopResponse>, errorListener: Response.ErrorListener) {
  val request = GsonRequest<StopResponse>(
    Request.Method.GET,
    getPtvUrl("/v3/stops/$stopId/route_type/${routeType.value}"),
    StopResponse::class.java,
    null,
    listener,
    errorListener
  )
  VolleySingleton.getInstance(context).addToRequestQueue(request)
}

data class RouteResponse(
  val route: Route,
  val status: Status
)

// GET /v3/routes/{route_id}View route name and number for specific route ID
// http://timetableapi.ptv.vic.gov.au/swagger/ui/index#!/Stops/Stops_StopDetails
fun fetchRoute(context: Context, routeId: Int, listener: Response.Listener<RouteResponse>, errorListener: Response.ErrorListener) {
  val request = GsonRequest<RouteResponse>(
    Request.Method.GET,
    getPtvUrl("/v3/routes/$routeId"),
    RouteResponse::class.java,
    null,
    listener,
    errorListener
  )
  VolleySingleton.getInstance(context).addToRequestQueue(request)
}

data class DirectionsResponse(
  val directions: List<Direction>,
  val status: Status
)

// GET /v3/directions/route/{route_id}
// Get the possible directions a route can go
// http://timetableapi.ptv.vic.gov.au/swagger/ui/index#!/Stops/Stops_StopDetails
fun fetchDirectionsForRoute(context: Context, routeId: Int, listener: Response.Listener<DirectionsResponse>, errorListener: Response.ErrorListener) {
  val request = GsonRequest<DirectionsResponse>(
    Request.Method.GET,
    getPtvUrl("/v3/directions/route/$routeId"),
    DirectionsResponse::class.java,
    null,
    listener,
    errorListener
  )
  VolleySingleton.getInstance(context).addToRequestQueue(request)
}

data class DeparturesResponse(
  val departures: List<Departure>,
  val directions: Any,
  val disruptions: Any,
  val routes: Any,
  val runs: Any,
  val status: Status,
  val stops: Any
)

// GET /v3/departures/route_type/{route_type}/stop/{stop_id}
// direction_id, gtfs, date_utc, max_results, include_cancelled, look_backwards, expand, include_geopath
// http://timetableapi.ptv.vic.gov.au/swagger/ui/index#!/Departures/Departures_GetForStopAndRoute
fun fetchDepartures(context: Context, routeId: Int, stopId: Int, directionId: Int, maxResults: Int, listener: Response.Listener<DeparturesResponse>, errorListener: Response.ErrorListener) {
  val request = GsonRequest<DeparturesResponse>(
    Request.Method.GET,
    getPtvUrl("/v3/departures/route_type/1/stop/$stopId/route/$routeId" +
        "?direction_id=$directionId&max_results=$maxResults"),
    DeparturesResponse::class.java,
    null,
    listener,
    errorListener
  )
  VolleySingleton.getInstance(context).addToRequestQueue(request)
}

// GET /v3/disruptions/route/{route_id}/stop/{stop_id}
// disruption_status
// http://timetableapi.ptv.vic.gov.au/swagger/ui/index#!/Disruptions/Disruptions_GetDisruptionsByRouteAndStop
//fun fetchDisruptions(context: Context, listener: Response.Listener<CurrentWeather>, errorListener: Response.ErrorListener) {
//
//}