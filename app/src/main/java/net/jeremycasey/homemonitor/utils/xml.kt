package net.jeremycasey.homemonitor.utils

import org.w3c.dom.Document
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.InputStream
import javax.xml.parsers.DocumentBuilderFactory

fun parseXml(xml: String): Document {
  val factory = DocumentBuilderFactory.newInstance()
  factory.setIgnoringElementContentWhitespace(true)
  val builder = factory.newDocumentBuilder()
  val xmlAsStream: InputStream = xml.byteInputStream(Charsets.UTF_8)

  return builder.parse(xmlAsStream)
}

fun getElement(element: Node, path: Array<Any>): Node {
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

fun findNode(nodeList: NodeList, predicate: (node: Node) -> Boolean): Node? {
  for (i in 0 until nodeList.length) {
    val n = nodeList.item(i)
    if (predicate(n)) {
      return n
    }
  }
  return null
}

fun getAttribute(node: Node, name: String): String? {
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

/**
 * Sometimes whitespace will return as a "node". This function clears all of that.
 */
fun filterOutTextNodes(nodeList: NodeList): ArrayList<Node> {
  val newNodeList = arrayListOf<Node>()

  for (i in 0 until nodeList.length) {
    val item = nodeList.item(i)

    if (item.nodeName != "#text") {
      newNodeList.add(item)
    }
  }
  return newNodeList
}
