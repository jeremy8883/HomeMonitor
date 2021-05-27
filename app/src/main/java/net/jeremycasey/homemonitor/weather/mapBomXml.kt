package net.jeremycasey.homemonitor.weather
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.InputStream
import javax.xml.parsers.DocumentBuilderFactory

// Codes from:
// http://www.bom.gov.au/catalogue/anon-ftp.shtml
// http://www.bom.gov.au/catalogue/data-feeds.shtml
// Current weather
// ftp://ftp.bom.gov.au/anon/gen/fwo/IDV60920.xml
// 7 days
// ftp://ftp.bom.gov.au/anon/gen/fwo/IDV10753.xml

data class WeatherForDay (
  val airTemperatureMinimum: String,
  val airTemperatureMaximum: String,
  val endTimeLocal: String,
  val endTimeUtc: String,
  val forecastIconCode: String,
  val index: Number,
  val startTimeLocal: String,
  val startTimeUtc: String,
  val precis: String,
  val probabilityOfPrecipitation: String,
)

private fun getElement(element: Node, path: Array<Any>): Node {
  val pathItem = path[0]

  if (pathItem is Number) {
    return element.childNodes.item(pathItem as Int)
  } else if (pathItem is String) {
    for (nodeIndex in 0 until element.childNodes.length) {
      val node: Node = element.childNodes.item(nodeIndex)

      if (node.nodeName == pathItem as String) {
        if (path.size == 1) {
          return node
        } else {
          return getElement(node, path.sliceArray(1 until path.size))
        }
      }
    }
  }
  throw Exception("Could not find entry at $pathItem")
}

private fun findNode(nodeList: NodeList, predicate: (node: Node) -> Boolean): Node? {
  for (i in 0 until nodeList.length) {
    val n = nodeList.item(i)
    if (predicate(n)) {
      return n
    }
  }
  return null
}

private fun getAttribute(node: Node, name: String): String? {
  if (node.attributes == null) {
    return null
  }
  for (i in 0 until node.attributes.length) {
    val a = node.attributes.item(i)
    if (a != null && a.nodeName == name) {
      return a.nodeValue
    }
  }
  return null
}

private inline fun <reified T>mapNodes(nodeList: NodeList, cb: (node: Node) -> T): Array<T> {
  return Array<T>(nodeList.length) { i -> cb(nodeList.item(i)) }
}

private fun filterTextNodes(nodeList: NodeList): ArrayList<Node> {
  val newNodeList = arrayListOf<Node>()

  for (i in 0 until nodeList.length) {
    val item = nodeList.item(i)

    if (item.nodeName != "#text") {
      newNodeList.add(item)
    }
  }
  return newNodeList
}

private fun getValueOfType(node: Node, type: String): String? {
  val node = findNode(node.childNodes, {
    childNode ->
      getAttribute(childNode, "type") == type
  })
  println(node != null)
  if (node != null) {
    println(node.firstChild.nodeValue)
  }

  return node?.firstChild?.nodeValue
}

private fun withDefault(value: String?, default: String): String {
  if (value == null) return default else return value
}

private fun getForecast(documentElement: Element): List<WeatherForDay> {
  val areas = getElement(documentElement, arrayOf("forecast")).childNodes
  val melbourneArea = findNode(areas, { node -> node.nodeName == "area" && getAttribute(node, "aac") == "VIC_PT042" }) as Node
  // I think all of the white space is coming up as xml nodes
  val forcastsNodes = filterTextNodes(melbourneArea.childNodes)

  val forecasts = forcastsNodes.map { node ->
    WeatherForDay(
      airTemperatureMinimum = withDefault(getValueOfType(node, "air_temperature_minimum"), "-"),
      airTemperatureMaximum = withDefault(getValueOfType(node, "air_temperature_maximum"), "-"),
      endTimeLocal = getAttribute(node, "end-time-local") as String,
      endTimeUtc = getAttribute(node, "end-time-local") as String,
      forecastIconCode = getValueOfType(node, "forecast_icon_code") as String,
      index = (getAttribute(node, "index") as String).toInt(),
      startTimeLocal = getAttribute(node, "start-time-local") as String,
      startTimeUtc = getAttribute(node, "start-time-utc") as String,
      precis = getValueOfType(node, "precis") as String,
      probabilityOfPrecipitation = getValueOfType(node, "probability_of_precipitation") as String,
    )
  }

  printObject(forecasts)

  return forecasts
}

private fun parseXml(xml: String): Document {
  val factory = DocumentBuilderFactory.newInstance()
  factory.setValidating(true)
  factory.setIgnoringElementContentWhitespace(true)
  val builder = factory.newDocumentBuilder()
  val xmlAsStream: InputStream = xml.byteInputStream(Charsets.UTF_8)

  return builder.parse(xmlAsStream)
}

fun mapBomXml(xml: String): List<WeatherForDay> {
  val parsed = parseXml(xml)

  val forecast = getForecast(parsed.documentElement)

  return forecast
}