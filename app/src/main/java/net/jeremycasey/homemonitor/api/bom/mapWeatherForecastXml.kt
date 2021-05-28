package net.jeremycasey.homemonitor.api.bom
import net.jeremycasey.homemonitor.utils.*
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList

data class WeatherDailyForecast (
  val airTemperatureMinimum: Number?,
  val airTemperatureMaximum: Number?,
  val endTimeLocal: String?,
  val endTimeUtc: String?,
  val forecastIconCode: String?,
  val index: Number?,
  val startTimeLocal: String?,
  val startTimeUtc: String?,
  val precis: String?,
  val probabilityOfPrecipitation: String?,
)

private inline fun <reified T>mapNodes(nodeList: NodeList, cb: (node: Node) -> T): Array<T> {
  return Array<T>(nodeList.length) { i -> cb(nodeList.item(i)) }
}

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

private fun getForecast(documentElement: Element): List<WeatherDailyForecast> {
  val areas = getElement(documentElement, arrayOf("forecast")).childNodes
  val melbourneArea = findNode(areas, { node -> node.nodeName == "area" && getAttribute(node, "aac") == "VIC_PT042" }) as Node
  val forcastsNodes = filterOutTextNodes(melbourneArea.childNodes)

  val forecasts = forcastsNodes.map { node ->
    WeatherDailyForecast(
      airTemperatureMinimum = getNumberValueOfType(node, "air_temperature_minimum"),
      airTemperatureMaximum = getNumberValueOfType(node, "air_temperature_maximum"),
      endTimeLocal = getAttribute(node, "end-time-local"),
      endTimeUtc = getAttribute(node, "end-time-local"),
      forecastIconCode = getStringValueOfType(node, "forecast_icon_code"),
      index = (getAttribute(node, "index"))?.toInt(),
      startTimeLocal = getAttribute(node, "start-time-local"),
      startTimeUtc = getAttribute(node, "start-time-utc"),
      precis = getStringValueOfType(node, "precis"),
      probabilityOfPrecipitation = getStringValueOfType(node, "probability_of_precipitation"),
    )
  }

  return forecasts
}

/**
 * Original response:
 *   ftp://ftp.bom.gov.au/anon/gen/fwo/IDV10753.xml
 */
fun mapWeatherForecastXml(xml: String): List<WeatherDailyForecast> {
  val parsed = parseXml(xml)

  return getForecast(parsed.documentElement)
}