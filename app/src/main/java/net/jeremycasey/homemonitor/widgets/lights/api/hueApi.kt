package net.jeremycasey.homemonitor.widgets.lights.api

import GsonRequest
import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.StringRequest
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import net.jeremycasey.homemonitor.private.*
import net.jeremycasey.homemonitor.widgets.lights.Light
import net.jeremycasey.homemonitor.widgets.lights.LightGroup
import net.jeremycasey.homemonitor.widgets.lights.LightGroupStateAction
import net.jeremycasey.homemonitor.widgets.lights.LightState
import java.lang.reflect.Type
import java.nio.charset.Charset

private fun getHueUrl(path: String): String {
  return "${hueHostAddress}api/${hueUsername}${path}"
}

//fun fetchLightInfo(id: String, context: Context, listener: Response.Listener<Light>, errorListener: Response.ErrorListener) {
//  val request = GsonRequest<Light>(
//    getHueUrl("/lights/$id"),
//    Light::class.java,
//    null,
//    listener,
//    errorListener
//  )
//  VolleySingleton.getInstance(context).addToRequestQueue(request)
//}
//
///**
// * More info http://www.burgestrand.se/hue-api/api/lights/
// * @param state {
// *   on?: boolean,
// *   sat?: number, - saturation, in range 0 - 254.
// *   bri?: number, - brightness, in range 0 - 254. 0 is not off
// *   hue?: number, - hue, in range 0 - 65535
// * }
// */
//fun changeLightState(id: String, state: LightState, context: Context, listener: Response.Listener<Light>, errorListener: Response.ErrorListener) {
//  val request = GsonRequest<Light>(
//    getHueUrl("/lights/$id/state"),
//    Light::class.java, // TODO PUT, body: state: LightState
//    null,
//    listener,
//    errorListener
//  )
//  VolleySingleton.getInstance(context).addToRequestQueue(request)
//}

fun fetchGroups(context: Context, listener: (lightGroups: Map<String, LightGroup>) -> Unit, errorListener: Response.ErrorListener) {
  val request = StringRequest(
    Request.Method.GET, getHueUrl("/groups"),
    { response ->
      val gson = GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create()
      val groups = gson.fromJson(response, Map::class.java)
      listener(groups as Map<String, LightGroup>)
    },
    errorListener
  )

  VolleySingleton.getInstance(context).addToRequestQueue(request)
}

/**
 * More info http://www.burgestrand.se/hue-api/api/groups/
 * @param state {
 *   on?: boolean,
 *   hue?: number, - hue, in range 0 - 65535
 * }
 */
fun changeGroupState(id: String, state: LightGroupStateAction, context: Context, listener: Response.Listener<LightGroup>, errorListener: Response.ErrorListener) {
  val request = StringRequest(
    Request.Method.GET, getHueUrl("/lights/$id/state"),
    { response ->
      val gson = GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create()
      val groups = gson.fromJson(response, Map::class.java)
      listener(groups as Map<String, LightGroup>)
    },
    errorListener
  )

  VolleySingleton.getInstance(context).addToRequestQueue(request)

  val request = GsonRequest<LightGroup>(
    getHueUrl("/lights/$id/state"),
    LightGroup::class.java, // TODO PUT, body: state: LightGroupStateAction
    null,
    listener,
    errorListener
  )
  VolleySingleton.getInstance(context).addToRequestQueue(request)
}
