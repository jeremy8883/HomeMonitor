package net.jeremycasey.homemonitor.widgets.ptv.api

import GsonRequest
import android.content.Context
import com.android.volley.Response
import net.jeremycasey.homemonitor.private.ptvApiKey
import net.jeremycasey.homemonitor.private.ptvDevId
import net.jeremycasey.homemonitor.widgets.ptv.*
import java.security.Key
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

private fun appendQueryParam(url: String, key: String, value: String): String {
  var ret = url
  if (url.contains('?')) {
    ret += "&"
  } else {
    ret += "?"
  }
  return "$ret$key=$value"
}

//fun _buildTtapiUrl(
//  path: String,
//  developerId: String,
//  privateKey: String
//): String {
////  val uriWithDeveloperID = Uri.parse("http://timetableapi.ptv.vic.gov.au$path")
////    .buildUpon()
////    .appendQueryParameter("devid", developerId)
////    .build()
////    .toString()
//  val uriWithDeveloperID = appendQueryParam("http://timetableapi.ptv.vic.gov.au$path", "devid", developerId)
//
//  val keyBytes = privateKey.toByteArray()
//  val uriBytes = uriWithDeveloperID.toByteArray()
//  val signingKey: Key = SecretKeySpec(keyBytes, "HmacSHA1")
//  val mac: Mac = Mac.getInstance("HmacSHA1")
//  mac.init(signingKey)
//  val signatureBytes: ByteArray = mac.doFinal(uriBytes)
//  val signature = StringBuffer(signatureBytes.size * 2)
//  for (signatureByte in signatureBytes) {
//    val intVal: Int = signatureByte.toInt() and 0xff
//    if (intVal < 0x10) {
//      signature.append("0")
//    }
//    signature.append(Integer.toHexString(intVal))
//  }
//
//  return appendQueryParam(uriWithDeveloperID, "signature", signature.toString().toUpperCase())
////  return Uri.parse(uriWithDeveloperID)
////    .buildUpon()
////    .appendQueryParameter("signature", signature.toString().toUpperCase())
////    .build()
//}

fun _buildTtapiUrl(path: String, developerId: String, privateKey: String): String {
  val ret = TtApiUrl.buildTTAPIURL("https://timetableapi.ptv.vic.gov.au", privateKey, path, developerId)
  println(ret)
  return ret
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
    getPtvUrl("/v3/routes/$routeId"),
    RouteResponse::class.java,
    null,
    listener,
    errorListener
  )
  VolleySingleton.getInstance(context).addToRequestQueue(request)
}

data class DirectionResponse(
  val directions: List<Direction>,
  val status: Status
)

// GET /v3/directions/{direction_id}/route_type/{route_type}
// http://timetableapi.ptv.vic.gov.au/swagger/ui/index#!/Directions/Directions_ForDirectionAndType
fun fetchDirection(context: Context, directionId: Int, routeType: RouteType, listener: Response.Listener<DirectionResponse>, errorListener: Response.ErrorListener) {
  val request = GsonRequest<DirectionResponse>(
    getPtvUrl("/v3/directions/$directionId/route_type/${routeType.value}"),
    DirectionResponse::class.java,
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