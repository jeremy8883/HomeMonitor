package net.jeremycasey.homemonitor.weather
import net.jeremycasey.homemonitor.utils.findNode
import net.jeremycasey.homemonitor.utils.getAttribute
import net.jeremycasey.homemonitor.utils.getElement
import net.jeremycasey.homemonitor.utils.parseXml
import org.w3c.dom.Element
import org.w3c.dom.Node

data class WeatherSummary (
  val apparentTemp: Number?, // "Feals like"
  val deltaT: Number?,
  val gustKmh: Number?,
  val windGustSpeed: Number?,
  val airTemperature: Number?, // Actual temperature
  val dewPoint: Number?,
  val pres: Number?,
  val mslPres: Number?,
  val qnhPres: Number?,
  val rainHour: Number?,
  val rainTen: Number?,
  val relHumidity: Number?,
  val visKm: Number?,
  val windDir: String?,
  val windDirDeg: Number?,
  val windSpeedKmh: Number?,
  val windSpeed: Number?,
)

private fun getStringValueOfType(node: Node, type: String): String? {
  val foundNode = findNode(node.childNodes, {
    childNode ->
      getAttribute(childNode, "type") == type
  })

  return foundNode?.firstChild?.nodeValue
}

private fun getNumberValueOfType(node: Node, type: String): Number? {
  return getStringValueOfType(node, type)?.toDouble()
}

val weatherBomId = "086338"

private fun getWeatherSummary(documentElement: Element): WeatherSummary {
  val observationsXml = getElement(documentElement, arrayOf("observations"))
  val stationXml = findNode(observationsXml.childNodes) { node ->
    node.nodeName == "station" && getAttribute(node, "bom-id") == weatherBomId
  } as Node
  val levelXml = getElement(stationXml, arrayOf("period", "level"))

  return WeatherSummary(
    apparentTemp = getNumberValueOfType(levelXml, "apparent_temp"),
    deltaT = getNumberValueOfType(levelXml, "delta_t"),
    gustKmh = getNumberValueOfType(levelXml, "gust_kmh"),
    windGustSpeed = getNumberValueOfType(levelXml, "wind_gust_spd"),
    airTemperature = getNumberValueOfType(levelXml, "air_temperature"),
    dewPoint = getNumberValueOfType(levelXml, "dew_point"),
    pres = getNumberValueOfType(levelXml, "pres"),
    mslPres = getNumberValueOfType(levelXml, "msl_pres"),
    qnhPres = getNumberValueOfType(levelXml, "qnh_pres"),
    rainHour = getNumberValueOfType(levelXml, "rain_hour"),
    rainTen = getNumberValueOfType(levelXml, "rain_ten"),
    relHumidity = getNumberValueOfType(levelXml, "rel-humidity"),
    visKm = getNumberValueOfType(levelXml, "vis_km"),
    windDir = getStringValueOfType(levelXml, "wind_dir"),
    windDirDeg = getNumberValueOfType(levelXml, "wind_dir_deg"),
    windSpeedKmh = getNumberValueOfType(levelXml, "wind_spd_kmh"),
    windSpeed = getNumberValueOfType(levelXml, "wind_spd"),
  )
}

/**
 * Maps the following response:
 *   ftp://ftp.bom.gov.au/anon/gen/fwo/IDV60920.xml
 * Codes from:
 *   http:www.bom.gov.au/catalogue/anon-ftp.shtml
 *   http:www.bom.gov.au/catalogue/data-feeds.shtml
 * 7 days
 *   ftp://ftp.bom.gov.au/anon/gen/fwo/IDV10753.xml
 */
fun mapWeatherSummary(xml: String): WeatherSummary {
  val parsed = parseXml(xml)
  return getWeatherSummary(parsed.documentElement)
}