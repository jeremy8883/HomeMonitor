package net.jeremycasey.homemonitor.widgets.ptv

// Retrieved from /v3/route_types
enum class RouteType(val value: Int) {
  train(0),
  tram(1),
  bus(2),
  vline(3),
  nightBus(4)
}

data class WatchedStop(
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
//  val routeGtfsId: String,
//  val geopath: List<Any>
//  val routeServiceStatus: RouteServiceStatus,
)

//data class RouteServiceStatus(
//  val description: String,
//  val timestamp: String
//)

data class Stop(
//  val pointId = Int,
//  val operatingHours = "N",
//  val modeId = Int,
//  val stationDetailsId = Int,
//  val flexibleStopOpeningHours = String,
//  val stopContact = null,
//  val stopTicket = null,
//  val disruptionIds: List<Int>,
  val routeType: RouteType,
//  val routes: List<Route>,
//  val stationDescription: String?,
//  val stationType: String?,
//  val stopAccessibility: StopAccessibility,
//  val stopAmenities: StopAmenities,
  val stopId: Int,
//  val stopLandmark: String?,
//  val stopLocation: StopLocation?,
  val stopName: String,
//  val stopStaffing: StopStaffing
)

//data class StopAccessibility(
//  val audioCustomerInformation: Boolean,
//  val escalator: Boolean,
//  val hearingLoop: Boolean,
//  val lift: Boolean,
//  val lighting: Boolean,
//  val platformNumber: Int,
//  val stairs: Boolean,
//  val stopAccessible: Boolean,
//  val tactileGroundSurfaceIndicator: Boolean,
//  val waitingRoom: Boolean,
//  val wheelchair: Wheelchair
//)
//
//data class StopAmenities(
//  val carParking: String,
//  val cctv: Boolean,
//  val taxiRank: Boolean,
//  val toilet: Boolean
//)
//
//data class StopLocation(
//  val gps: Gps
//)
//
//data class StopStaffing(
//  val friAmFrom: String,
//  val friAmTo: String,
//  val friPmFrom: String,
//  val friPmTo: String,
//  val monAmFrom: String,
//  val monAmTo: String,
//  val monPmFrom: String,
//  val monPmTo: String,
//  val phAdditionalText: String,
//  val phFrom: String,
//  val phTo: String,
//  val satAmFrom: String,
//  val satAmTo: String,
//  val satPmFrom: String,
//  val satPmTo: String,
//  val sunAmFrom: String,
//  val sunAmTo: String,
//  val sunPmFrom: String,
//  val sunPmTo: String,
//  val thuAmFrom: String,
//  val thuAmTo: String,
//  val thuPmFrom: String,
//  val thuPmTo: String,
//  val tueAmFrom: String,
//  val tueAmTo: String,
//  val tuePmFrom: String,
//  val tuePmTo: String,
//  val wedAmFrom: String,
//  val wedAmTo: String,
//  val wedPmTo: String,
//  val wedPmFrom: String
//)

//data class Wheelchair(
//  val accessibleRamp: Boolean,
//  val lowTicketCounter: Boolean,
//  val manouvering: Boolean,
//  val parking: Boolean,
//  val raisedPlatform: Boolean,
//  val raisedPlatformShelther: Boolean,
//  val ramp: Boolean,
//  val secondaryPath: Boolean,
//  val steepRamp: Boolean,
//  val telephone: Boolean,
//  val toilet: Boolean
//)
//
//data class Gps(
//  val latitude: Int,
//  val longitude: Int
//)

data class Direction(
  val directionId: Int,
  val directionName: String,
  val routeDirectionDescription: String,
  val routeId: Int,
  val routeType: Int
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
