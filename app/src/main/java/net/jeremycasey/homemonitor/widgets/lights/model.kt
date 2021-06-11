package net.jeremycasey.homemonitor.widgets.lights

data class Light(
  val modelid: String,
  val name: String,
  val pointsymbol: Map<String, String>,
  val state: LightState,
  val swversion: String,
  val type: String
)

data class LightState(
  val alert: String,
  val bri: Int,
  val colormode: String,
  val ct: Int,
  val effect: String,
  val hue: Int,
  val on: Boolean,
  val reachable: Boolean,
  val sat: Int,
  val xy: List<Double>
)

data class LightGroup(
  val action: LightGroupAction,
  val `class`: String,
  val lights: List<String>,
//  val locations: Map<String, List<Double>>,
  val name: String,
  val recycle: Boolean,
  val sensors: List<Any>,
  val state: LightGroupState,
//  val stream: Stream,
  val type: String
)

data class LightGroupAction(
  val alert: String,
  val bri: Int,
  val colormode: String? = null,
  val ct: Int? = null,
  val effect: String? = null,
  val hue: Int? = null,
  val on: Boolean,
  val sat: Int? = null,
  val xy: List<Double>? = null,
)

data class LightGroupState(
  val allOn: Boolean,
  val anyOn: Boolean
)

data class Stream(
  val active: Boolean,
  val owner: Any,
  val proxymode: String,
  val proxynode: String
)

data class LightGroupStateAction(
  val hue: Int?,
  val on: Boolean?
)
